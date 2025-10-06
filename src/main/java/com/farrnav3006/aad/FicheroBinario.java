package com.farrnav3006.aad;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class FicheroBinario {
    public static void main(String[] args) {
        try (BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream("imagen.jpg"))) {

            byte[] buffer = new byte[1024];
            int bytesLeidos;
            int total = 0;

            while ((bytesLeidos = bis.read(buffer)) != -1) {
                total += bytesLeidos;
            }
            System.out.println("Imagen leída con éxito. Total bytes: " + total);
        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }

        try {
            BufferedImage img = ImageIO.read(new File("imagen.jpg"));
            // Escalar la imagen para que quepa en consola
            int newWidth = 100; // ancho en caracteres
            int newHeight = (img.getHeight() * newWidth) / img.getWidth();
            BufferedImage scaled = new BufferedImage(newWidth, newHeight,
                    BufferedImage.TYPE_INT_RGB);
            scaled.getGraphics().drawImage(img, 0, 0, newWidth, newHeight, null);
            // Gradiente de caracteres de más oscuro a más claro
            String gradient = "@#8&xo;:,. ";
            for (int y = 0; y < newHeight; y += 2) { // saltamos filas para corregir proporción
                for (int x = 0; x < newWidth; x++) {
                    Color c = new Color(scaled.getRGB(x, y));
                    int gris = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                    int index = (gris * (gradient.length() - 1)) / 255;
                    System.out.print(gradient.charAt(index));
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
        }

        try {
            BufferedImage img = ImageIO.read(new File("imagen.jpg"));
            // Escalar para que quepa en consola
            int newWidth = 80; // caracteres de ancho
            int newHeight = (img.getHeight() * newWidth) / img.getWidth();
            BufferedImage scaled = new BufferedImage(newWidth, newHeight,
                    BufferedImage.TYPE_INT_RGB);
            scaled.getGraphics().drawImage(img, 0, 0, newWidth, newHeight,
                    null);
            for (int y = 0; y < newHeight; y += 2) { // corregir proporción
                for (int x = 0; x < newWidth; x++) {
                    Color c = new Color(scaled.getRGB(x, y));
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    // ANSI escape code para color 24 bits
                    System.out.print("\u001B[38;2;" + r + ";" + g + ";" + b +
                            "m█");
                }
                System.out.println("\u001B[0m"); // reset color al final cada línea
            }
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen: " +
                    e.getMessage());
        }

        try {
            BufferedImage img = ImageIO.read(new File("imagen.jpg"));
            // Escalar la imagen para que quepa en consola
            int newWidth = 80; // caracteres de ancho
            int newHeight = (img.getHeight() * newWidth) / img.getWidth();
            BufferedImage scaled = new BufferedImage(newWidth, newHeight,
                    BufferedImage.TYPE_INT_RGB);
            scaled.getGraphics().drawImage(img, 0, 0, newWidth, newHeight, null);
            for (int y = 0; y < newHeight - 1; y += 2) { // procesamos de dos en dos
                for (int x = 0; x < newWidth; x++) {
                    // Color del pixel de arriba
                    Color top = new Color(scaled.getRGB(x, y));
                    // Color del pixel de abajo
                    Color bottom = new Color(scaled.getRGB(x, y + 1));
                    // ANSI: color de texto = top, fondo = bottom
                    System.out.print(
                            "\u001B[38;2;" + top.getRed() + ";" + top.getGreen() + ";" + top.getBlue() +
                                    "m" +
                                    "\u001B[48;2;" + bottom.getRed() + ";" + bottom.getGreen() + ";" +
                                    bottom.getBlue() + "m" +
                                    "▀" // bloque superior coloreado
                    );
                }
                System.out.print("\u001B[0m\n"); // reset al final de la línea
            }
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
        }

    }
}
