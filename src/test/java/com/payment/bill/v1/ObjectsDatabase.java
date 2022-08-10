package com.payment.bill.v1;

import com.payment.bill.v1.api.http.resources.request.PaymentForm;
import com.payment.bill.v1.api.http.resources.response.PaymentGenerated;
import com.payment.bill.v1.domain.model.Buyer;
import com.payment.bill.v1.domain.model.GroupSpending;
import com.payment.bill.v1.domain.model.Payment;
import com.payment.bill.v1.domain.model.Person;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectsDatabase {

    protected Person person1;

    protected Person person2;

    protected GroupSpending groupSpending;

    protected GroupSpending newGroupSpending;

    protected Payment payment;

    protected PaymentGenerated paymentGenerated;

    public ObjectsDatabase() {

        person1 = new Person();
        person1.setFirstName("Adam");
        person1.setLastName("Reis");
        person1.setDocument("999.999.999-99");
        person1.setEmail("adam@gmail.com");
        person1.setPhone("(99) 99999-9999");
        person1.setPersonalBill(BigDecimal.valueOf(12));
        person1.setFinalBill(BigDecimal.valueOf(14));


        person2 = new Person();
        person2.setFirstName("Adam2");
        person2.setLastName("Reis2");
        person2.setDocument("199.999.999-99");
        person2.setEmail("adammmm@gmail.com");
        person2.setPhone("(99) 99999-9998");
        person2.setPersonalBill(BigDecimal.valueOf(22));
        person2.setFinalBill(BigDecimal.valueOf(34));


        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person2);

        groupSpending = new GroupSpending();
        groupSpending.setAdditionals(BigDecimal.valueOf(20));
        groupSpending.setDiscounts(BigDecimal.valueOf(27));
        groupSpending.setGlobalBill(BigDecimal.valueOf(69));
        groupSpending.setHasWaiterAdd(false);
        groupSpending.setPeopleList(personList);


        newGroupSpending = new GroupSpending();
        newGroupSpending.setAdditionals(BigDecimal.valueOf(40));
        newGroupSpending.setDiscounts(BigDecimal.valueOf(23));
        newGroupSpending.setGlobalBill(BigDecimal.valueOf(99));
        newGroupSpending.setHasWaiterAdd(true);
        newGroupSpending.setPeopleList(personList);

        payment = new Payment();
        payment.setReferenceId("id");
        payment.setCallbackUrl("callback");
        payment.setReturnUrl("url");
        payment.setExpiresAt("120");
        payment.setId(1L);
    }

}
