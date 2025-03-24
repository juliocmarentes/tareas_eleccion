package Tarea3.src;
import org.apache.commons.math3.analysis.MultivariateFunction;
import java.lang.Math;
import java.util.ArrayList;

public class Likelihood_nested_logit {

    ArrayList<Ticket> listaTickets = new ArrayList<Ticket>(); 
    int J; // Número de alternativas
    int[] nidos; // Vector que indica a qué nido pertenece cada alternativa
    int R; // Número de nidos

    public Likelihood_nested_logit(ArrayList<Ticket> listaTickets, int[] nidos) {
        this.listaTickets = listaTickets;
        this.J = listaTickets.get(0).getPrices().length;
        this.nidos = nidos;
        this.R = calcularNumeroNidos(nidos);
    }

    private int calcularNumeroNidos(int[] nidos) {
        int max = 0;
        for (int nido : nidos) {
            if (nido > max) {
                max = nido;
            }
        }
        return max + 1; // Suponiendo que los índices empiezan en 0
    }

    public double[] calcula_p_logit(int indice_ticket, double[] params) {
        double[] p = new double[J];
        double[] alphas = new double[3];
        for (int i = 0; i < alphas.length; i++) {
            alphas[i] = params[i];
        }
        double[] betas = new double[2];
        for (int i = 0; i < betas.length; i++) {
            betas[i] = params[3 + i];
        }
        double[] gamma = new double[R];
        for (int r = 0; r < R; r++) {
            gamma[r] = Math.exp(params[5 + r]) / (1 + Math.exp(params[5 + r])); // Transformación sigmoide
        }

        Ticket ticket = listaTickets.get(indice_ticket);
        double[] aux = new double[J];
        double[] sum_nido = new double[R];
        
        for (int j = 0; j < J; j++) {
            double v = betas[0] * ticket.getPrices()[j] + betas[1] * ticket.getFeats()[j]
                    + ((j < (J - 1)) ? alphas[j] : 0);
            aux[j] = Math.exp(v / gamma[nidos[j]]);
            sum_nido[nidos[j]] += aux[j];
        }

        double sum_total = 0;
        for (int r = 0; r < R; r++) {
            sum_total += Math.pow(sum_nido[r], gamma[r]);
        }

        for (int j = 0; j < J; j++) {
            double P_nest = Math.pow(sum_nido[nidos[j]], gamma[nidos[j]]) / sum_total;
            p[j] = (aux[j] / sum_nido[nidos[j]]) * P_nest;
        }
        
        return p;
    }
    
    public double likelihood_logit(double[] params) {
        double likelihood = 0;
        int T = listaTickets.size();
        for (int t = 0; t < T; t++) {
            Ticket ticket = listaTickets.get(t);
            double[] p = calcula_p_logit(t, params);
            for (int j = 0; j < J; j++) {
                if (ticket.getBrands()[j] == 1) {
                    if (p[j] > 0) {
                        likelihood += Math.log(p[j]);
                    }
                    break;
                }
            }
        }
        return likelihood;
    }
}
