package Tarea3.src;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BOBYQAOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunctionMappingAdapter;

public class Orquestador {
    private ArrayList<Ticket> listaTickets = new ArrayList<>();
    private double[] optimalParams;
    private int M;
    private int [] nidos; 
    private String ruta;
    private int n_nidos;
    private int dim;

    public Orquestador(String ruta, int M, int[] nidos, int n_nidos) {;
        this.M = M;
        this.ruta = ruta;
        this.nidos = nidos;
        this.n_nidos = n_nidos;
        this.dim = 5 + n_nidos;
    }

    public void ejecutar(){
        leedatos();
        //optimiza();
        //prueba();
        double [][] optimalParams = optimiza_general_faster();
        Utils_CSV.saveToCSV(optimalParams, ruta+"/resultado.csv");

    }

    public double [] getParams_mean(double [][] optimalParams){
        int B = optimalParams[0].length;
        double [] optimalParams_mean = new double[B];
        for (int i = 0; i < B; i++){
            double sum = 0;
            for (int j = 0; j < M; j++){
                sum += optimalParams[j][i];
            }
            optimalParams_mean[i] = sum/M;
        }
        return optimalParams_mean;
    }


    public void leedatos() {
        ArrayList<Ticket> listaTickets = Utils_CSV.leerCSV(ruta+"/yogurt.csv");
        this.listaTickets = listaTickets;
        System.out.println("Datos leídos");
        System.out.println(listaTickets.size() + " tickets leídos");
    }

    public void optimiza(){
        
        Optimizador optimizador = new Optimizador(listaTickets, nidos, n_nidos);
        double[] optimalParams = optimizador.optimalParams();
        // Resultado óptimo
        this.optimalParams = optimalParams;
        System.out.println("Optimal parameters: ");
        for (int i = 0; i < optimalParams.length; i++) {
            System.out.println("Param " + i + ": " + optimalParams[i]);
        }
    }

    public double [][]  optimiza_general(){
        
        double [][] optimalParams = new double[M][dim];
        int N = listaTickets.size();
        for (int i = 0; i < M; i++){
            ArrayList<Ticket> listaTickets = new ArrayList<>();
            int [] indices = Herramientas.generateRandomArray(N);
            for (int j = 0; j < N; j++){
                listaTickets.add(this.listaTickets.get(indices[j]));
            }
            Optimizador optimizador = new Optimizador(listaTickets, nidos, n_nidos);
            double[] optimalParams_i = optimizador.optimalParams();
            optimalParams[i] = optimalParams_i;
        }
        return optimalParams;
    }

    public double[][] optimiza_general_faster() {
        double[][] optimalParams = new double[M][dim];
        int N = listaTickets.size();
        int numThreads = Runtime.getRuntime().availableProcessors();

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<double[]>> futures = new ArrayList<>();

        for (int i = 0; i < M; i++) {
            final int index = i;
            futures.add(executor.submit(() -> {
                ArrayList<Ticket> shuffledTickets = new ArrayList<>();
                int[] indices = Herramientas.generateRandomArray(N);
                for (int j = 0; j < N; j++) {
                    shuffledTickets.add(listaTickets.get(indices[j]));
                }
                Optimizador optimizador = new Optimizador(shuffledTickets, nidos, n_nidos);
                return optimizador.optimalParams();
            }));
        }

        // Recoger los resultados
        for (int i = 0; i < M; i++) {
            try {
                optimalParams[i] = futures.get(i).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return optimalParams;
    }

    public double[] getOptimalParams() {
        return optimalParams;
    }

    public void setOptimalParams(double[] optimalParams) {
        this.optimalParams = optimalParams;
    }

    public List<Ticket> getListaTickets() {
        return listaTickets;
    }

    public void setListaTickets(ArrayList<Ticket> listaTickets) {
        this.listaTickets = listaTickets;
    }
}
