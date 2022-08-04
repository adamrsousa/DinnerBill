package com.payment.bill.v1.api.controller;

import javax.validation.Valid;

import com.payment.bill.v1.domain.model.Buyer;
import com.payment.bill.v1.domain.model.Payment;
import com.payment.bill.v1.api.http.resources.dto.NewStatusPayment;
import com.payment.bill.v1.api.http.resources.dto.PaymentGenerated;
import com.payment.bill.v1.domain.model.Person;
import com.payment.bill.v1.api.controller.exception.PaymentRequestException;
import com.payment.bill.v1.api.controller.exception.StatusChangeException;
import com.payment.bill.v1.domain.model.form.PaymentForm;
import com.payment.bill.v1.domain.model.form.StatusChangeForm;
import com.payment.bill.v1.domain.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@RestController
@RequestMapping("/payments")
public class PaymentController {

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

    private final PersonService personService;

    public PaymentController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<PaymentGenerated> generatePayment(@Valid @RequestBody PaymentForm form)
            throws Exception {

        final Person persontoPay=personService.findById(1L);
        Payment payment = form.toPayment(callbackUrl, returnUrl,
                minutesForExpirationPayment, persontoPay.getFinalBill(), persontoPay);
        Buyer buyer = personService.mapToBuyer(persontoPay);
        payment.setBuyer(buyer);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-picpay-token", picpayToken);

        HttpEntity<Payment> entity = new HttpEntity<>(payment, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(urlGeneratePayment, entity,
                    String.class);
            PaymentGenerated paymentGenerated = new PaymentGenerated(response.getBody());
            return ResponseEntity.ok(paymentGenerated);
        } catch (Exception ex) {
            throw new PaymentRequestException(ex.getMessage());
        }
    }

    @PostMapping("/status-changed")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NewStatusPayment> handlePaymentStatusChange(@RequestBody StatusChangeForm form) {
        String url = String.format(urlStatusPayment, form.getReferenceId());

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-picpay-token", picpayToken);

        HttpEntity<Object> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            NewStatusPayment newStatusPayment = new NewStatusPayment(response.getBody());
            return ResponseEntity.ok(newStatusPayment);
        } catch (Exception ex) {
            throw new StatusChangeException(ex.getMessage());
        }
    }
}