import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture carve;

    // Create a seam carver object based on the given picture, making a
    // defensive copy of picture.
    public SeamCarver(Picture picture) {
        carve = new Picture(picture);
    }

    // Current picture.
    public Picture picture() {
        return carve;
    }

    // Width of current picture.
    public int width() {
        return carve.width();
    }

    // Height of current picture.
    public int height() {
        return carve.height();
    }

    // Energy of pixel at column x and row y.
    public double energy(int x, int y) {
        double energy = 0;
        double red, green, blue;
        Color north, east, west, south;

        north = carve.get(x, wrap(y - 1, height()));
        east  = carve.get(wrap(x + 1, width()), y);
        west  = carve.get(wrap(x - 1, width()), y);
        south = carve.get(x, wrap(y + 1, height()));

        red   = (north.getRed() - south.getRed())
                * (north.getRed() - south.getRed());
        green = (north.getGreen() - south.getGreen())
                * (north.getGreen() - south.getGreen());
        blue  = (north.getBlue() - south.getBlue())
                * (north.getBlue() - south.getBlue());

        energy += (red + green + blue);

        red   = (east.getRed() - west.getRed())
                * (east.getRed() - west.getRed());
        green = (east.getGreen() - west.getGreen())
                * (east.getGreen() - west.getGreen());
        blue  = (east.getBlue() - west.getBlue())
                * (east.getBlue() - west.getBlue());

        energy += (red + green + blue);

        return energy;
    }

    // Sequence of indices for horizontal seam.
    public int[] findHorizontalSeam() {
        SeamCarver sc = new SeamCarver(transpose(carve));
        return sc.findVerticalSeam();
    }

    // Sequence of indices for vertical seam.
    public int[] findVerticalSeam() {
        int[] seams = new int[height()];
        double minPath = Double.POSITIVE_INFINITY;
        int minIndex = -1;
        double [][] distTo = new double[height()][width()];
        for (int i = 0; i < width(); i++) {
            distTo[0][i] = energy(i, 0);
        }
        for (int j = 1; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                double dist = Double.POSITIVE_INFINITY;
                if (i - 1 >= 0
                && distTo[j - 1][i - 1] + energy(i, j) < dist) {
                    dist = distTo[j - 1][i - 1] + energy(i, j);
                }
                if (distTo[j - 1][i] + energy(i, j) < dist) {
                    dist = distTo[j - 1][i] + energy(i, j);
                }
                if (i + 1 < width()
                && distTo[j - 1][i + 1] + energy(i, j) < dist) {
                    dist = distTo[j - 1][i + 1] + energy(i, j);
                }
                distTo[j][i] = dist;
                if (j == height() - 1 && dist < minPath) {
                   minPath = dist;
                   minIndex = i;
                }
            }
        }
        seams[height() - 1] = minIndex;
        for (int j = height() - 2; j >= 0; j--) {
            double e = distTo[j + 1][minIndex] - energy(minIndex, j + 1);
            if (minIndex - 1 >= 0
            && distTo[j][minIndex - 1] == e) {
                minIndex -= 1;
            }
            if (minIndex + 1 < width()
            && distTo[j][minIndex + 1] == e) {
                minIndex += 1;
            }
            seams[j] = minIndex;
        }
        return seams;
    }

    // Remove horizontal seam from current picture.
    public void removeHorizontalSeam(int[] seam) {
        SeamCarver sc = new SeamCarver(transpose(carve));
        sc.removeVerticalSeam(seam);
        carve = transpose(sc.picture());
    }

    // Remove vertical seam from current picture.
    public void removeVerticalSeam(int[] seam) {
        Picture temp =  new Picture(width() - 1, height());
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                if (i < seam[j]) {
                    temp.set(i, j, carve.get(i, j));
                }
                if (i > seam[j]) {
                    temp.set(i - 1, j, carve.get(i, j));
                }
            }
        }
        carve = temp;
    }

    // Return y - 1 if x < 0; 0 if x >= y; and x otherwise.
    private static int wrap(int x, int y) {
        if (x < 0) {
            return y - 1;
        }
        else if (x >= y) {
            return 0;
        }
        return x;
    }

    // Return a new picture that is a transpose of the given picture.
    private static Picture transpose(Picture picture) {
        Picture transpose = new Picture(picture.height(), picture.width());
        for (int i = 0; i < transpose.width(); i++) {
            for (int j = 0; j < transpose.height(); j++) {
                transpose.set(i, j, picture.get(j, i));
            }
        }
        return transpose;
    }
}
