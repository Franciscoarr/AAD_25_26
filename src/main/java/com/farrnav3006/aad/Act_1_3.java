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

        try (Scanner scanner = new Scanner(System.in)) {
            int opcion;
            do {
                log.info("===== Conversor de Alumnos =====");
                log.info("1. Convertir a JSON");
                log.info("2. Convertir a XML");
                log.info("3. Salir");
                log.info("Seleccione una opción: ");

                opcion = scanInt(scanner);

                // Leer CSV
                List<Alumnos> alumnos = leerCSV(csv);

                switch (opcion) {
                    case 1:
                        escribirJSON(alumnos, json);
                        log.info("Fichero JSON generado correctamente: " + json);
                        break;
                    case 2:
                        escribirXML(alumnos, xml);
                        log.info("Fichero XML generado correctamente: " + xml);
                        break;
                    case 3:
                        log.info("Saliendo del programa");
                        break;
                    default:
                        log.warn("Opción no válida");
                }
            } while (opcion != 3);

        } catch (FileNotFoundException e) {
            log.error("No se encontró el fichero CSV: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado: " + e.getMessage(), e);
        }
    }

    private List<Alumnos> leerCSV(String ruta) throws IOException {
        List<Alumnos> alumnos = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(ruta))) {
            String linea = br.readLine(); // cabecera
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    int id = Integer.parseInt(partes[0].trim());
                    String nombre = partes[1].trim();
                    double nota = Double.parseDouble(partes[2].trim());
                    alumnos.add(new Alumnos(id, nombre, nota));
                } else {
                    log.warn("Línea ignorada por formato incorrecto: " + linea);
                }
            }
        }
        return alumnos;
    }

    private int scanInt(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                log.warn("Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private void escribirJSON(List<Alumnos> alumnos, String ruta) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(ruta), alumnos);
    }

    private void escribirXML(List<Alumnos> alumnos, String ruta) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.writerWithDefaultPrettyPrinter().writeValue(new File(ruta), alumnos);
    }
}
