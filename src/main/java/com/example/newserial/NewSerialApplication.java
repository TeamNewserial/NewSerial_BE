package com.example.newserial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NewSerialApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewSerialApplication.class, args);
	}

}
