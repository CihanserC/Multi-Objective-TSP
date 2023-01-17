import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Cihanser Çalışkan
 */
public class Heuristics {

    static Random rnd = new Random();
    static int tMax = 10000;

    public static Solution findNeighbour(Solution current, int type, int fitness) {
        Solution neighbour = new Solution(current);
        Solution bestNeighbour = new Solution(current);
        int a, b;
        switch (type) {
            case 1: // Random Insert
                a = rnd.nextInt(ProblemInstance.getNbOfCities());
                b = rnd.nextInt(ProblemInstance.getNbOfCities());
                neighbour.removeAndInsert(a, b);
                neighbour.evaluate();
                return neighbour;
            case 2: // Random Swap
                a = rnd.nextInt(ProblemInstance.getNbOfCities());
                b = rnd.nextInt(ProblemInstance.getNbOfCities());
                neighbour.swap(a, b);
                neighbour.evaluate();
                return neighbour;
            case 3: // i --> i + 2 Insert
                for (int i = 0; i < ProblemInstance.getNbOfCities() - 2; i++) {
                    neighbour = new Solution(current);
                    neighbour.removeAndInsert(i, i + 2);
                    neighbour.evaluate();
                    if (neighbour.getFitness(fitness) < bestNeighbour.getFitness(fitness)) {
                        bestNeighbour = new Solution(neighbour);
                    }
                }
                return bestNeighbour;
            case 4: // i --> i + 1 Swap
                for (int i = 0; i < ProblemInstance.getNbOfCities() - 1; i++) {
                    neighbour = new Solution(current);
                    neighbour.swap(i, i + 1);
                    neighbour.evaluate();
                    if (neighbour.getFitness(fitness) < bestNeighbour.getFitness(fitness)) {
                        bestNeighbour = new Solution(neighbour);
                    }
                }
                return bestNeighbour;
        }
        return current;
    }

    public static void twoOptOnTour(Solution s) {
        boolean modified = true;
        while (modified) {
            modified = false;
            for (int i = 0; i < s.tour.size() - 2; i++) {
                for (int j = i + 2; j < s.tour.size() - 1; j++) {
                    double d1 = ProblemInstance.getDistance(s.tour.get(i), s.tour.get(i + 1))
                            + ProblemInstance.getDistance(s.tour.get(j), s.tour.get(j + 1));
                    double d2 = ProblemInstance.getDistance(s.tour.get(i), s.tour.get(j))
                            + ProblemInstance.getDistance(s.tour.get(i + 1), s.tour.get(j + 1));

                    // if distance can be shortened, adjust the tour
                    if (d2 < d1) {
                        // reverse
                        int begin = i + 1, end = j;
                        while (begin < end) {
                            Collections.swap(s.tour, begin, end);
                            begin++;
                            end--;
                        }
                        modified = true;
                    } 
                }
            }
        }
        s.evaluate();
    }

    public static void twoOptOnTime(Solution s) {
        boolean modified = true;
        while (modified) {
            modified = false;
            for (int i = 0; i < s.tour.size() - 2; i++) {
                for (int j = i + 2; j < s.tour.size() - 1; j++) {
                    double d1 = ProblemInstance.getTime(s.tour.get(i), s.tour.get(i + 1))
                            + ProblemInstance.getTime(s.tour.get(j), s.tour.get(j + 1));
                    double d2 = ProblemInstance.getTime(s.tour.get(i), s.tour.get(j))
                            + ProblemInstance.getTime(s.tour.get(i + 1), s.tour.get(j + 1));

                    // if time can be shortened, adjust the tour
                    if (d2 < d1) {
                        // reverse
                        int begin = i + 1, end = j;
                        while (begin < end) {
                            Collections.swap(s.tour, begin, end);
                            begin++;
                            end--;
                        }
                        modified = true;
                    }
                }
            }
        }
        s.evaluate();
    }

}
