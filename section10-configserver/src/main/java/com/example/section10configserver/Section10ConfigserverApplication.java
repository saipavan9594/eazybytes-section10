package com.example.section10configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class Section10ConfigserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(Section10ConfigserverApplication.class, args);
	}

}
