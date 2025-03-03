package Tarea2.src;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.lang.Math;
import java.util.ArrayList;

public class Likelihood_logit  {

    ArrayList<Ticket> listaTickets = new ArrayList<Ticket>();
    int J;

    public Likelihood_logit(ArrayList<Ticket> listaTickets){
        this.listaTickets = listaTickets;
        this.J = listaTickets.get(0).getPrices().length;

    }

    public double [] calcula_p_logit (int indice_ticket, double [] params){
        double [] p = new double[J];
        double [] alphas  = new double[3];
        for (int i = 0; i < 3; i++){
            alphas[i] = params[i];
        }
        double [] betas = new double[4];
        for (int i = 0; i < 4; i++){
            betas[i] = params[3+i];
        }
        Ticket ticket = listaTickets.get(indice_ticket);
        double aux [] = new double[J];
        double sum = 0;
        for (int j = 0; j < J; j++){
            double v = betas[j] * ticket.getPrices()[j] + ((j == (J-1)) ? alphas[j] :  0);
            aux[j] = Math.exp(v); 
            sum += aux[j];
        }
        for (int j = 0; j < J; j++){
            p[j] = aux[j] / sum;
        }
        return p;

    }
    
    public double likelihood_logit(double [] params){
        double likelihood = 0;
        int T = listaTickets.size();
        for(int t = 0; t < T; t++){
            Ticket ticket = listaTickets.get(t);
            double [] p = calcula_p_logit(t,params);
            for(int j = 0; j < J; j++){
                if(ticket.getBrands()[j] == 1){
                    if(p[j] > 0){
                        likelihood += ticket.getQuantity() * Math.log(p[j]);
                    }
                    break;
                }
            }
        }
        return likelihood;
    }

    
}
