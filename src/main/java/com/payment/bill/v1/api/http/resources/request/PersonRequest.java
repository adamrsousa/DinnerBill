package com.payment.bill.v1.api.http.resources.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PersonRequest {

    @ApiModelProperty(value = "Name", required = true)
    @NotEmpty(message = "String name is required")
    private String name;

    @ApiModelProperty(value = "Email")
    private String email;

    @ApiModelProperty(value = "Cell Phone")
    private Long cellPhone;

    @ApiModelProperty(value = "Personal Bill")
    private BigDecimal personalBill;

}
