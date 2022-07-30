package com.payment.bill.v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class PaymentBillApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentBillApplication.class, args);
	}

}
