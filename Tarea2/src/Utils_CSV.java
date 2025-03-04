package Tarea2.src;

import java.io.*;
import java.util.*;

public class Utils_CSV {

    public static ArrayList<Ticket> leerCSV(String rutaArchivo) {
        ArrayList<Ticket> tickets = new ArrayList<>();
        String linea;
        
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            // Leer y descartar la primera línea (encabezados)
            br.readLine();
            
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(","); // Suponiendo que el separador es una coma
                         
                // Convertir valores y crear objeto Individuo
                int id_hogar = Integer.parseInt(valores[0]);
                double expend = Double.parseDouble(valores[1]);
                double income = Double.parseDouble(valores[2]);
                int quantity = Integer.parseInt(valores[4]);
                double [] prices = new double[4];
                int [] brands = new int[4];
                int [] feats = new int[4];
                for(int i = 0; i < 4; i++){
                    brands[i] = Integer.parseInt(valores[5+i]);
                    feats[i] = Integer.parseInt(valores[9+i]);
                    prices[i] = Double.parseDouble(valores[13+i]);
                    
                }
                tickets.add(new Ticket(id_hogar, income, expend, quantity, prices, brands, feats));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    public static void saveToCSV(double[][] data, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (double[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    writer.append(String.valueOf(row[i])); // Convertir double a String
                    if (i < row.length - 1) {
                        writer.append(","); // Separador CSV
                    }
                }
                writer.append("\n"); // Nueva línea para cada fila
            }
            System.out.println("Archivo CSV guardado en: " + fileName);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo CSV: " + e.getMessage());
        }
    }

   
}