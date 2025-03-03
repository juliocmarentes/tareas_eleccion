package Tarea2.src;

import java.util.List;

public class Main {
     public static void main(String[] args) {
        String ruta = "C:/Users/julio/OneDrive/Documentos/Maestría/cuarto semestre/eleccion/tareas_eleccion/Tarea2/data/yogurt.csv"; // Ruta del archivo CSV
        Orquestador orquestador = new Orquestador(ruta);
        orquestador.ejecutar();

    }
}
