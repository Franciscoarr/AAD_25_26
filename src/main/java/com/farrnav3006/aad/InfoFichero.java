package com.farrnav3006.aad;

import java.io.File;
public class InfoFichero {
    public static void main(String[] args) {
        File f = new File("ejemplo.txt");
        if (f.exists()) {
            System.out.println("El fichero existe.");
            if (f.isFile()) {
                System.out.println("Es un fichero.");
                System.out.println("Tamaño: " + f.length() + " bytes");
            } else if (f.isDirectory()) {
                System.out.println("Es un directorio.");
            }
        } else {
            System.out.println("El fichero no existe.");
        }
    }
}
