package com.farrnav3006.aad;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

@SpringBootApplication
@Slf4j
public class AadApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AadApplication.class, args);
	}

    private static String log_filename = "app.log";
    private static Charset charset = StandardCharsets.UTF_8; // Codificación por defecto

	@Override
	public void run(String... args) throws Exception {

        try (Scanner scanner = new Scanner(System.in)) {
            int option;
            do {
                log.info("===== Log Manager =====");
                log.info("1. Add events");
                log.info("2. Filter events");
                log.info("3. Change encoding");
                log.info("4. Exit");
                log.info("Select an option: ");

                option = scanInt(scanner);
                scanner.nextLine();

                switch (option) {
                    case 1:
                        AddEvents(scanner);
                        break;
                    case 2:

                        break;
                    case 3:
                        ChangeEncoding(scanner);
                        break;
                    case 4:

                    default:
                        log.warn("Invalid option");
                }
            } while (option != 4);
        } catch (Exception e) {
            log.error("Unexpected error: " + e.getMessage(), e);
        }
	}

    private static int scanInt(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                log.warn("Please enter a valid number");
                scanner.nextLine();//Clear invalid input from scanner buffer
            }
        }
    }

    private static void AddEvents(Scanner scanner) {
        log.info("Introduce el mensaje del evento: ");
        String mensaje = scanner.nextLine();
        String fecha = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

        String linea = "[" + fecha + "]" + mensaje;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(log_filename, charset))) {
            bw.write(linea);
            bw.newLine();
            log.info("Event added");
        } catch (IOException e) {
            log.error("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    private static void FilterEvents(Scanner scanner) {
        log.info("Introduce la fecha del evento (YYYY-MM-DD): ");
        String fecha = scanner.nextLine();


    }

    private static void ChangeEncoding(Scanner scanner) {
        int option2;
        do {
            log.info("===== Change encoding =====");
            log.info("Current encoding: " + charset.displayName());
            log.info("1. UTF-8");
            log.info("2. ISO-8859-1");
            log.info("3. Exit to main menu");
            log.info("Select an option: ");

            option2 = scanInt(scanner);

            switch (option2) {
                case 1:
                    charset = StandardCharsets.UTF_8;
                    log.info("Encoding changed to UTF-8");
                    break;
                case 2:
                    charset = StandardCharsets.ISO_8859_1;
                    log.info("Encoding changed to ISO-8859-1");
                    break;
                case 3:
                    log.info("Returning to previous menu...");
                    break;
                default:
                    log.warn("Invalid option, ");
            }
        }while (option2 != 3);
    }


}

