package com.farrnav3006.aad;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AadApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AadApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Ejecutando lógica dentro del método run() del microservicio ADD");

	}
}
