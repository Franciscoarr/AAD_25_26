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

    private static final String NombreFichero = "alumnos.dat";
    private static final int NombreLong = 20;
    private static final int Contenido = 4 + (2 * NombreLong) + 8; //Id + nombre + nota
    //Cada alumno ocupa 52 bytes, de los cuales 40 son del nombre porque cada char ocupa 2 bytes, mientras que un double ocupa 8 bytes

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(Act_1_2.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("=== GESTIÓN DE ALUMNOS ===");

        boolean salir = false;
        while (!salir) {
            log.info("\n----- MENÚ -----");
            log.info("1. Insertar nuevo alumno (acceso secuencial)");
            log.info("2. Consultar alumno por posición (acceso aleatorio)");
            log.info("3. Modificar nota de un alumno");
            log.info("4. Salir");
            log.info("Elige una opción:");

            int opcion;
            try {
                opcion = scanner.nextInt();
            } catch (NumberFormatException e) {
                log.warn("Por favor, introduce un número válido.");
                continue;
            }

            switch (opcion) {
                case 1:
                    insertarAlumno();
                    break;
                case 2:
                    consultarAlumno();
                    break;
                case 3:
                    modificarNota();
                    break;
                case 4:
                    salir = true;
                    log.info("Saliendo del programa...");
                    break;
                default:
                    log.warn("Opción no válida, intentalo de nuevo");
                    break;
            }
        }
    }

    public static void insertarAlumno() {
        try (RandomAccessFile raf = new RandomAccessFile(NombreFichero, "rw")) { //Puede leerlo y escribirlo
            raf.seek(raf.length()); //Puntero en el fin del fichero

            log.info("Introduce el ID del alumno:");
            int id = scanner.nextInt();

            log.info("Introduce el nombre (máx 20 caracteres):");
            String nombre = scanner.nextLine();

            log.info("Introduce la nota:");
            double nota = scanner.nextDouble();

            //Escribe en el registro
            raf.writeInt(id);

            StringBuilder sb = new StringBuilder(nombre);
            sb.setLength(NombreLong); //Rellena o recorta
            raf.writeChars(sb.toString()); //Ocupa 2 bytes por cada letra
            raf.writeDouble(nota); //Ocupa 8 bytes

            log.info("Alumno insertado correctamente");

        } catch (IOException e) {
            log.error("Error al insertar alumno: " + e.getMessage());
        } catch (NumberFormatException e) {
            log.warn("Entrada no válida");
        }
    }

    public static void consultarAlumno() {
        log.info("Introduce la posición del alumno (empezando desde 0):");
        int posicion;
        try {
            posicion = scanner.nextInt();
        } catch (NumberFormatException e) {
            log.warn("Número inválido");
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(NombreFichero, "r")) { //Solo lo puede leer
            long posicionArchivo = posicion * Contenido; //Los alumnos empiezan cada 52 bytes 0-52-104-...

            if (posicionArchivo >= raf.length()) {
                log.warn("No existe ningún alumno en esa posición");
                return;
            }

            raf.seek(posicionArchivo); //Va a la posicion del byte

            int id = raf.readInt();

            char[] nombreChars = new char[NombreLong];
            for (int i = 0; i < NombreLong; i++) {
                nombreChars[i] = raf.readChar();
            }
            String nombre = new String(nombreChars).trim();

            double nota = raf.readDouble();

            log.info(String.format("Alumno encontrado: ID: %d | Nombre: %s | Nota: %.2f", id, nombre, nota));

        } catch (IOException e) {
            log.error("Error al leer alumno: " + e.getMessage());
        }
    }

    public static void modificarNota() {
        log.info("Introduce la posición del alumno:");
        int pos;
        try {
            pos = scanner.nextInt();
        } catch (NumberFormatException e) {
            log.warn("Número inválido");
            return;
        }

        log.info("Introduce la nueva nota:");
        double nuevaNota;
        try {
            nuevaNota = scanner.nextDouble();
        } catch (NumberFormatException e) {
            log.warn("Nota inválida");
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(NombreFichero, "rw")) { //Puede leerlo y escribrlo
            long posicionArchivo = pos * Contenido + 4 + (2 * NombreLong); //Salta id + nombre

            if (posicionArchivo >= raf.length()) {
                log.warn("No existe ningún alumno en esa posición");
                return;
            }

            raf.seek(posicionArchivo);
            raf.writeDouble(nuevaNota);

            log.info("Nota modificada correctamente para el alumno en posición" + pos);

        } catch (IOException e) {
            log.error("Error al modificar nota: " + e.getMessage());
        }
    }
}
