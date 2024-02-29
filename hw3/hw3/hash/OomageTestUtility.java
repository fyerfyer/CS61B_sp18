package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* TODO:
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int N =oomages.size();
        for (Oomage item : oomages) {
            int bucketNum = (item.hashCode() & 0x7FFFFFFF) % M;
            if (bucketNum * 50 < N || bucketNum * 5 > N * 2) {
                return false;
            }
        }

        return true;
    }
}
