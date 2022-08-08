package com.payment.bill.v1.api.controller;

import javax.validation.Valid;

import com.payment.bill.v1.api.http.resources.response.PaymentResponse;
import com.payment.bill.v1.domain.model.Payment;
import com.payment.bill.v1.api.http.resources.dto.NewStatusPayment;
import com.payment.bill.v1.api.http.resources.dto.PaymentGenerated;
import com.payment.bill.v1.domain.model.Person;
import com.payment.bill.v1.api.controller.exception.PaymentRequestException;
import com.payment.bill.v1.api.controller.exception.StatusChangeException;
import com.payment.bill.v1.domain.model.form.PaymentForm;
import com.payment.bill.v1.domain.model.form.StatusChangeForm;
import com.payment.bill.v1.domain.service.PaymentService;
import com.payment.bill.v1.domain.service.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final PaymentService paymentService;
    private final ModelMapper modelMapper;

    public PaymentController(PersonService personService, PaymentService paymentService,
                             ModelMapper modelMapper) {
        this.personService = personService;
        this.paymentService = paymentService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<PaymentGenerated> generatePayment(@Valid @RequestBody PaymentForm form,
                                                            @RequestParam Long id)
            throws Exception {

        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        final Person persontoPay=personService.findById(id);
        Payment payment = form.toPayment(callbackUrl, returnUrl,
                minutesForExpirationPayment, persontoPay.getPersonalBill(), persontoPay, uuidAsString);
        paymentService.create(payment);

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

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<PaymentResponse> findById(@PathVariable(name = "id") Long id) {
        final Payment payment = paymentService.findById(id);
        PaymentResponse response = modelMapper.map(payment, PaymentResponse.class);
        return ResponseEntity.ok(response);

    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Page<PaymentResponse>> findAll(Pageable pageable) {
        Page<Payment> payments = paymentService.findAll(pageable);
        List<PaymentResponse> content = payments.stream()
                .map(item -> modelMapper.map(item, PaymentResponse.class))
                .collect(Collectors.toList());
        Page<PaymentResponse> paymentResponse = new PageImpl<>(content, pageable, payments.getTotalElements());
        return ResponseEntity.ok(paymentResponse);
    }
}