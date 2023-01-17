import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class NSGA_II {

    private int popsize, maxIteration;
    private double cr, mr;
    private Population p;
    private Random rnd;

    public NSGA_II(int popsize, int maxIteration, double cr, double mr) {
        this.popsize = popsize;
        this.maxIteration = maxIteration;
        this.cr = cr;
        this.mr = mr;
    }

    public void run() {
        rnd = new Random();
        p = new Population();
        p.addRandom(popsize);

        for (int i = 0; i < maxIteration; i++) {

            Population q = new Population(); // Offspring population

            for (int j = 0; j < popsize; j = j + 2) {
                int p1Index = 0, p2Index = 0;
                p1Index = Operators.binaryNonDominatedTournamentSelection(p);
                p2Index = Operators.binaryNonDominatedTournamentSelection(p);

                ArrayList<Solution> temp = new ArrayList<>();

                boolean isApplied = false;

                // Crossover
                double r = rnd.nextDouble();
                if (r <= cr) {
                    temp = Operators.Crossover(p.getSolution(p1Index),
                            p.getSolution(p2Index), 1);
                    isApplied = true;
                } else {
                    temp.add(p.getSolution(p1Index));
                    temp.add(p.getSolution(p2Index));
                }

                // Mutation
                r = rnd.nextDouble();
                if (r <= mr) {
                    for (int k = 0; k < temp.size(); k++) {
                        Operators.Mutate(temp.get(k), 2);
                    }
                    isApplied = true;
                }

                if (isApplied) {
                    q.add(temp);
                }
            }
            // P U Q
            p.add(q);

            // Fast Non-dominated sorting
            // We transfer the first fronts w/o exceeding the popsize
            ArrayList<NonDominatedPopulation> fronts = NonDominatedSorting.fastNDSort(p);

            Population pNext = new Population();
            int f = 0;
            while (pNext.getSize() + fronts.get(f).getSize() <= popsize) {
                pNext.add(fronts.get(f).getPopulation());
                f++;
            }

            if (pNext.getSize() == popsize) {
                p = pNext;
                continue;
            }

            // Crowding distance
            NonDominatedSorting.crowdingDistance(fronts.get(f));
            int s = 0;
            while(pNext.getSize() < popsize){
                pNext.add(fronts.get(f).getSolution(s));
                s++;
            }
            
            // OR, alternatively
            //pNext.add(new ArrayList<Solution> (fronts.get(f).getPopulation().subList(0,
            //        popsize - pNext.getSize())));
                   
            p = pNext;

        }
    }

    public Population getParetoFront() {
        Population pf = new Population();
        for (int i = 0; i < p.getSize(); i++) {
            if (p.getSolution(i).getRank() == 1) {
                pf.add(p.getSolution(i));
            }
        }
        return pf;
    }

}
