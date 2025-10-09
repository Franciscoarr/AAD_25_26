package com.farrnav3006.aad;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

@Slf4j
@SpringBootApplication
public class Act_1_2 implements CommandLineRunner {

    private static final String FileName = "students.dat";
    private static final int NameLength = 20;
    private static final int RecordSize = 4 + (2 * NameLength) + 8; //Id + name + grade
    //Each student record takes 52 bytes, of which 40 are for the name because each char takes 2 bytes, while a double takes 8 bytes

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(Act_1_2.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("=== STUDENT MANAGEMENT ===");

        boolean exit = false;
        while (!exit) {
            log.info("\n----- MENU -----");
            log.info("1. Insert new student");
            log.info("2. Consult student by position");
            log.info("3. Modify student's grade");
            log.info("4. Exit");
            log.info("Choose an option:");

            int option;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                log.warn("Please enter a valid number.");
                continue;
            }

            switch (option) {
                case 1:
                    insertStudent();
                    break;
                case 2:
                    consultStudent();
                    break;
                case 3:
                    modifyGrade();
                    break;
                case 4:
                    exit = true;
                    log.info("Exiting the program...");
                    System.exit(0); //To close the app
                    break;
                default:
                    log.warn("Invalid option, please try again");
                    break;
            }
        }
    }

    public static void insertStudent() {
        try (RandomAccessFile raf = new RandomAccessFile(FileName, "rw")) { //Can read and write
            raf.seek(raf.length()); //Pointer at the end of the file

            log.info("Enter the student's ID:");
            int id = Integer.parseInt(scanner.nextLine());

            log.info("Enter the name (max 20 characters):");
            String name = scanner.nextLine();

            double grade;
            while (true) {
                log.info("Enter the grade (greater than 0 and less or equal to 10):");
                try {
                    grade = Double.parseDouble(scanner.nextLine());
                    if (grade > 0 && grade <= 10) {
                        break;
                    } else {
                        log.warn("Grade must be greater than 0 and less or equal to 10. Try again");
                    }
                } catch (NumberFormatException e) {
                    log.warn("Invalid grade format. Try again");
                }
            }

            //Write to the record
            raf.writeInt(id);

            StringBuilder sb = new StringBuilder(name);
            sb.setLength(NameLength); //Pad or trim
            raf.writeChars(sb.toString()); //Each char takes 2 bytes
            raf.writeDouble(grade); //Takes 8 bytes

            log.info("Student inserted successfully");

        } catch (IOException e) {
            log.error("Error inserting student: " + e.getMessage());
        } catch (NumberFormatException e) {
            log.warn("Invalid input");
        }
    }

    public static void consultStudent() {
        log.info("Enter the position of the student (starting from 0):");
        int position;
        try {
            position = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            log.warn("Invalid number");
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(FileName, "r")) { //Read-only
            long filePosition = position * RecordSize; //Students start every 52 bytes: 0-52-104-...

            if (filePosition >= raf.length()) {
                log.warn("No student exists at that position");
                return;
            }

            raf.seek(filePosition); //Go to byte position

            int id = raf.readInt();

            char[] nameChars = new char[NameLength];
            for (int i = 0; i < NameLength; i++) {
                nameChars[i] = raf.readChar();
            }
            String name = new String(nameChars).trim();

            double grade = raf.readDouble();

            log.info(String.format("Student found: ID: %d | Name: %s | Grade: %.2f", id, name, grade));

        } catch (IOException e) {
            log.error("Error reading student: " + e.getMessage());
        }
    }

    public static void modifyGrade() {
        log.info("Enter the position of the student (starting from 0):");
        int position;
        try {
            position = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            log.warn("Invalid number");
            return;
        }

        log.info("Enter the new grade:");
        double newGrade;
        try {
            newGrade = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            log.warn("Invalid grade");
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(FileName, "rw")) { //Can read and write
            long filePosition = position * RecordSize + 4 + (2 * NameLength); //Skip id + name

            if (filePosition >= raf.length()) {
                log.warn("No student exists at that position");
                return;
            }

            raf.seek(filePosition);
            raf.writeDouble(newGrade);

            log.info("Grade successfully updated for student at position " + position);

        } catch (IOException e) {
            log.error("Error modifying grade: " + e.getMessage());
        }
    }
}

