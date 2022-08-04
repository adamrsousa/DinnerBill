package com.payment.bill.v1.domain.model.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    private Double value;
    @NotNull @NotEmpty @NotBlank
    private String firstName;
    @NotNull @NotEmpty @NotBlank
    private String lastName;
    @NotNull @NotEmpty @NotBlank
    private String document;
    @NotNull @NotEmpty @NotBlank @Email
    private String email;
    @NotNull @NotEmpty @NotBlank
    private String phone;

    public Payment toPayment(String callbackUrl, String returnUrl, Integer minutesForExpiration, Double value) {
        Payment payment = new Payment(minutesForExpiration);

        payment.setReferenceId(referenceId);
        payment.setCallbackUrl(callbackUrl);
        payment.setReturnUrl(returnUrl);
        payment.setValue(value);

        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setDocument(document);
        person.setEmail(email);
        person.setPhone(phone);
        person.setFinalBill(BigDecimal.valueOf(value));
        payment.setPerson(person);

        return payment;
    }
}
