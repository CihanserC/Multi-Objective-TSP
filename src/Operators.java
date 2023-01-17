import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author Cihanser Çalışkan
 */
public class Operators {

    private static Random rnd;
    public static ArrayList<Double> rw;
    public static double susCounter;
    public static double susInterval;
    public static ArrayList<Double> rank;

    public static ArrayList<ArrayList<ArrayList<Integer>>> map;
    public static int a, b;
    public static ArrayList<ArrayList<Integer>> cycles;

    // SELECTION
    public static int binaryNonDominatedTournamentSelection(Population population) {
        rnd = new Random();
        int contestant1 = rnd.nextInt(population.getSize());
        int contestant2;
        do {
            contestant2 = rnd.nextInt(population.getSize());
        } while (contestant1 == contestant2);
        int dominance = Dominance.compare(population.getSolution(contestant1), 
                population.getSolution(contestant2));
        switch (dominance) {
            case -1:
                return contestant1;
            case 1:
                return contestant2;
            default:
                return rnd.nextDouble() < 0.5 ? contestant1 : contestant2;
        }

    }

    // CROSSOVER
    public static int findCityInMapping(ArrayList<ArrayList<Integer>> map, int city) {
        int result = city;
        boolean found;
        do {
            found = false;
            for (int i = 0; i < map.size(); i++) {
                if (result == map.get(i).get(0)) {
                    result = map.get(i).get(1);
                    found = true;
                    break;
                }
            }
        } while (found);
        return result;
    }

    public static ArrayList<Solution> PMX(Solution p1, Solution p2) {
        int n = p1.tour.size();
        ArrayList<Solution> child = new ArrayList<>();
        map = new ArrayList<>();
        rnd = new Random();
        int a, b = rnd.nextInt(n);
        do {
            a = rnd.nextInt(n);
        } while (a == b);

        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }

        Operators.a = a;
        Operators.b = b;
        //int a = 1, b = 4;

        // Child Tours
        ArrayList<Integer> c1 = new ArrayList<Integer>(Collections.nCopies(n, -1));
        ArrayList<Integer> c2 = new ArrayList<Integer>(Collections.nCopies(n, -1));

        // Transferring the Mapping Part
        for (int i = a; i <= b; i++) {
            c1.set(i, p1.tour.get(i));
            c2.set(i, p2.tour.get(i));
        }

        // Construct Mapping
        ArrayList<ArrayList<Integer>> map1 = new ArrayList<>(b - a + 1);
        ArrayList<ArrayList<Integer>> map2 = new ArrayList<>(b - a + 1);
        for (int i = a; i <= b; i++) {
            ArrayList<Integer> pair = new ArrayList<>();
            pair.add(p1.tour.get(i));
            pair.add(p2.tour.get(i));
            map1.add((ArrayList<Integer>) pair.clone());
            Collections.swap(pair, 0, 1);
            map2.add((ArrayList<Integer>) pair.clone());
        }
        map.add(map1);
        map.add(map2);

        // Transferring the Mapping Part
        for (int i = a; i <= b; i++) {
            c1.set(i, p1.tour.get(i));
            c2.set(i, p2.tour.get(i));
        }

        // [0, a)
        for (int i = 0; i < a; i++) {
            int city = Operators.findCityInMapping(map1, p2.tour.get(i));
            c1.set(i, city);
            city = Operators.findCityInMapping(map2, p1.tour.get(i));
            c2.set(i, city);
        }

        // (b, n)
        for (int i = b + 1; i < n; i++) {
            int city = Operators.findCityInMapping(map1, p2.tour.get(i));
            c1.set(i, city);
            city = Operators.findCityInMapping(map2, p1.tour.get(i));
            c2.set(i, city);
        }

        child.add(new Solution(c1));
        child.add(new Solution(c2));

        return child;
    }

    public static ArrayList<ArrayList<Integer>> findCycles(ArrayList<Integer> p1, ArrayList<Integer> p2) {
        int n = p1.size();
        ArrayList<ArrayList<Integer>> cycles = new ArrayList<>(); // Indices NOT Cities!!!
        ArrayList<Integer> check = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            check.add(p1.get(i));
        }

        int index; // index on parent_1
        while (check.size() > 0) {
            ArrayList<Integer> cycle = new ArrayList<>();
            int beginCity = check.get(0);
            index = p1.indexOf(beginCity);
            cycle.add(index);
            check.remove(0);
            int endCity;
            do {
                endCity = p2.get(index);
                int removeIndex = check.indexOf(endCity);
                if (removeIndex == -1) {
                    break;
                }
                check.remove(removeIndex);
                index = p1.indexOf(endCity);
                cycle.add(index);
            } while (beginCity != endCity);
            cycles.add(cycle);
        }

        return cycles;
    }

    public static ArrayList<Solution> CX(Solution p1, Solution p2) {
        ArrayList<Solution> child = new ArrayList<>();

        ArrayList<ArrayList<Integer>> cycles = findCycles(p1.tour, p2.tour);

        Operators.cycles = cycles;

        ArrayList<Integer> c1 = (ArrayList<Integer>) p1.tour.clone();
        ArrayList<Integer> c2 = (ArrayList<Integer>) p2.tour.clone();

        for (int i = 1; i < cycles.size(); i += 2) {
            for (int j = 0; j < cycles.get(i).size(); j++) {
                int index = cycles.get(i).get(j);
                c1.set(index, p2.tour.get(index));
                c2.set(index, p1.tour.get(index));
            }
        }

        Solution child1 = new Solution(c1);
        Solution child2 = new Solution(c2);
        child.add(child1);
        child.add(child2);
        return child;
    }

    public static ArrayList<Solution> OX(Solution p1, Solution p2) {
        int n = p1.tour.size();
        ArrayList<Solution> child = new ArrayList<>();
        ArrayList<Integer> c1 = new ArrayList<Integer>(Collections.nCopies(n, -1));
        ArrayList<Integer> c2 = new ArrayList<Integer>(Collections.nCopies(n, -1));

        rnd = new Random();
        int a, b = rnd.nextInt(n);
        do {
            a = rnd.nextInt(n);
        } while (a == b);

        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }

        Operators.a = a;
        Operators.b = b;
        //int a = 1, b = 4;

        // [a,b]
        for (int i = a; i <= b; i++) {
            c1.set(i, p1.tour.get(i));
            c2.set(i, p2.tour.get(i));
        }

        // indices of (b,n) U [0, a)
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = b + 1; i < n; i++) {
            indices.add(i);
        }
        for (int i = 0; i < a; i++) {
            indices.add(i);
        }

        // indices of (b,n) U [0, a): Parent_2 -> Child_1
        for (int i = 0; i < indices.size(); i++) {
            int index = indices.get(i);
            int j = indices.get(i);
            int city = -1;
            do {
                city = p2.tour.get(j);
                j++;
                if (j == n) {
                    j = 0;
                }
            } while (c1.indexOf(city) != -1);
            c1.set(index, city);
        }

        // indices of (b,n) U [0, a): Parent_1 -> Child_2
        for (int i = 0; i < indices.size(); i++) {
            int index = indices.get(i);
            int j = indices.get(i);
            int city = -1;
            do {
                city = p1.tour.get(j);
                j++;
                if (j == n) {
                    j = 0;
                }
            } while (c2.indexOf(city) != -1);
            c2.set(index, city);
        }

        child.add(new Solution(c1));
        child.add(new Solution(c2));
        return child;
    }

    public static ArrayList<Solution> Crossover(Solution p1, Solution p2, int type) {
        ArrayList<Solution> child = null;
        switch (type) {
            case 1: // PMX
                child = PMX(p1, p2);
                break;
            case 2: // CX
                child = CX(p1, p2);
                break;
            case 3: // OX
                child = OX(p1, p2);
                break;
        }
        return child;
    }

    // MUTATION
    public static void Mutate(Solution s, int type) {
        int n = s.tour.size();
        int m = n / 10;
        int a, b;
        rnd = new Random();
        switch (type) {
            case 1: // Insert
                for (int i = 0; i < m; i++) {
                    a = rnd.nextInt(n);
                    b = rnd.nextInt(n);
                    s.removeAndInsert(a, b);
                    //System.out.println("a: " + a + " b: " + b);
                    //System.out.println(s.tour);
                }
                break;
            case 2: // Swap
                for (int i = 0; i < m; i++) {
                    a = rnd.nextInt(n);
                    b = rnd.nextInt(n);
                    s.swap(a, b);
                    //System.out.println("a: " + a + " b: " + b);
                    //System.out.println(s.tour);
                }
                break;
            case 3: // Invert
                a = rnd.nextInt(n);
                b = rnd.nextInt(n);
                if (a > b) {
                    int temp = a;
                    a = b;
                    b = temp;
                }
                s.invert(a, b);
                //System.out.println("a: " + a + " b: " + b);
                //System.out.println(s.tour);
                break;
            case 4: // Scramble
                a = rnd.nextInt(n);
                b = rnd.nextInt(n);
                if (a > b) {
                    int temp = a;
                    a = b;
                    b = temp;
                }
                int k = (b - a + 1) / 2;
                for (int i = 0; i < k; i++) {
                    int i1 = rnd.nextInt(k) + a;
                    int i2 = rnd.nextInt(k) + a;
                    s.swap(i1, i2);
                }
                //System.out.println("a: " + a + " b: " + b);
                //System.out.println(s.tour);
                break;
        }
        s.evaluate();
    }
}
