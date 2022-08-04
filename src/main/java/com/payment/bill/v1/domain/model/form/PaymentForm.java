package com.payment.bill.v1.domain.model.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.payment.bill.v1.domain.model.Buyer;
import com.payment.bill.v1.domain.model.Payment;
import com.payment.bill.v1.domain.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentForm {

    @NotNull @NotEmpty @NotBlank
    private String referenceId;
    @NotNull
    private BigDecimal value;

    public Payment toPayment(String callbackUrl, String returnUrl,
                             Integer minutesForExpiration, BigDecimal value, Person person) {
        Payment payment = new Payment(minutesForExpiration);

        payment.setReferenceId(referenceId);
        payment.setCallbackUrl(callbackUrl);
        payment.setReturnUrl(returnUrl);
        payment.setValue(value);

        Buyer buyer = new Buyer();
        buyer.setFirstName(person.getFirstName());
        buyer.setLastName(person.getLastName());
        buyer.setDocument(person.getDocument());
        buyer.setEmail(person.getEmail());
        buyer.setPhone(person.getPhone());
        payment.setBuyer(buyer);

        return payment;
    }
}
