import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cihanser Çalışkan
 */
public class Population {

    private ArrayList<Solution> population;

    public Population() {
        population = new ArrayList<>();
    }

    public boolean add(Solution s) {
        return population.add(s);
    }
    
    public void add(Population p){
        population.addAll(p.getPopulation());
    }
    
    public void add(ArrayList<Solution> p){
        population.addAll(p);
    }
    
    public ArrayList<Solution> getPopulation(){
        return population;
    }

    public boolean remove(Solution s) {
        return population.remove(s);
    }

    public Solution removeByIndex(int index) {
        return population.remove(index);
    }

    public Solution getSolution(int index) {
        return population.get(index);
    }

    public void setSolution(int index, Solution s) {
        population.set(index, new Solution(s));
    }

    protected void addRandom(int size) {
        for (int i = 0; i < size; i++) {
            add(new Solution(0));
        }
    }

    public void clear() {
        population.clear();
    }

    public int getSize() {
        return population.size();
    }

    public void sort(Comparator<Solution> myComparator) {
        Collections.sort(population, myComparator);
    }

    public boolean contains(Solution sNew) {
        for (Solution s : population) {
            if (s.equals(sNew)) {
                return true;
            }
        }
        return false;
    }

    public void printALLinfo() {
        for (int i = 0; i < getSize(); i++) {
            System.out.println(String.format(Locale.ROOT, "S%d) %.2f ; %.2f; %d; %.2f",
                    (i + 1),
                    getSolution(i).getFitness(1),
                    getSolution(i).getFitness(2),
                    getSolution(i).getRank(),
                    getSolution(i).getCrowdingDistance()));
        }
    }

    public void printFitnesses() {
        for (int i = 0; i < getSize(); i++) {
            System.out.println(String.format(Locale.ROOT, "S%d) %.2f ; %.2f",
                    (i + 1), getSolution(i).getFitness(1), getSolution(i).getFitness(2)));
        }
    }

    public void writeToFile(String fileName) {
        try {
            FileWriter f = new FileWriter(new File(fileName));
            for (int i = 0; i < population.size(); i++) {
                f.write("Solution " + (i + 1) + "\n");
                // Writing the tour
                for (int j = 0; j < getSolution(i).tour.size(); j++) {
                    f.write(getSolution(i).tour.get(j) + "-");
                }
                f.write("" + getSolution(i).tour.get(0));
                f.write(String.format(Locale.ROOT, "; Distance = %.2f; Time = %.2f", 
                        getSolution(i).getFitness(1),
                        getSolution(i).getFitness(2)));                        ;
                f.write("\n\n");
            }
            f.write("Pareto Front: (Distance; Time)\n");
            for (int i = 0; i < getSize(); i++) {
                f.write(String.format(Locale.ROOT, "%.2f ; %.2f\n", 
                        getSolution(i).getFitness(1),
                        getSolution(i).getFitness(2)));
            }
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(Population.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
