package com.payment.bill.v1.domain.service;

import com.payment.bill.v1.domain.model.GroupSpending;
import com.payment.bill.v1.domain.model.Person;
import com.payment.bill.v1.api.controller.exception.NotFoundException;
import com.payment.bill.v1.domain.repository.GroupSpendingRepository;
import com.payment.bill.v1.domain.repository.PersonRepository;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class GroupSpendingService {

    private final GroupSpendingRepository groupSpendingRepository;
    private final PersonRepository personRepository;

    public GroupSpendingService(GroupSpendingRepository groupSpendingRepository,
                                PersonRepository personRepository) {
        this.groupSpendingRepository = groupSpendingRepository;
        this.personRepository = personRepository;
    }

    void division(GroupSpending groupSpending) {

        BigDecimal billBeforeAdditionals= groupSpending.getPeopleList().stream()
                .map(Person::getPersonalBill)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal globalBill = groupSpending.getGlobalBill();
        BigDecimal additionals = groupSpending.getAdditionals();
        BigDecimal discounts = groupSpending.getDiscounts();

        BigDecimal prefinalBill= billBeforeAdditionals.add(globalBill)
                .add(additionals)
                .subtract(discounts);
        BigDecimal finalBill= prefinalBill;

        if (groupSpending.getHasWaiterAdd()) {
            BigDecimal waiterAdd= prefinalBill.multiply(BigDecimal.valueOf(0.1));
            finalBill = prefinalBill.add(waiterAdd);
        }

        for (Person person: groupSpending.getPeopleList()) {
            BigDecimal pBill= (person.getPersonalBill()).multiply(finalBill)
                    .divide(billBeforeAdditionals, 2, RoundingMode.HALF_UP);
            person.setFinalBill(pBill);
            personRepository.save(person);
        }
    }

    public GroupSpending create(GroupSpending group) {
        division(group);
        return groupSpendingRepository.save(group);
    }

    public GroupSpending findById(Long id) {
        return groupSpendingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Grupo de gastos não encontrado"));
    }

    public Page<GroupSpending> findAll(Pageable pageable) {
        return groupSpendingRepository.findAll(pageable);
    }

    public GroupSpending update(Long id, GroupSpending group) {
        GroupSpending groupToUpdate = findById(id);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        division(group);
        mapper.map(group, groupToUpdate);

      return groupSpendingRepository.save(groupToUpdate);
    }

    public void delete(Long id) {
        groupSpendingRepository.deleteById(id);
    }
}