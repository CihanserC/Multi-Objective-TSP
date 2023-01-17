/**
 *
 * @author Cihanser Çalışkan
 */
public class Dominance {
    
    public static int compare(Solution s1, Solution s2){
        
        if (s1.getFitness(1) < s2.getFitness(1) && s1.getFitness(2) < s2.getFitness(2))
            return -1;
        else if (s2.getFitness(1) < s1.getFitness(1) && s2.getFitness(2) < s1.getFitness(2))
            return 1;
        else
            return 0;
    }
    
}
