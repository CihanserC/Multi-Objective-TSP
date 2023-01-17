import java.util.Comparator;

/**
 *
 * @author Cihanser Çalışkan
 */
public class FitnessComperator implements Comparator {
    
    private int fitnessID;
    
    public FitnessComperator(int fitnessID){
        this.fitnessID = fitnessID;
    }

    @Override
    public int compare(Object t1, Object t2) {
        Solution s1 = (Solution) t1;
        Solution s2 = (Solution) t2;
        return Double.compare(s1.getFitness(fitnessID), s2.getFitness(fitnessID));
        
    }
    
}
