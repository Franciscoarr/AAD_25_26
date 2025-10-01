package com.farrnav3006.aad;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

@Slf4j
@SpringBootApplication
public class Act_1_1 implements CommandLineRunner {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(AadApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Path directorio = null;

        while (directorio == null) {
            log.info("Introduce la ruta del directorio:");
            String ruta = scanner.nextLine();
            Path posibleDirectorio = Paths.get(ruta);

            if (!Files.exists(posibleDirectorio) || !Files.isDirectory(posibleDirectorio)) { //Comprueba si el directorio existe y que no sea otra cosa
                log.warn("El directorio no existe o no es válido, vuelve a introducirlo.");
            } else {
                directorio = posibleDirectorio;
            }
        }

        MostrarContenido(directorio);
        MostrarMenu(directorio);
    }

    public static void MostrarContenido(Path directorio) {
        log.info("Mostrando contenido de: " + directorio.toAbsolutePath());
        try {
            Files.list(directorio).forEach(path -> {
                File fichero = path.toFile();
                String nombre = fichero.getName();
                long bytes = fichero.length();
                String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(fichero.lastModified()));

                log.info(String.format("%-30s | %10d bytes | %s", nombre, bytes, fecha)); //Muestra el nombre, los numero de bytes y la última modificación
            });
        } catch (IOException e) {
            log.error("Error al mostrar el contenido: {}", e.getMessage(), e);
        }
    }

    public static void MostrarMenu(Path directorio) {
        boolean salir = false;
        while (!salir) {
            log.info("-----MENU-----");
            log.info("1. Crear un nuevo fichero vacío");
            log.info("2. Mover un fichero a otra ubicación");
            log.info("3. Borrar un fichero existente");
            log.info("4. Salir");
            log.info("Que opción quieres elegir");
            int opc = scanner.nextInt();
            scanner.nextLine(); //Sirve para depurar el escaner y que no de ningun error

            switch (opc) {
                case 1:
                    CrearFichero(directorio);
                    break;
                case 2:
                    MoverFichero();
                    break;
                case 3:
                    BorrarFichero();
                    break;
                case 4:
                    salir = true;
                    log.info("Saliendo del programa...");
                    break;
                default:
                    log.info("Introduzca una opción valida");
            }
        }
    }

    public static void CrearFichero(Path directorio) {
        log.info("Introduce el nombre del fichero:");
        String nombre = scanner.nextLine();
        Path nuevoFichero = directorio.resolve(nombre); //Junta la ruta del directorio con el nombre que hemos dado

        try {
            if (!Files.exists(nuevoFichero)) {
                Files.createFile(nuevoFichero);
                log.info("Fichero creado exitosamente.");
            } else {
                log.warn("El fichero ya existe.");
            }
        } catch (IOException e) {
            log.error("Error al crear el fichero: " + e.getMessage());
        }
    }

    public static void MoverFichero() {
        log.info("Introduce la ruta del fichero que quieres mover (incluyendo nombre del fichero):");
        String rutaorigen = scanner.nextLine();
        Path origen = Paths.get(rutaorigen);

        if (!Files.exists(origen)) {
            log.warn("El fichero no existe.");
            return;
        }

        log.info("Introduce la ruta de destino (incluyendo nombre del fichero):");
        String rutadestino = scanner.nextLine();
        Path destino = Paths.get(rutadestino);

        try {
            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
            log.info("Fichero movido exitosamente a " + destino.toAbsolutePath());
        } catch (IOException e) {
            log.error("Error al mover el fichero: " + e.getMessage());
        }
    }

    public static void BorrarFichero() {
        log.info("Introduce la ruta absoluta del fichero:");
        String ruta = scanner.nextLine();
        Path fichero = Paths.get(ruta);

        try {
            if (Files.exists(fichero)) {
                Files.delete(fichero); //Borra el fichero especificado
                log.info("Fichero borrado exitosamente.");
            } else {
                log.warn("El fichero no existe.");
            }
        } catch (IOException e) {
            log.error("Error al borrar el fichero: " + e.getMessage());
        }
    }
}
