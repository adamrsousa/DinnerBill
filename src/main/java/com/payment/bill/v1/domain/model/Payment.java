package com.payment.bill.v1.domain.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String referenceId;
    private String callbackUrl;
    private String returnUrl;
    private String expiresAt;
    private BigDecimal value;
    @Embedded
    private Buyer buyer;

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

        builder.append(this.buyer.getFirstName() + "\n");
        builder.append(this.buyer.getLastName() + "\n");
        builder.append(this.buyer.getDocument() + "\n");
        builder.append(this.buyer.getPhone() + "\n");
        builder.append(this.buyer.getEmail() + "\n");

        return builder.toString();
    }
}
