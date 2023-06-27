package com.devforce.devForce;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.Column;

@Slf4j
@SpringBootApplication
public class DevForce2023Application {

	public static void main(String[] args) {
		SpringApplication.run(DevForce2023Application.class, args);
	}

	@Bean
	public CommandLineRunner initData() {
		System.out.println(" --------- Hola crack, llegaste! --------- ");
		return (args) -> {
		};
	}
}
