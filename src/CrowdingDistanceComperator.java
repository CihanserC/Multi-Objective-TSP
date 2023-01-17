import java.util.Comparator;

/**
 *
 * @author Cihanser Çalışkan
 */
public class CrowdingDistanceComperator implements Comparator{

    @Override
    public int compare(Object t1, Object t2) {
        Solution s1 = (Solution) t1;
        Solution s2 = (Solution) t2;
        return Double.compare(s2.getCrowdingDistance(),s1.getCrowdingDistance());
    }
    
}
