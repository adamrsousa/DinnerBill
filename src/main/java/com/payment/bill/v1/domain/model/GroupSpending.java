package com.payment.bill.v1.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class GroupSpending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal additionals;

    private BigDecimal discounts;

    //Um exemplo de gasto global pode ser um pacote de cerveja que a galera pediu.
    private BigDecimal globalBill;

    private Boolean hasWaiterAdd;

    @OneToMany
    private List<Person> peopleList;
}
