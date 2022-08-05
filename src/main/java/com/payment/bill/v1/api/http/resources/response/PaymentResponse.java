package com.payment.bill.v1.api.http.resources.response;

import com.payment.bill.v1.domain.model.Buyer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    @ApiModelProperty(value = "Id")
    private Long id;

    @ApiModelProperty(value = "Reference Id")
    private String referenceId;

    @ApiModelProperty(value = "expires at")
    private String expiresAt;

    @ApiModelProperty(value = "Value")
    private BigDecimal value;

    @ApiModelProperty(value = "Buyer")
    private Buyer buyer;
}
