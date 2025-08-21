package com.heyyoung.solsol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SolsolApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolsolApplication.class, args);
	}

}
