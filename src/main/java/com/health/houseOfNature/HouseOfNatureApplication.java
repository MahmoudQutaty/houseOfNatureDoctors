package com.health.houseOfNature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class HouseOfNatureApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseOfNatureApplication.class, args);
	}

}
