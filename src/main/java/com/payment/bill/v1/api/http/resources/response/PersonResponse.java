package com.payment.bill.v1.api.http.resources.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponse {

    @ApiModelProperty(value = "Id")
    private Long id;

    @ApiModelProperty(value = "First name", required = true)
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

    @ApiModelProperty(value = "Final Bill")
    private BigDecimal finalBill;
}
