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
        MultivariateFunction function = params -> -logit.likelihood_logit(params);

        // Definir optimizador (BOBYQA en este caso)
        int dim = 7; // Número de parámetros (3 alphas + 4 betas)
        int numInterpolationPoints = 2 * dim + 1; // Recomendación para BOBYQA
        MultivariateOptimizer optimizer = new BOBYQAOptimizer(numInterpolationPoints);

        // Optimización sin restricciones
        PointValuePair result = optimizer.optimize(
                new org.apache.commons.math3.optim.MaxEval(10000),
                new org.apache.commons.math3.optim.InitialGuess(new double[]{0, 0, 0, 0, 0, 0, 0}),
                new org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction(function),
                GoalType.MINIMIZE
        );

        // Retornar los parámetros óptimos
        return result.getPoint();
    }
   

    // Ejemplo de uso
}
