import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class client {
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
        for (int i = 20; i < 40; i++) {
            for (int j = 0; j < 10; j++) {
                if (!perc.isOpen(i, j)) { 
                    StdOut.printf(" %d  %d\n", i, j);
                }
            }
        }
    }
}
