package com.payment.bill.v1.domain.service;

import com.payment.bill.v1.api.controller.exception.NotFoundException;
import com.payment.bill.v1.domain.model.Payment;
import com.payment.bill.v1.domain.model.Person;
import com.payment.bill.v1.domain.repository.PaymentRepository;
import com.payment.bill.v1.domain.repository.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment create(Payment payment) {
        return paymentRepository.save(payment);
    }
    public Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Requisição de pagamento não encontrada"));
    }

    public Page<Payment> findAll(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }
}
