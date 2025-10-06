package com.farrnav3006.aad;

import java.io.File;
public class ListarDirectorio {
    public static void main(String[] args) {
        File carpeta = new File(".");
        File[] archivos = carpeta.listFiles();
        for (File archivo : archivos) {
            if (archivo.isDirectory()) {
                System.out.println("[DIR] " + archivo.getName());
            } else {
                System.out.println("[FILE] " + archivo.getName() + " (" + archivo.length() + " bytes)");
            }
        }
    }
}
