package com.payment.bill.v1.domain.service;

import com.payment.bill.v1.domain.model.GroupSpending;
import com.payment.bill.v1.domain.model.Person;
import com.payment.bill.v1.domain.model.exception.NotFoundException;
import com.payment.bill.v1.domain.repository.GroupSpendingRepository;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class GroupSpendingService {

    private final GroupSpendingRepository groupSpendingRepository;

    public GroupSpendingService(GroupSpendingRepository groupSpendingRepository) {
        this.groupSpendingRepository = groupSpendingRepository;
    }

    private void division(GroupSpending entity) {

        BigDecimal billBeforeAdditionals= entity.getPeopleList().stream()
                .map(Person::getPersonalBill)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal globalBill = entity.getGlobalBill();
        BigDecimal additionals = entity.getAdditionals();
        BigDecimal discounts = entity.getDiscounts();

        BigDecimal prefinalBill= billBeforeAdditionals.add(globalBill).add(additionals).subtract(discounts);
        BigDecimal finalBill= prefinalBill;

        if (entity.getHasWaiterAdd()) {
            BigDecimal waiterAdd= prefinalBill.multiply(BigDecimal.valueOf(0.1));
            finalBill = prefinalBill.add(waiterAdd);
        }

        for (Person person: entity.getPeopleList()) {
            BigDecimal pBill= (person.getPersonalBill()).multiply(finalBill).divide(billBeforeAdditionals);
            person.setFinalBill(pBill);
        }
    }

    public GroupSpending create(GroupSpending entity) {
        division(entity);
        return groupSpendingRepository.save(entity);
    }

    public GroupSpending findById(Long id) {
        return groupSpendingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Grupo de gastos não encontrado"));
    }

    public Page<GroupSpending> findAll(Pageable pageable) {
        return groupSpendingRepository.findAll(pageable);
    }

    public void update(Long id, GroupSpending entity) {
        GroupSpending group = findById(id);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        mapper.map(entity, group);

        groupSpendingRepository.save(group);
    }

    public void delete(Long id) {
        groupSpendingRepository.deleteById(id);
    }
}