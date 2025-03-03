package Tarea2.src;
import java.util.Random;

public class GumbelDistribution {
    private final double mu;
    private final double beta;
    private final Random random;

    public GumbelDistribution(double mu, double beta, long seed) {
        this.mu = mu;
        this.beta = beta;
        this.random = new Random(seed);
    }

    public GumbelDistribution(double mu, double beta) {
        this.mu = mu;
        this.beta = beta;
        this.random = new Random();
    }

    public double sample() {
        double U = random.nextDouble(); // U ~ U(0,1)
        return  (mu - beta * Math.log(-Math.log(U)));
    }

  
}