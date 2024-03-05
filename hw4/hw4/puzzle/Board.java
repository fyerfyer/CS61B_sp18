package hw4.puzzle;
import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState{
    private int[][] arr;
    private int N;
    private final int BLANK = 0;

    public Board(int[][] tiles) {
        N = tiles.length;
        arr = new int[N][N];
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                arr[i][j] = tiles[i][j];
            }
        }
    }

    public int tileAt(int i, int j) {
        if (i < 0 || i >= N || j < 0 || j >= N) {
            throw new IndexOutOfBoundsException();
        }
        return arr[i][j];
    }

    public int size() {
        return N;
    }

    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    private int xyToD(int x, int y) {
        return x * N + y + 1;
    }

    private int posX(int num) {
        return (int) (num - 1) / N;
    }

    private int posY(int num) {
        return (int) (num - 1) % N;
    }

    private int disCal(int x, int y, int xx, int yy) {
        return Math.abs(x - xx) + Math.abs(y - yy);
    }

    public int hamming() {
        int tot = 0;
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if (arr[i][j] == BLANK) continue;
                if (arr[i][j] != xyToD(i, j)) tot += 1;
            }
        }

        return tot;
    }

    public int manhattan() {
        int tot = 0;
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if (arr[i][j] == BLANK) continue;
                if (arr[i][j] != xyToD(i, j)) {
                    int x = posX(arr[i][j]);
                    int y = posY(arr[i][j]);
                    tot += disCal(x, y, i, j);
                }
            }
        }

        return tot;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean equal(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;

        Board o = (Board) other;
        if (o.N != this.N) return false;
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if (o.arr[i][j] == arr[i][j]) return false;
            }
        }

        return true;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
