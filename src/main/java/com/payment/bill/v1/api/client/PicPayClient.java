package com.payment.bill.v1.api.client;

import com.payment.bill.v1.api.http.resources.response.NewStatusPayment;
import com.payment.bill.v1.api.http.resources.response.PaymentGenerated;
import com.payment.bill.v1.domain.model.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PicPayClient {

    @Value("${picpay.url-generate-payment}")
    private String urlGeneratePayment;

    @Value("${picpay.url-status-payment}")
    private String urlStatusPayment;

    @Value("${picpay.x-picpay-token}")
    private String picpayToken;

    private final RestTemplate restTemplate;

    public PicPayClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PaymentGenerated createPayment(Payment payment) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-picpay-token", picpayToken);

        HttpEntity<Payment> entity = new HttpEntity<>(payment, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(urlGeneratePayment, entity,
                String.class);
        return new PaymentGenerated(response.getBody());
    }

    public NewStatusPayment getPaymentStatus(String referenceId){
        String url = String.format(urlStatusPayment, referenceId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-picpay-token", picpayToken);

        HttpEntity<Object> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                entity, String.class);
        return new NewStatusPayment(response.getBody());
    }
}
