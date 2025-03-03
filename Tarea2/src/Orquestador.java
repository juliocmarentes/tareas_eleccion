package Tarea2.src;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BOBYQAOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunctionMappingAdapter;

public class Orquestador {
    private String ruta;
    private ArrayList<Ticket> listaTickets = new ArrayList<>();
    private double[] optimalParams;
    public Orquestador(String ruta) {
        this.ruta = ruta;
    }

    public void ejecutar(){
        leedatos();
        optimiza();
    }

    public void leedatos() {
        ArrayList<Ticket> listaTickets = LectorCSV.leerCSV(ruta);
        this.listaTickets = listaTickets;
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

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public List<Ticket> getListaTickets() {
        return listaTickets;
    }

    public void setListaTickets(ArrayList<Ticket> listaTickets) {
        this.listaTickets = listaTickets;
    }
}
