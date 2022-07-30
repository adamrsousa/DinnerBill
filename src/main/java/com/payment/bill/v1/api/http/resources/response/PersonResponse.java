package com.payment.bill.v1.api.http.resources.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PersonResponse {

    @ApiModelProperty(value = "Name")
    private String name;

    @ApiModelProperty(value = "Personal Bill")
    private BigDecimal personalBill;

    @ApiModelProperty(value = "Final Bill")
    private BigDecimal finalBill;
}
