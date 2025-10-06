package com.farrnav3006.aad;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class AadApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AadApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		log.info("---MENU---");
		log.info("1.- Insertar nuevos registros");
		log.info("2.- Consultar un alumno directamente por su posición en el fichero");
		log.info("3.- Modificar la nota de un alumno sin necesidad de reescribir todo el fichero");
	}
}

