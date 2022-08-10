package com.payment.bill.v1.api.http.resources.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusChangeForm {

    private String referenceId;
    private String authorizationId;
}