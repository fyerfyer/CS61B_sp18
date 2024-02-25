package byog.Core;

public class Position {
    public final int x;
    public final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position midPos(Position other) {
        int midX = other.x - this.x;
        int midY = other.y - this.y;
        Position mid = new Position((int)(this.x + 0.5 * midX), ((int)(this.y + 0.5 * midY)));
        return mid;
    }
}
