package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // create N-by-N grid, with all sites initially blocked
    private int N;
    private int[][] arr;
    private WeightedQuickUnionUF site;
    private int size;
    private int bottomConnector;
    private int upperConnector;

    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("Invalid index!");
        }
        this.N = N;
        bottomConnector = N * N;
        upperConnector = N * N + 1;
        site = new WeightedQuickUnionUF(N * N + 2);
        size = 0;
        arr = new int[N][N];
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                arr[i][j] = 0;
            }
        }

        for (int i = 0; i < N; i += 1) {
            site.union(bottomConnector, i);
            site.union(upperConnector, i + N * (N - 1));
        }
    }

    private int getPos(int row, int col) {
        return row * N + col;
    }

    private boolean validatePos(int row, int col) {
        if (row < 0 || row > arr.length - 1 || col < 0 || col > arr.length - 1) {
            return false;
        }
        return true;
    }

    private void connect(int prerow, int precol, int nowrow, int nowcol) {
        if (!validatePos(nowrow, nowcol) || !validatePos(prerow, precol)) {
            return;
        }
        if (arr[nowrow][nowcol] == 1) {
            site.union(getPos(nowrow, nowcol), getPos(prerow, precol));
        }

    }
    
    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!validatePos(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("Invalid index!");
        }
        if (arr[row][col] == 0) {
            arr[row][col] = 1;
            size += 1;
            connect(row, col, row + 1, col);
            connect(row, col, row - 1, col);
            connect(row, col, row, col - 1);
            connect(row, col, row, col + 1);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!validatePos(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("Invalid index!");
        }
        return arr[row][col] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!validatePos(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("Invalid index!");
        }
        if (!isOpen(row, col)) {
            return false;
        }
        return site.connected(bottomConnector, getPos(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return size;
    }

    // does the system percolate?
    public boolean percolates() {
        return site.connected(upperConnector, bottomConnector);
    }

    // use for unit testing (not required)
    public static void main(String[] args) {
    }

}
