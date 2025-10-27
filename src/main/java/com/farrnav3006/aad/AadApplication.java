package com.farrnav3006.aad;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

@SpringBootApplication
@Slf4j
public class AadApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AadApplication.class, args);
	}
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

                switch (option) {
                    case 1:
                        AddEvents();
                        break;
                    case 2:

                        break;
                    case 3:
                        break;
                    case 4:

                    default:
                        log.warn("Invalid option");
                }
            } while (option != 3);

        } catch (FileNotFoundException e) {
            log.error("CSV file not found: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: " + e.getMessage(), e);
        }
	}

    private int scanInt(Scanner scanner) {
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


    }


}

