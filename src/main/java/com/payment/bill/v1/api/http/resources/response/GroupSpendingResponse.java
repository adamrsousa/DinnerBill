package com.payment.bill.v1.api.http.resources.response;

import com.payment.bill.v1.domain.model.Person;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupSpendingResponse {

    @ApiModelProperty(value = "Id")
    private Long id;

    @ApiModelProperty(value = "Additionals")
    private BigDecimal additionals;

    @ApiModelProperty(value = "Discounts")
    private BigDecimal discounts;

    @ApiModelProperty(value = "Global Bill")
    private BigDecimal globalBill;

    @ApiModelProperty(value = "Has waiter additional?")
    private Boolean hasWaiterAdd;

    @ApiModelProperty(value = "List of people who are in that bill")
    private List<Person> peopleList;

}
