package com.payment.bill.v1.domain.service;

import com.payment.bill.v1.domain.model.Person;
import com.payment.bill.v1.domain.model.exception.NotFoundException;
import com.payment.bill.v1.domain.repository.PersonRepository;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person create(Person entity) {
        return personRepository.save(entity);
    }

    public Person findById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pessoa não encontrada"));
    }

    public Page<Person> findAll(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    public void update(Long id, Person entity) {
        Person person = findById(id);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        mapper.map(entity, person);

        personRepository.save(person);
    }

    public void delete(Long id) {
        personRepository.deleteById(id);
    }

}
