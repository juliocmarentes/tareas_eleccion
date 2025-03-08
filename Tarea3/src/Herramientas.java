package Tarea2.src;
import java.util.Random;


public class Herramientas {
    
    public static int[] generateRandomArray(int n) {
        int[] randomArray = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            randomArray[i] = random.nextInt(n);
        }
        return randomArray;
    }

    

}
