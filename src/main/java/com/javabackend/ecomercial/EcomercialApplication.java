package com.javabackend.ecomercial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EcomercialApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomercialApplication.class, args);
	}

}
