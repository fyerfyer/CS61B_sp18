import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
        int maxLen = 0;
        for (String s : asciis) {
            if (s.length() > maxLen) maxLen = s.length();
        }

        String[] res = Arrays.copyOf(asciis, asciis.length);
        for(int d = 0; d < maxLen; d += 1) {
            sortHelperLSD(res, d);
        }

        return res;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        int R = 256;
        int[] count = new int[R + 1];
        for (String s : asciis) {
            count[s.charAt(index) + 1]++;
        }

        // turn input into index
        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }

        // partition
        String[] aux = new String[asciis.length];
        for (String s : asciis) {
            int position = count[s.charAt(index)];
            aux[position] = s;
            count[s.charAt(index)]++;
        }

        // rewrite
        for (int i = 0; i < asciis.length; i++) {
            asciis[i] = aux[i];
        }
    }


    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] arr = {"abc", "def", "bcd", "xyz", "ghi", "rst", "uvw", "mno"};
        String[] arr2 = {"apple", "banana", "orange", "grape", "melon", "kiwi", "pear", "peach"};
        arr = RadixSort.sort(arr);
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(arr2));
    }
}
