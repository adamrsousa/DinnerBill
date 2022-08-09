package com.payment.bill.v1.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.bill.v1.ObjectsDatabase;
import com.payment.bill.v1.domain.model.Person;
import com.payment.bill.v1.domain.service.PersonService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonControllerTests extends ObjectsDatabase {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    // JUnit: PersonController.create(Person)
    @DisplayName("JUnit: PersonController.create(Person)")
    @Test
    public void givenObjectPerson_whenCreate_thenReturnCreatedPerson() throws Exception {

        // DADO: pré-condição ou setup
        given(personService.create(any(Person.class))).willAnswer(
                (invocation) -> invocation.getArgument(0));

        // QUANDO: ação ou comportamento a ser testado
        ResultActions response = mockMvc.perform(post("/people")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(person1)));

        // ENTÃO: verificação das saídas
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(person1.getFirstName())));

    }


    // JUnit: PersonController.findAll(Pageable)
    @DisplayName("JUnit: PersonController.findAll(Pageable)")
    @Test
    public void givenObjectsPerson_whenFindAll_thenReturnPeopleList() throws Exception {

        // DADO: pré-condição ou setup
        // inserindo lista de alunos
        List<Person> peopleList = Arrays.asList(person1, person2);

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
        person1.setId(personId);

        given(personService.update(any(Long.class), any(Person.class))).willReturn(person1);
        given(personService.findById(any(Long.class))).willReturn(person1);


        // QUANDO: ação ou comportamento a ser testado
        ResultActions response = mockMvc.perform(put("/people/{id}", person1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person1)));


        // ENTÃO: verificação das saídas
        response.andDo(print())
                .andExpect(status().isOk());

    }

    // JUnit: PersonController.delete(Long)
    @DisplayName("JUnit: PersonController.delete(Long)")
    @Test
    public void givenObjectPerson_whenDelete_thenReturn204() throws Exception{

        // DADO: pré-condição ou setup
        person1.setId(1L);
        willDoNothing().given(personService).delete(person1.getId());

        // QUANDO: ação ou comportamento a ser testado
        ResultActions response = mockMvc.perform(delete("/people/{id}", person1.getId()));

        // ENTÃO: verificação das saídas
        response.andDo(print())
                .andExpect(status().isNoContent());

    }
}
