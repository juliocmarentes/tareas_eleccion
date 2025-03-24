package Tarea3.src;

import java.util.List;

public class Main {
     public static void main(String[] args) {
        String ruta = "C:/Users/julio/OneDrive/Documentos/Maestría/cuarto semestre/eleccion/tareas_eleccion/Tarea3/data/"; // Ruta del archivo CSV
        //String ruta = "C:/Users/Mi/Documents/Maestrias/Colmex/4to semestre/eleccion/tareas eleccion/Tarea3/data/"; // Ruta del archivo CSV escuela
        int M = 3211; // Número de iteraciones
        int [] nidos = {0,0,1,1};
        int n_nidos = 2;
        Orquestador orquestador = new Orquestador(ruta, M,nidos,n_nidos);
        orquestador.ejecutar();

    }
}
