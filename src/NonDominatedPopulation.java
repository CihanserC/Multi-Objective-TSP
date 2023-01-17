/**
 *
 * @author Cihanser Çalışkan
 */
public class NonDominatedPopulation extends Population {

    @Override
    public boolean add(Solution s) {

        if (super.getSize() == 0) {
            super.add(s);
            return true;
        }

        for (int i = 0; i < super.getSize(); i++) {
            int d = Dominance.compare(s, super.getSolution(i));
            if (d == -1) { // s dominates by solution i
                super.removeByIndex(i);
                i--;
            } else if (d == 1) { // s is dominated solution i
                return false;
            }
        }
        super.add(s);
        return true;
    }

}
