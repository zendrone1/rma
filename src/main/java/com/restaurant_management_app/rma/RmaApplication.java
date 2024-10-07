package com.restaurant_management_app.rma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.restaurant_management_app.rma")
public class RmaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RmaApplication.class, args);
	}

}
