public class OffByOne implements CharacterComparator{
    @Override
    public boolean equalChars(char x, char y) {
        int asciix = (int) x;
        int asciiy = (int) y;
        return Math.abs(asciix - asciiy) == 1;
    }
}
