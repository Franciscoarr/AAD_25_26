package com.farrnav3006.aad;

import java.io.File;
import java.io.IOException;
public class CrearFicherosYCarpetas {
    public static void main(String[] args) throws IOException {
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            carpeta.mkdir();
            System.out.println("Carpeta creada.");
        }
        File fichero = new File(carpeta, "alumnos.txt");
        if (fichero.createNewFile()) {
            System.out.println("Fichero creado en: " + fichero.getAbsolutePath());
        }
    }
}
