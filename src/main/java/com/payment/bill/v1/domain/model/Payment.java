package com.payment.bill.v1.domain.model;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;
@Data
public class Payment {

    private String referenceId;
    private String callbackUrl;
    private String returnUrl;
    private String expiresAt;
    private Double value;
    private Person person;

    public Payment(Integer minutesForExpiration) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        this.expiresAt = ZonedDateTime.now().plusMinutes(minutesForExpiration).format(formatter);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(this.referenceId + "\n");
        builder.append(this.callbackUrl + "\n");
        builder.append(this.returnUrl + "\n");
        builder.append(this.expiresAt + "\n");
        builder.append(this.value + "\n");

        builder.append(this.person.getFirstName() + "\n");
        builder.append(this.person.getLastName() + "\n");
        builder.append(this.person.getDocument() + "\n");
        builder.append(this.person.getPhone() + "\n");
        builder.append(this.person.getEmail() + "\n");

        return builder.toString();
    }
}
