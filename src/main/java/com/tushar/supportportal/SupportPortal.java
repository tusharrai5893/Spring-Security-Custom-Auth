package com.tushar.supportportal;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(exclude = {ServletWebServerFactoryAutoConfiguration.class}) // TODO this will remove auto configuration of web server
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Support Portal API", version = "1.0", description = "Documentation for Support Portal APIs"))
public class SupportPortal {
	public static void main(String[] args) {
		SpringApplication.run(SupportPortal.class, args);
	}
}