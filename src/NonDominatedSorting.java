import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Cihanser Çalışkan
 */
public class NonDominatedSorting {

    public static ArrayList<NonDominatedPopulation> fastNDSort(Population p) {
        ArrayList<NonDominatedPopulation> fronts = new ArrayList<>();

        int rank = 1;
        while (p.getSize() > 0) {
            NonDominatedPopulation np = new NonDominatedPopulation();
            for (int i = 0; i < p.getSize(); i++) {
                np.add(p.getSolution(i));
            }
            for (int i = 0; i < np.getSize(); i++) {
                np.getSolution(i).setRank(rank);
                p.remove(np.getSolution(i));
            }
            fronts.add(np);
            rank++;
        }

        return fronts;
    }
    
    public static void crowdingDistance(NonDominatedPopulation f){
        for (int i = 0; i < f.getSize(); i++) {
            f.getSolution(i).setCrowdingDistance(0.0);
        }
        
        // For each objective
        for (int i = 1; i < 3; i++) {
            f.sort(new FitnessComperator((i)));
            double min = f.getSolution(0).getFitness(i);
            double max = f.getSolution(f.getSize()-1).getFitness(i);
            f.getSolution(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
            f.getSolution(f.getSize()-1).setCrowdingDistance(Double.POSITIVE_INFINITY);
            for (int j = 1; j < f.getSize()-1; j++) {
                double d = f.getSolution(j).getCrowdingDistance()
                        + (f.getSolution(j+1).getFitness(i) 
                        - f.getSolution(j-1).getFitness(i)) / (max - min);
                f.getSolution(j).setCrowdingDistance(d);
            }
        }
    }

}
