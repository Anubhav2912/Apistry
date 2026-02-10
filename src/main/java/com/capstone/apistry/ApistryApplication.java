package com.capstone.apistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration;

@SpringBootApplication(exclude = OAuth2ClientAutoConfiguration.class)
public class ApistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApistryApplication.class, args);
	}

}
