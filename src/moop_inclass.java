import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 *
 * @author Cihanser Çalışkan
 */
public class moop_inclass {

    public static ProblemInstance instance;
    public static Random rnd;

    public static void testDominance() {
        Solution s1 = new Solution(0), s2 = new Solution(0);
        System.out.println(String.format(Locale.ROOT,"S1: %.2f; %.2f", 
                s1.getFitness(1), s1.getFitness(2)));
        System.out.println(String.format(Locale.ROOT,"S2: %.2f; %.2f", 
                s2.getFitness(1), s2.getFitness(2)));
        System.out.println(Dominance.compare(s1, s2));
    }

    public static void testNonDomPop() {
        Population p = new Population();
        p.addRandom(10);
        NonDominatedPopulation np = new NonDominatedPopulation();
        for (int i = 0; i < p.getSize(); i++) {
            np.add(p.getSolution(i));
        }
        p.printFitnesses();
        System.out.println("*********");
        np.printFitnesses();
    }

    public static void testFronts() {
        Population p = new Population();
        p.addRandom(100);
        p.printALLinfo();
        ArrayList<NonDominatedPopulation> fronts = NonDominatedSorting.fastNDSort(p);
        System.out.println("*********");
        for (int i = 0; i < fronts.size(); i++) {
            System.out.println("FRONT " + (i + 1));
            fronts.get(i).printALLinfo();
            System.out.println("");
        }
        System.out.println("+++++++++++++++++++");
        NonDominatedSorting.crowdingDistance(fronts.get(0));
        fronts.get(0).printALLinfo();
    }

    public static void main(String[] args) {
        instance = new ProblemInstance("./problem_instances_TSP/berlin52.tsp",
                "./time_matrices_TSP/TSP_Time_Matrix.txt");
        //testDominance();
        //testNonDomPop();
        //testFronts();
        NSGA_II nsga2 = new NSGA_II(100, 100, 0.7, 0.3);
        nsga2.run();
        Population pareto = nsga2.getParetoFront();
        pareto.writeToFile("NSGA-II_Results.txt");
        pareto.printALLinfo();
    }

}
