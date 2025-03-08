package Tarea2.src;

import java.util.ArrayList;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunctionMappingAdapter;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BOBYQAOptimizer;
import org.apache.commons.math3.optim.univariate.SearchInterval;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optim.univariate.UnivariateObjectiveFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class Optimizador {

    private ArrayList<Ticket> listaTickets = new ArrayList<Ticket>();
    private Likelihood_logit logit;
    public Optimizador(ArrayList<Ticket> listaTickets) {
        this.listaTickets = listaTickets;
    }

    public double [] optimalParams(){
        Likelihood_logit logit = new Likelihood_logit(listaTickets);
        // Definir la función de log-verosimilitud negativa para minimizar
        MultivariateFunction function = params -> -logit.likelihood_logit(params);

        // Definir optimizador (BOBYQA)
        int dim = 5; // Tamaño correcto de parámetros
        int numInterpolationPoints = 2 * dim + 1; // Recomendación para BOBYQA
        MultivariateOptimizer optimizer = new BOBYQAOptimizer(numInterpolationPoints);

        // Definir límites (ajústalos si es necesario)
        double[] lowerBounds = new double[]{-100, -100, -100, -100, -100};
        double[] upperBounds = new double[]{100, 100, 100, 100, 100};

        // Optimización con límites
        PointValuePair result = optimizer.optimize(
            new org.apache.commons.math3.optim.MaxEval(10000),
            new org.apache.commons.math3.optim.InitialGuess(new double[]{0, 0, 0, 0, 0}),
            new org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction(function),
            GoalType.MINIMIZE,
            new org.apache.commons.math3.optim.SimpleBounds(lowerBounds, upperBounds) // <-- Agregar límites
        );

        // Retornar los parámetros óptimos
        return result.getPoint();
    }
   
}
