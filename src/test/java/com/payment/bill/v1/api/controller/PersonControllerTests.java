package com.payment.bill.v1.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.bill.v1.domain.model.Person;
import com.payment.bill.v1.domain.service.PersonService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonController.class)
@ContextConfiguration(classes=PersonController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    private Person person;

    private Person newPerson;

    @BeforeEach
    void setup (){
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

    // JUnit: PersonController.create(Person)
    @DisplayName("JUnit: PersonController.create(Person)")
    @Test
    public void givenObjectPerson_whenCreate_thenReturnCreatedPerson() throws Exception {

        // DADO: pré-condição ou setup
        given(personService.create(any(Person.class))).willAnswer(
                (invocation) -> invocation.getArgument(0));

        // QUANDO: ação ou comportamento a ser testado
        ResultActions response = mockMvc.perform(post("/people")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(person)));

        // ENTÃO: verificação das saídas
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())));

    }


    // JUnit: PersonController.findAll(Pageable)
    @DisplayName("JUnit: PersonController.findAll(Pageable)")
    @Test
    public void givenObjectsPerson_whenFindAll_thenReturnPeopleList() throws Exception {

        // DADO: pré-condição ou setup
        // inserindo lista de alunos
        List<Person> peopleList = Arrays.asList(person, newPerson);

        PageRequest pageable = PageRequest.of(0, 20);

        Page<Person> peopleListPage = new PageImpl<>(peopleList, pageable, peopleList.size());

        //stubbing
        given(personService.findAll(Mockito.any(Pageable.class))).willReturn(peopleListPage);


        // QUANDO: ação ou comportamento a ser testado
        ResultActions response = mockMvc.perform(get("/people"));


        // ENTÃO: verificação das saídas
        response.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(peopleList.size())));
    }

    // JUnit: PersonController.update(Long, PersonRequest)
    @DisplayName("JUnit: PersonController.update(Long, PersonRequest)")
    @Test
    public void givenPersonId_whenPersonById_thenReturnObjectPerson() throws Exception {

        // DADO: pré-condição ou setup
        Long personId = 1L;
        person.setId(personId);

        willDoNothing().given(personService).update(any(Long.class), any(Person.class));
        given(personService.findById(any(Long.class))).willReturn(person);


        // QUANDO: ação ou comportamento a ser testado
        ResultActions response = mockMvc.perform(put("/people/{id}", person.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)));


        // ENTÃO: verificação das saídas
        response.andDo(print())
                .andExpect(status().isNoContent());

    }

    // JUnit: PersonController.delete(Long)
    @DisplayName("JUnit: PersonController.delete(Long)")
    @Test
    public void givenObjectPerson_whenDelete_thenReturn204() throws Exception{

        // DADO: pré-condição ou setup
        person.setId(1L);
        willDoNothing().given(personService).delete(person.getId());

        // QUANDO: ação ou comportamento a ser testado
        ResultActions response = mockMvc.perform(delete("/people/{id}", person.getId()));

        // ENTÃO: verificação das saídas
        response.andDo(print())
                .andExpect(status().isNoContent());

    }
}
