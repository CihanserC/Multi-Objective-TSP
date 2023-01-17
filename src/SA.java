import java.util.Locale;
import java.util.Random;

/**
 *
 * @author Cihanser Çalışkan
 */
public class SA {

    private double T, h, tMin;  // initial temperature, cooling ratio, frozen temperature
    private Random rnd = new Random();
    private NonDominatedPopulation archive;
    private double w1, w2;

    public SA(double T, double h, double tMin) {
        this.T = T;
        this.h = h;
        this.tMin = tMin;
    }

    private double normalize(double x, int fitness){
        double result, min_x = 0, max_x = 0;
        if (fitness == 1){
            min_x = 7500;
            max_x = 40000; 
        } else if (fitness == 2){
            min_x = 1;
            max_x = 1000;
        }
        
        result = (x - min_x) / (max_x - min_x);
        
        return result;
    }
    
    private double cost(Solution s) {
        return w1 * normalize(s.getFitness(1),1) + w2 * normalize(s.getFitness(2),2);
    }

    public void run() {
        archive = new NonDominatedPopulation();
        for (int i = 0; i < 11; i++) {
            double T_new = T;
            w1 = i / 10.0;
            w2 = 1 - w1;
            Solution current = new Solution(0); // 0 - Random
            Heuristics.twoOptOnTime(current);
            archive.add(current);
            Heuristics.twoOptOnTour(current);
            Solution best = new Solution(current);
            archive.add(best);

            do {
                Solution neighbour = new Solution(Heuristics.findNeighbour(current, 2, 1));
                Heuristics.twoOptOnTime(neighbour);
                archive.add(neighbour);
                Heuristics.twoOptOnTour(neighbour);
                archive.add(neighbour);

                if (cost(neighbour) < cost(current)) {
                    current = new Solution(neighbour);
                    if (cost(neighbour) < cost(best)) {
                        best = new Solution(neighbour);
                    }
                } else {
                    double probability = (double) (1
                            / (1 + (Math.pow(Math.E, (cost(current) - cost(neighbour) / T_new)))));
                    double chance = rnd.nextDouble();
                    if (chance < probability) {
                        current = new Solution(neighbour);
                    }
                }
                T_new *= h;
            } while (T_new > tMin);
        }
    }

    public NonDominatedPopulation getArchive() {
        return archive;
    }

}
