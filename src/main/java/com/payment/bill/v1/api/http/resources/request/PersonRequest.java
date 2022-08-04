package com.payment.bill.v1.api.http.resources.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PersonRequest {

    @ApiModelProperty(value = "First name", required = true)
    @NotEmpty(message = "String name is required")
    private String firstName;

    @ApiModelProperty(value = "Last name")
    private String lastName;

    @ApiModelProperty(value = "Email")
    private String email;

    @ApiModelProperty(value = "Cell Phone")
    private String phone;

    @ApiModelProperty(value = "Personal Bill")
    private BigDecimal personalBill;

    @ApiModelProperty(value = "CPF")
    private String document;




}
