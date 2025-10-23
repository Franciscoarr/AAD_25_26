package com.farrnav3006.aad;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@Slf4j
public class Act_1_3 implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Act_1_3.class, args);
    }

    @Override
    public void run(String... args) {
        String csv = "alumnos.csv";
        String json = "alumnos.json";
        String xml = "alumnos.xml";
        //Files names

        try (Scanner scanner = new Scanner(System.in)) {
            int option;
            do {
                log.info("===== File Converter =====");
                log.info("1. Convert to JSON");
                log.info("2. Convert to XML");
                log.info("3. Exit");
                log.info("Select an option: ");

                option = scanInt(scanner);

                //Read CSV
                List<Alumnos> alumnnos = readCSV(csv);

                switch (option) {
                    case 1:
                        writeJSON(alumnnos, json);
                        log.info("JSON file generated successfully: " + json);
                        break;
                    case 2:
                        writeXML(alumnnos, xml);
                        log.info("XML file generated successfully: " + xml);
                        break;
                    case 3:
                        log.info("Exiting the program");
                        break;
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

    private List<Alumnos> readCSV(String path) throws IOException {
        List<Alumnos> students = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) { //Text reader for the path
            String line = br.readLine(); //Read and skip header line
            while ((line = br.readLine()) != null) { //Read until end of file
                String[] parts = line.split(","); //Divided into 3 parts by commas
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    double grade = Double.parseDouble(parts[2].trim());
                    students.add(new Alumnos(id, name, grade)); //Create and add Alumnos object
                } else {
                    log.warn("Line ignored due to incorrect format: " + line);
                }
            }
        }
        return students;
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

    //Convert CSV to JSON
    private void writeJSON(List<Alumnos> students, String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper(); //Jackson JSON processor
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), students);  //Write formatted JSON
    }

    //Convert CSV to XML
    private void writeXML(List<Alumnos> students, String path) throws IOException {
        XmlMapper xmlMapper = new XmlMapper(); //Jackson XML processor
        xmlMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), students); //Write formatted XML
    }
}
