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

    @ApiModelProperty(value = "Name")
    private String name;

    @ApiModelProperty(value = "Email")
    private String email;

    @ApiModelProperty(value = "Cell Phone")
    private Long cellPhone;

    @ApiModelProperty(value = "Personal Bill")
    private BigDecimal personalBill;

    @ApiModelProperty(value = "Final Bill")
    private BigDecimal finalBill;
}
