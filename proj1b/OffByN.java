public class OffByN implements CharacterComparator{
    private int N;

    public OffByN(int n) {
        N = n;
    }

    @Override
    public boolean equalChars(char x, char y) {
        int asciix = (int) x;
        int asciiy = (int) y;
        return Math.abs(asciix - asciiy) == N;
    }
}
