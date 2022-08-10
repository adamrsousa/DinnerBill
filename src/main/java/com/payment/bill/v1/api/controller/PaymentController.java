package com.payment.bill.v1.api.controller;

import javax.validation.Valid;

import com.payment.bill.v1.api.client.PicPayClient;
import com.payment.bill.v1.api.http.resources.response.PaymentResponse;
import com.payment.bill.v1.domain.model.Payment;
import com.payment.bill.v1.api.http.resources.response.NewStatusPayment;
import com.payment.bill.v1.api.http.resources.response.PaymentGenerated;
import com.payment.bill.v1.domain.model.Person;
import com.payment.bill.v1.api.controller.exception.PaymentRequestException;
import com.payment.bill.v1.api.controller.exception.StatusChangeException;
import com.payment.bill.v1.api.http.resources.request.PaymentForm;
import com.payment.bill.v1.api.http.resources.request.StatusChangeForm;
import com.payment.bill.v1.domain.service.PaymentService;
import com.payment.bill.v1.domain.service.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Value("${picpay.url-callback-payment}")
    private String callbackUrl;

    @Value("${picpay.url-return-payment}")
    private String returnUrl;

    @Value("${picpay.minutes-for-expiration-payment}")
    private Integer minutesForExpirationPayment;


    private final PersonService personService;
    private final PaymentService paymentService;
    private final ModelMapper modelMapper;
    private final PicPayClient picPayClient;

    public PaymentController(PersonService personService,
                             PaymentService paymentService,
                             ModelMapper modelMapper,
                             PicPayClient picPayClient) {
        this.personService = personService;
        this.paymentService = paymentService;
        this.modelMapper = modelMapper;
        this.picPayClient = picPayClient;
    }

    @PostMapping
    public ResponseEntity<PaymentGenerated> generatePayment(@Valid @RequestBody PaymentForm form,
                                                            @RequestParam Long id) {
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        final Person persontoPay = personService.findById(id);
        Payment payment = form.toPayment(callbackUrl, returnUrl,
                minutesForExpirationPayment, persontoPay.getFinalBill(), persontoPay, uuidAsString);
        paymentService.create(payment);

        try {
           PaymentGenerated paymentGenerated = picPayClient.createPayment(payment);
            return ResponseEntity.ok(paymentGenerated);
        } catch (Exception ex) {
            throw new PaymentRequestException(ex.getMessage());
        }
    }

    @PostMapping("/status-changed")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NewStatusPayment> handlePaymentStatusChange(@RequestBody StatusChangeForm form) {
        try {
            NewStatusPayment newStatusPayment = picPayClient.getPaymentStatus(form.getReferenceId());
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