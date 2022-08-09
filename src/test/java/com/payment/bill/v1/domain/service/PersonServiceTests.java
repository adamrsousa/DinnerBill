package com.payment.bill.v1.domain.service;

import com.payment.bill.v1.ObjectsDatabase;
import com.payment.bill.v1.api.controller.exception.NotFoundException;
import com.payment.bill.v1.domain.model.Person;
import com.payment.bill.v1.domain.repository.PersonRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonServiceTests extends ObjectsDatabase {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    // Teste JUnit para método PersonService.create(Person)
    @DisplayName("Teste JUnit: PersonService.create(Person)")
    @Test
    public void givenObjectPerson_whenCreate_thenReturnSavedPerson() {

        // DADO: pré-condição ou setup
        // do PersonService, vemos que utilizamos o PersonRepository.save, que precisa de stubbing.
        given(personRepository.save(person1)).willReturn(person1);

        // QUANDO: ação ou comportamento a ser testado
        Person savedPerson = personService.create(person1);

        // ENTÃO: verificação das saídas
        System.out.println(savedPerson);
        assertThat(savedPerson).isNotNull();
    }

    // Teste JUnit para método PersonService.findById(Long) - cenário positivo
    @DisplayName("Teste JUnit: PersonService.findById(Long) - cenário positivo")
    @Test
    public void givenPersonId_whenFindById_thenReturnPerson() {

        // DADO: pré-condição ou setup
        given(personRepository.findById(person1.getId())).willReturn(Optional.of(person1));

        // QUANDO: ação ou comportamento a ser testado
        Person savedPerson = personService.findById(person1.getId());

        // ENTÃO: verificação das saídas
        assertThat(savedPerson).isNotNull();
        System.out.println(savedPerson);

    }

    // Teste JUnit para método PersonService.findById(Long) - cenário negativo
    @DisplayName("Teste JUnit: PersonService.findById(Long) - cenário negativo")
    @Test
    public void givenPersonId_whenFindById_thenReturnExcecao() {

        NotFoundException notFoundException = new NotFoundException("Não encontrado");

        // DADO: pré-condição ou setup
        given(personRepository.findById(person1.getId())).willThrow(notFoundException);


        // QUANDO e ENTÃO: ação ou comportamento a ser testado e verificação das saídas
        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> {
            personService.findById(person1.getId());
        });

    }

    // Teste JUnit para personService.findAll
    @DisplayName("Teste JUnit: PersonService.findAll(PageRequest)")
    @Test
    public void givenPeopleList_whenFindAll_thenReturnPageOfPeople() {

        // DADO: pré-condição ou setup
        List<Person> peopleList = new ArrayList<>();
        peopleList.add(person1);
        peopleList.add(person2);
        Page<Person> page = new PageImpl<>(peopleList);
        PageRequest pageRequest = PageRequest.of(1, 20);

        // stubbing
        given(personRepository.findAll(pageRequest)).willReturn(page);


        // QUANDO: ação ou comportamento a ser testado
        Page<Person> response = personService.findAll(pageRequest);


        // ENTÃO: verificação das saídas
        System.out.println(response.toString());
        assertThat(response).isNotNull();
        response.getContent().forEach(x -> {
            assertThat(x).isOfAnyClassIn(Person.class);
            System.out.println(x.toString());
        });

    }

    // Teste JUnit: PersonService.update(Long, Person)
    @DisplayName("Teste JUnit: PersonService.update(Long, Person)")
    @Test
    public void givenObjectPerson_whenUpdatePerson_thenReturnUpdatedPerson() {

        // DADO: pré-condição ou setup
        given(personRepository.save(person1)).willReturn(person1);

        given(personRepository.findById(person1.getId())).willReturn(Optional.of(person1));

        // atualizando objeto:
        person1.setFirstName("jjj");


        // QUANDO: ação ou comportamento a ser testado
        personService.update(person1.getId(), person1);

        // ENTÃO: verificação das saídas
        // conferindo se personRepository.save foi chamado apenas uma vez
        verify(personRepository, times(1)).save(person1);
        verify(personRepository, times(1)).findById(person1.getId());
        assertThat(person1.getFirstName()).isEqualTo("jjj");

    }

    // Teste JUnit: PersonService.delete(Long)
    @DisplayName("Teste JUnit: PersonService.delete(Long)")
    @Test
    public void givenObjectPerson_whenDelete_thenReturnNoContent() {

        // DADO: pré-condição ou setup
        //build do setup
        willDoNothing().given(personRepository).deleteById(person1.getId());

        // QUANDO: ação ou comportamento a ser testado
        personService.delete(person1.getId());

        // ENTÃO: verificação das saídas
        verify(personRepository, times(1)).deleteById(person1.getId());

    }
}
