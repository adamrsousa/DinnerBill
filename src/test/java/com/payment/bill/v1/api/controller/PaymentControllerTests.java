package com.payment.bill.v1.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.bill.v1.ObjectsDatabase;
import com.payment.bill.v1.api.client.PicPayClient;
import com.payment.bill.v1.domain.model.Payment;
import com.payment.bill.v1.domain.service.PaymentService;
import com.payment.bill.v1.domain.service.PersonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PaymentControllerTests extends ObjectsDatabase {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private PicPayClient picPayClient;

    // JUnit: PaymentController.generatePayment(PaymentForm, Long)
    @DisplayName("JUnit: PaymentController.generatePayment(PaymentForm, Long)")
    @Test
    public void givenObjectForm_whenGeneratePayment_thenReturnGeneratedPayment() throws Exception {

        // DADO: pré-condição ou setup
        given(personService.findById(any(Long.class))).willReturn(person1);
        given(paymentService.create(any(Payment.class))).willAnswer((invocation) -> invocation.getArgument(0));
        given(picPayClient.createPayment(any(Payment.class))).willReturn(paymentGenerated);

        // QUANDO: ação ou comportamento a ser testado
        ResultActions response = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(payment)));

        // ENTÃO: verificação das saídas
        response.andDo(print())
                .andExpect(status().isOk());

    }

}
