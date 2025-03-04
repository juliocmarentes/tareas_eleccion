package Tarea2.src;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BOBYQAOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunctionMappingAdapter;

public class Orquestador {
    private ArrayList<Ticket> listaTickets = new ArrayList<>();
    private double[] optimalParams;
    private int M;
    private String ruta;

    public Orquestador(String ruta, int M) {;
        this.M = M;
        this.ruta = ruta;
    }

    public void ejecutar(){
        leedatos();
        //optimiza();
        //prueba();
        double [][] optimalParams = optimiza_general();
        Utils_CSV.saveToCSV(optimalParams, ruta+"/resultado.csv");
        
    }

    public void leedatos() {
        ArrayList<Ticket> listaTickets = Utils_CSV.leerCSV(ruta+"/yogurt.csv");
        this.listaTickets = listaTickets;
        System.out.println("Datos leídos");
        System.out.println(listaTickets.size() + " tickets leídos");
    }

    public void prueba(){
        Likelihood_logit likelihood = new Likelihood_logit(listaTickets);
        double [] params = new double[5];
        params[0] = 0.1;
        params[1] = 0.2;
        params[2] = 0.3;
        params[3] = 0.4;
        params[4] = 0.5;
        System.out.println(likelihood.likelihood_logit(params));
    }

    public void optimiza(){
        
        Optimizador optimizador = new Optimizador(listaTickets);
        double[] optimalParams = optimizador.optimalParams();
        // Resultado óptimo
        this.optimalParams = optimalParams;
        System.out.println("Optimal parameters: ");
        for (int i = 0; i < optimalParams.length; i++) {
            System.out.println("Param " + i + ": " + optimalParams[i]);
        }
    }

    public double [][]  optimiza_general(){
        
        double [][] optimalParams = new double[M][5];
        int N = listaTickets.size();
        for (int i = 0; i < M; i++){
            ArrayList<Ticket> listaTickets = new ArrayList<>();
            int [] indices = Herramientas.generateRandomArray(N);
            for (int j = 0; j < N; j++){
                listaTickets.add(this.listaTickets.get(indices[j]));
            }
            Optimizador optimizador = new Optimizador(listaTickets);
            double[] optimalParams_i = optimizador.optimalParams();
            optimalParams[i] = optimalParams_i;
        }
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
