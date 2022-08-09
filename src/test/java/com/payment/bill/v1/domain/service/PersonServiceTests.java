package com.payment.bill.v1.domain.service;

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
public class PersonServiceTests {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private Person person;

    private Person newPerson;

    @BeforeEach
    public void setup() {

        person = new Person();
        person.setFirstName("Adam");
        person.setLastName("Reis");
        person.setDocument("999.999.999-99");
        person.setEmail("adam@gmail.com");
        person.setPhone("(99) 99999-9999");
        person.setPersonalBill(BigDecimal.valueOf(12));
        person.setFinalBill(BigDecimal.valueOf(14));


        newPerson = new Person();
        person.setFirstName("Adam2");
        person.setLastName("Reis2");
        person.setDocument("199.999.999-99");
        person.setEmail("adammmm@gmail.com");
        person.setPhone("(99) 99999-9998");
        person.setPersonalBill(BigDecimal.valueOf(22));
        person.setFinalBill(BigDecimal.valueOf(34));
    }

    // Teste JUnit para método PersonService.create(Person)
    @DisplayName("Teste JUnit: PersonService.create(Person)")
    @Test
    public void givenObjectPerson_whenCreate_thenReturnSavedPerson() {

        // DADO: pré-condição ou setup
        // do PersonService, vemos que utilizamos o PersonRepository.save, que precisa de stubbing.
        given(personRepository.save(person)).willReturn(person);

        // QUANDO: ação ou comportamento a ser testado
        Person savedPerson = personService.create(person);

        // ENTÃO: verificação das saídas
        System.out.println(savedPerson);
        assertThat(savedPerson).isNotNull();
    }

    // Teste JUnit para método PersonService.findById(Long) - cenário positivo
    @DisplayName("Teste JUnit: PersonService.findById(Long) - cenário positivo")
    @Test
    public void givenPersonId_whenFindById_thenReturnPerson() {

        // DADO: pré-condição ou setup
        given(personRepository.findById(person.getId())).willReturn(Optional.of(person));

        // QUANDO: ação ou comportamento a ser testado
        Person savedPerson = personService.findById(person.getId());

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
        given(personRepository.findById(person.getId())).willThrow(notFoundException);


        // QUANDO e ENTÃO: ação ou comportamento a ser testado e verificação das saídas
        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> {
            personService.findById(person.getId());
        });

    }

    // Teste JUnit para personService.findAll
    @DisplayName("Teste JUnit: PersonService.findAll(PageRequest)")
    @Test
    public void givenPeopleList_whenFindAll_thenReturnPageOfPeople() {

        // DADO: pré-condição ou setup
        List<Person> peopleList = new ArrayList<>();
        peopleList.add(person);
        peopleList.add(newPerson);
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
        given(personRepository.save(person)).willReturn(person);

        given(personRepository.findById(person.getId())).willReturn(Optional.of(person));

        // atualizando objeto:
        person.setFirstName("jjj");


        // QUANDO: ação ou comportamento a ser testado
        personService.update(person.getId(), person);

        // ENTÃO: verificação das saídas
        // conferindo se personRepository.save foi chamado apenas uma vez
        verify(personRepository, times(1)).save(person);
        verify(personRepository, times(1)).findById(person.getId());
        assertThat(person.getFirstName()).isEqualTo("jjj");

    }

    // Teste JUnit: PersonService.delete(Long)
    @DisplayName("Teste JUnit: PersonService.delete(Long)")
    @Test
    public void givenObjectPerson_whenDelete_thenReturnNoContent() {

        // DADO: pré-condição ou setup
        //build do setup
        willDoNothing().given(personRepository).deleteById(person.getId());

        // QUANDO: ação ou comportamento a ser testado
        personService.delete(person.getId());

        // ENTÃO: verificação das saídas
        verify(personRepository, times(1)).deleteById(person.getId());

    }
}
