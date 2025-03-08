package Tarea2.src;

import java.util.List;

public class Main {
     public static void main(String[] args) {
        //String ruta = "C:/Users/julio/OneDrive/Documentos/Maestría/cuarto semestre/eleccion/tareas_eleccion/Tarea2/data/yogurt.csv"; // Ruta del archivo CSV
        String ruta = "C:/Users/Mi/Documents/Maestrias/Colmex/4to semestre/eleccion/tareas eleccion/Tarea2/data/"; // Ruta del archivo CSV escuela
        int M = 3211; // Número de iteraciones
        Orquestador orquestador = new Orquestador(ruta, M);
        orquestador.ejecutar();

    }
}
