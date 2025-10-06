package com.farrnav3006.aad;

import java.io.*;
public class Recodificacion {
    public static void main(String[] args) {
        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(new FileInputStream("entrada.txt"), "UTF-8"));
                BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream("salida.txt"), "ISO-8859-1"))
        ) {
            String linea;
            while ((linea = br.readLine()) != null) {
                bw.write(linea);
                bw.newLine();
            }
            System.out.println("Recodificación realizada correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
