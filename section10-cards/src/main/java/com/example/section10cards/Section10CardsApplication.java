package com.example.section10cards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@RefreshScope
public class Section10CardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(Section10CardsApplication.class, args);
	}

}
