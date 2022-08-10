package com.payment.bill.v1.domain.service;

import com.payment.bill.v1.domain.model.Buyer;
import com.payment.bill.v1.domain.model.Person;
import com.payment.bill.v1.api.controller.exception.NotFoundException;
import com.payment.bill.v1.domain.repository.PersonRepository;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person create(Person person) {
        return personRepository.save(person);
    }

    public Person findById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pessoa não encontrada"));
    }

    public Page<Person> findAll(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    public Person update(Long id, Person person) {
        Person personToUpdate = findById(id);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        mapper.map(person, personToUpdate);

        return personRepository.save(personToUpdate);
    }

    public void delete(Long id) {
        personRepository.deleteById(id);
    }
}
