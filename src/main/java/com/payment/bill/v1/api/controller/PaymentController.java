package com.payment.bill.v1.api.controller;

import javax.validation.Valid;

import com.payment.bill.v1.domain.model.Payment;
import com.payment.bill.v1.api.http.resources.dto.NewStatusPayment;
import com.payment.bill.v1.api.http.resources.dto.PaymentGenerated;
import com.payment.bill.v1.domain.model.exception.PaymentRequestException;
import com.payment.bill.v1.domain.model.exception.StatusChangeException;
import com.payment.bill.v1.domain.model.form.PaymentForm;
import com.payment.bill.v1.domain.model.form.StatusChangeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private SimpMessagingTemplate message;

    @Value("${picpay.url-generate-payment}")
    private String urlGeneratePayment;

    @Value("${picpay.url-status-payment}")
    private String urlStatusPayment;

    @Value("${picpay.url-callback-payment}")
    private String callbackUrl;

    @Value("${picpay.url-return-payment}")
    private String returnUrl;

    @Value("${picpay.minutes-for-expiration-payment}")
    private Integer minutesForExpirationPayment;

    @Value("${picpay.x-picpay-token}")
    private String picpayToken;

    @PostMapping(value = {"", "/"})
    public ResponseEntity<PaymentGenerated> generatePayment(@Valid @RequestBody PaymentForm form) throws Exception {
        Payment payment = form.toPayment(callbackUrl, returnUrl, minutesForExpirationPayment, form.getValue());

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-picpay-token", picpayToken);

        HttpEntity<Payment> entity = new HttpEntity<Payment>(payment, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(urlGeneratePayment, entity, String.class);
            return ResponseEntity.ok(new PaymentGenerated(response.getBody()));
        } catch (Exception ex) {
            throw new PaymentRequestException(ex.getMessage());
        }
    }

    @PostMapping("/status-changed")
    @ResponseStatus(HttpStatus.OK)
    public void handlePaymentStatusChange(@RequestBody StatusChangeForm form) {
        String url = String.format(urlStatusPayment, form.getReferenceId());

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-picpay-token", picpayToken);

        HttpEntity<Object> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            NewStatusPayment newStatusPayment = new NewStatusPayment(response.getBody());
            message.convertAndSend(String.format("/queue/%s", newStatusPayment.getReferenceId()), newStatusPayment.getStatus());
        } catch (Exception ex) {
            throw new StatusChangeException(ex.getMessage());
        }
    }
}
