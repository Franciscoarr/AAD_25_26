package com.farrnav3006.aad;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
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

    private static String log_filename = "app.log"; //Log file name
    private static Charset charset = StandardCharsets.UTF_8; //Default file encoding

    @Override
    public void run(String... args) throws Exception {

        try (Scanner scanner = new Scanner(System.in)) {
            int option;
            do {
                // Main menu options
                log.info("===== Log Manager =====");
                log.info("1. Add events");
                log.info("2. Filter events");
                log.info("3. Change encoding");
                log.info("4. Exit");
                log.info("Select an option: ");

                option = scanInt(scanner);
                scanner.nextLine(); //Clear buffer

                switch (option) {
                    case 1:
                        AddEvents(scanner);
                        break;
                    case 2:
                        FilterEvents(scanner);
                        break;
                    case 3:
                        ChangeEncoding(scanner);
                        break;
                    case 4:
                        log.info("Exiting the program");
                        System.exit(0);
                        break;
                    default:
                        log.warn("Invalid option");
                }
            } while (option != 4);
        } catch (Exception e) {
            log.error("Unexpected error: " + e.getMessage(), e);
        }
    }

    // Safely read an integer input
    private static int scanInt(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                log.warn("Please enter a valid number");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    //Add a new event to log
    //Opens the log file for writing (append mode) using the selected encoding
    private static void AddEvents(Scanner scanner) {
        String message = "";

        // Repeat until message is not empty
        while (message.isBlank()) {
            log.info("Enter the event message: ");
            message = scanner.nextLine();

            if (message.isBlank()) {
                log.warn("Message cannot be empty!");
            }
        }

        //Current date and time
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        String line = "[" + date + "] " + message;

        //Write to log file
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(log_filename, true), charset))) {
            bw.write(line);
            bw.newLine();
            log.info("Event added successfully");
        } catch (IOException e) {
            log.error("Error writing log file: " + e.getMessage());
        }
    }

    // Filter events by date
    private static void FilterEvents(Scanner scanner) {
        log.info("Enter event date (YYYY/MM/DD): ");
        String date = scanner.nextLine();

        //Read file and search by date
        //Opens the log file for reading using the selected character encoding
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(log_filename), charset))) {
            String line;
            boolean found = false;
            log.info("--- Events on " + date + " ---");
            while ((line = reader.readLine()) != null) {
                if (line.contains("[" + date)) {
                    log.info(line);
                    found = true;
                }
            }

            //No results found
            if (!found) {
                log.warn("No events found for that date");
            }
        } catch (IOException e) {
            log.error("Error reading log file: " + e.getMessage());
        }
    }

    //Change log file encoding
    private static void ChangeEncoding(Scanner scanner) {
        int option2;
        do {
            //Encoding menu options
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
                    log.warn("Invalid option");
            }
        } while (option2 != 3);
    }
}

