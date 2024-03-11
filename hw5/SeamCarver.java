import java.awt.*;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private int width;
    private int height;
    private Picture picture;
    private boolean isVertical = true;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int x, int y) {
        if (x >= width || y >= height) {
            throw new java.lang.IndexOutOfBoundsException("Invalid index!");
        }

        Color up, down, left, right;
        if (isVertical) {
            up = picture.get(x, up(y));
            down = picture.get(x, down(y));
            left = picture.get(left(x), y);
            right = picture.get(right(x), y);
        }

        else {
            up = picture.get(y, up(x));
            down = picture.get(y, down(x));
            left = picture.get(left(y), x);
            right = picture.get(right(y), x);
        }

        int rx = left.getRed() - right.getRed();
        int gx = left.getGreen() - right.getGreen();
        int bx = left.getBlue() - right.getBlue();
        int ry = up.getRed() - down.getRed();
        int gy = up.getGreen() - down.getGreen();
        int by = up.getBlue() - down.getBlue();

        return rx * rx + gx * gx + bx * bx + ry * ry + gy * gy + by * by;
    }

    private int left(int x) {
        return (x - 1) % width;
    }

    private int right(int x) {
        return (x + 1) % width;
    }

    private int up(int x) {
        return (x - 1) % height;
    }

    private int down(int x) {
        return (x + 1) % height;
    }

    private void swap() {
        int tmp = width;
        width = height;
        height = tmp;
    }

    public int[] findHorizontalSeam() {
        isVertical = false;
        swap();
        int[] res = findVerticalSeam();
        swap();
        isVertical = true;
        return res;
    }

    public int[] findVerticalSeam() {
        // path is to record the minimum cost choice of each coordinate
        // cost is to record the minimum cost
        int[][] path = new int[width][height];
        double[][] cost = new double[width][height];

        // note that the top of image in java is actually the bottom
        // so path[i][height - 1] is the actual bottom
        for (int i = 0; i < width; i += 1) {
            double e = isVertical ? energy(i, 0) : energy(0, i);
            cost[i][0] = e;
            path[i][height - 1] = i;
        }

        for (int i = 0; i < width; i += 1) {
            for (int j = 1; j < height; j += 1) {
                double e = isVertical ? energy(i, j) : energy(j, i);
                cost[i][j] = e + getMin(i, j, path, cost);
            }
        }

        // write the minimum path
        int[] res = new int[height];
        // write the minimum cost of each col
        double minCost = Double.MAX_VALUE;
        int minPos = 0;
        for(int i = 0; i < width; i += 1) {
            if (cost[i][height - 1] <minCost) {
                minCost = cost[i][height - 1];
                minPos = i;
            }
        }
        for (int i = height - 1; i >= 0; i -= 1) {
            res[i] = path[minPos][i];
            minPos = path[minPos][i];
        }

        return res;
    }

    private double getMin(int i, int j, int[][] path, double[][] cost) {
        double[] e = new double[3];
        e[1] = cost[i][j - 1];
        e[0] = i > 0 ? cost[i - 1][j - 1] : Double.MAX_VALUE;
        e[2] = i < width - 1 ? cost[i + 1][j - 1] : Double.MAX_VALUE;

        int minPos = 0;
        double minCost = Double.MAX_VALUE;
        for (int row = 0; row < 3; row += 1) {
            if (e[row] < minCost) {
                minCost = e[row];
                minPos = row;
            }
        }

        minPos = i + minPos - 1;
        path[i][j] = minPos;
        return minCost;
    }

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != width || !isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }
        SeamRemover.removeHorizontalSeam(picture, seam);
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height || !isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }
        SeamRemover.removeVerticalSeam(picture, seam);
    }

    private boolean isValidSeam(int[] seam) {
        for (int i = 0, j = 1; j < seam.length; i++, j++) {
            if (Math.abs(seam[i] - seam[j]) > 1) {
                return false;
            }
        }
        return true;
    }

}
