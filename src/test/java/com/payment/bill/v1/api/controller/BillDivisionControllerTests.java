package com.payment.bill.v1.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.bill.v1.ObjectsDatabase;
import com.payment.bill.v1.domain.model.GroupSpending;
import com.payment.bill.v1.domain.model.Person;
import com.payment.bill.v1.domain.service.GroupSpendingService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BillDivisionControllerTests extends ObjectsDatabase {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupSpendingService groupService;

    // JUnit: BillDivisionController.create(GroupSpending)
    @DisplayName("JUnit: BillDivisionController.create(GroupSpending)")
    @Test
    public void givenObjectGroupSpending_whenCreate_thenReturnCreatedGroupSpending() throws Exception {

        // DADO: pré-condição ou setup
        given(groupService.create(any(GroupSpending.class))).willAnswer(
                (invocation) -> invocation.getArgument(0));

        // QUANDO: ação ou comportamento a ser testado
        ResultActions response = mockMvc.perform(post("/billdivision")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(groupSpending)));

        // ENTÃO: verificação das saídas
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.additionals").value(groupSpending.getAdditionals()));

    }


    // JUnit: BillDivisionController.findAll(Pageable)
    @DisplayName("JUnit: BillDivisionController.findAll(Pageable)")
    @Test
    public void givenObjectsGroupSpending_whenFindAll_thenReturnPeopleList() throws Exception {

        // DADO: pré-condição ou setup
        // inserindo lista de alunos
        List<GroupSpending> groupList = Arrays.asList(groupSpending, newGroupSpending);

        PageRequest pageable = PageRequest.of(0, 20);

        Page<GroupSpending> groupListPage = new PageImpl<>(groupList, pageable, groupList.size());

        //stubbing
        given(groupService.findAll(Mockito.any(Pageable.class))).willReturn(groupListPage);


        // QUANDO: ação ou comportamento a ser testado
        ResultActions response = mockMvc.perform(get("/billdivision"));


        // ENTÃO: verificação das saídas
        response.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(groupList.size())));
    }

    // JUnit: BillDivisionController.update(Long, GroupSpendingRequest)
    @DisplayName("JUnit: BillDivisionController.update(Long, GroupSpendingRequest)")
    @Test
    public void givenGroupSpendingId_whenGroupSpendingById_thenReturnObjectGroupSpending() throws Exception {

        // DADO: pré-condição ou setup
        Long groupId = 1L;
        groupSpending.setId(groupId);

        given(groupService.update(any(Long.class), any(GroupSpending.class))).willReturn(groupSpending);
        given(groupService.findById(any(Long.class))).willReturn(groupSpending);


        // QUANDO: ação ou comportamento a ser testado
        ResultActions response = mockMvc.perform(put("/billdivision/{id}", groupSpending.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupSpending)));


        // ENTÃO: verificação das saídas
        response.andDo(print())
                .andExpect(status().isOk());

    }

    // JUnit: BillDivisionController.delete(Long)
    @DisplayName("JUnit: BillDivisionController.delete(Long)")
    @Test
    public void givenObjectGroupSpending_whenDelete_thenReturn204() throws Exception{

        // DADO: pré-condição ou setup
        groupSpending.setId(1L);
        willDoNothing().given(groupService).delete(groupSpending.getId());

        // QUANDO: ação ou comportamento a ser testado
        ResultActions response = mockMvc.perform(delete("/billdivision/{id}", groupSpending.getId()));

        // ENTÃO: verificação das saídas
        response.andDo(print())
                .andExpect(status().isNoContent());

    }
}
