package com.example.section10loans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@RefreshScope
public class Section10LoansApplication {

	public static void main(String[] args) {
		SpringApplication.run(Section10LoansApplication.class, args);
	}

}
