import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {
    private int T;
    private double[] p;

    // Perform T independent experiments (Monte Carlo simulations) on an 
    // N-by-N grid.
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) { 
            throw new IllegalArgumentException("N and T must be > 0");
        }
        this.T = T;
        p = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation perc = new Percolation(N);
            while (!perc.percolates()) {
                int a = StdRandom.uniform(0, N);
                int b = StdRandom.uniform(0, N);
                perc.open(a, b);
            }
            p[i] = (double) perc.numberOfOpenSites() / (N * N);
        }
    }
    
    // Sample mean of percolation threshold.
    public double mean() {
        double u = StdStats.mean(p);
        return u;
    }

    // Sample standard deviation of percolation threshold.
    public double stddev() {
        double numerator = 0;
        for (double fracs : p) { numerator += (fracs - mean())
                                 * (fracs - mean()); }
        double sigma = numerator / (T - 1);
        return Math.sqrt(sigma);
    }

    // Low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        return mean() - (1.96 * stddev()) / Math.sqrt(T);
    }

    // High endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        return mean() + (1.96 * stddev()) / Math.sqrt(T);
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean           = %f\n", stats.mean());
        StdOut.printf("stddev         = %f\n", stats.stddev());
        StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
        StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
    }
}

