package com.payment.bill.v1.api.http.resources.request;

import com.payment.bill.v1.domain.model.Buyer;
import com.payment.bill.v1.domain.model.Payment;
import com.payment.bill.v1.domain.model.Person;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PaymentForm {

    public Payment toPayment(String callbackUrl, String returnUrl,
                             Integer minutesForExpiration, BigDecimal value,
                             Person person, String referenceId) {
        Payment payment = new Payment(minutesForExpiration);

        payment.setReferenceId(referenceId);
        payment.setCallbackUrl(callbackUrl);
        payment.setReturnUrl(returnUrl);
        payment.setValue(value);

        ModelMapper mapper = new ModelMapper();
        Buyer buyer = new Buyer();
        mapper.map(person, buyer);
        payment.setBuyer(buyer);

        return payment;
    }
}
