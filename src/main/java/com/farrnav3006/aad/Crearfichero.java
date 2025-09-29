package com.farrnav3006.aad;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
@Slf4j

public class Crearfichero implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(AadApplication.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        File fichero = new File("ejemplo.txt");
        if (fichero.createNewFile()) {
            log.info("Fichero creado: " + fichero.getName());
        } else {
            log.info("El fichero ya existe."); //¿cómo se dónde está?
        }
    }
}

