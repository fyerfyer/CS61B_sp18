package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // create N-by-N grid, with all sites initially blocked
    private int[][] arr;
    private WeightedQuickUnionUF site;
    private int size;

    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("Invalid index!");
        }
        site = new WeightedQuickUnionUF(N * N + 1);
        size = 0;
        arr = new int[N][N];
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                arr[i][j] = 0;
            }
        }
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || row > arr.length - 1 || col < 0 || col > arr.length - 1) {
            throw new java.lang.IndexOutOfBoundsException("Invalid index!");
        }

        if (arr[row][col] == 0) {
            arr[row][col] = 1;
            size += 1;
            int index = row * arr.length + col;
            int[] positionIndex = new int[4];
            // up
            if (row == 0 || arr[row - 1][col] == 0) {
                positionIndex[0] = -1;
            }
            positionIndex[0] = (row - 1) * arr.length + col;
            //down
            if (row == arr.length - 1 || arr[row + 1][col] == 0) {
                positionIndex[1] = -1;
            }
            positionIndex[1] = (row + 1) * arr.length + col;
            //left
            if (col == 0 || arr[row][col - 1] == 0) {
                positionIndex[2] = -1;
            }
            positionIndex[2] = row * arr.length + col - 1;
            //right
            if (col == arr.length - 1 || arr[row][col + 1] == 0) {
                positionIndex[3] = -1;
            }
            positionIndex[3] = row * arr.length +col + 1;

            for (int i = 0; i < 4; i += 1) {
                if (positionIndex[i] >= 0) {
                    site.union(positionIndex[i], index);
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return arr[row][col] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return size == arr.length * arr.length;
    }

    // number of open sites
    public int numberOfOpenSites() {
        return size;
    }

    // does the system percolate?
    public boolean percolates() {
        for (int i = 0; i < arr.length; i += 1) {
            for (int j = 0; j < arr.length; j += 1) {
                if (site.connected(i, j + arr.length * (arr.length - 1))) {
                    return true;
                }
            }
        }

        return false;
    }

}
