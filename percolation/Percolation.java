import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// Models an N-by-N percolation system.
public class Percolation {
    private int N;
    private boolean[][] open;
    private int openSites;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF w;
    
    // Create an N-by-N grid, with all sites blocked.
    public Percolation(int N) {
        if (N <= 0) { 
            throw new IllegalArgumentException("N cannot be 0 or negative");
        }
        this.N = N;
        open = new boolean[N][N];
        this.openSites = openSites;
        uf = new WeightedQuickUnionUF(N * N + 2);
        // Second Union Find object to avoid backwash problem.
        w = new WeightedQuickUnionUF(N * N + 1);
        // Loop in charge of connecting source and sink.
        for (int x = 1; x <= N; x++) {
            uf.union(0, x);
            uf.union(N * N + 1, encode(N - 1, x - 1)); 
        }
        for (int y = 1; y <= N; y++) {
            w.union(0, y);
        }
    }

    // Open site (i, j) if it is not open already.
    public void open(int i, int j) {
        // Variables for connecting a site with its neighbors.
        int home;
        int neighbor;
        if (i < 0 || j < 0 || i > N - 1 || j > N - 1) {
            throw new IndexOutOfBoundsException("i and j must be > 0 and < N");
        }
        if (!isOpen(i, j)) { 
            open[i][j] = true;
            openSites++;
        }
        if ((i - 1) >= 0 && (i - 1) < N && isOpen(i - 1, j)) {
            home = encode(i, j);
            neighbor = encode(i - 1, j);
            uf.union(home, neighbor);
            w.union(home, neighbor);
        }
        if ((j + 1) >= 0 && (j + 1) < N && isOpen(i, j + 1)) {
            home = encode(i, j);
            neighbor = encode(i, j + 1);
            uf.union(home, neighbor);
            w.union(home, neighbor);
        }
        if ((j - 1) >= 0 && (j - 1) < N && isOpen(i, j - 1)) {
            home = encode(i, j);
            neighbor = encode(i, j - 1);
            uf.union(home, neighbor);
            w.union(home, neighbor);
        }
        if ((i + 1) >= 0 && (i + 1) < N && isOpen(i + 1, j)) {
            home = encode(i, j);
            neighbor = encode(i + 1, j);
            uf.union(home, neighbor);
            w.union(home, neighbor);
        }    
    }

    // Is site (i, j) open?
    public boolean isOpen(int i, int j) {
        if (i < 0 || j < 0 || i > N - 1 || j > N - 1) {
            throw new IndexOutOfBoundsException("i and j must be > 0 and < N");
        }
        return open[i][j];
    }

    // Is site (i, j) full?
    public boolean isFull(int i, int j) {
        if (i < 0 || j < 0 || i > N - 1 || j > N - 1) {
            throw new IndexOutOfBoundsException("i and j must be > 0 and < N");
        }
        return isOpen(i, j) && w.connected(encode(i, j), 0);
    }

    // Number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }

    // Does the system percolate?
    public boolean percolates() {
        return uf.connected(0, N * N + 1);
    }

    // An integer ID (1...N) for site (i, j).
    private int encode(int i, int j) {
        return i * N + j + 1;
    }
    
    

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        }
        else {
            StdOut.println("does not percolate");
        }
        
        // Check if site (i, j) optionally specified on the command line
        // is full.
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.println(perc.isFull(i, j));
        }
    }
}
