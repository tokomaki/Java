import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// An immutable data type for computing shortest common ancestors.
public class ShortestCommonAncestor {
    private final Digraph G;

    // Construct a ShortestCommonAncestor object given a rooted DAG.
    public ShortestCommonAncestor(Digraph G) {
        this.G = new Digraph(G);
    }

    // Length of the shortest ancestral path between v and w.
    public int length(int v, int w) {
        int relative = ancestor(v, w);
        return distFrom(v).get(relative) + distFrom(w).get(relative);
    }

    // Shortest common ancestor of vertices v and w.
    public int ancestor(int v, int w) {
        SeparateChainingHashST<Integer, Integer> vDist = distFrom(v);
        SeparateChainingHashST<Integer, Integer> wDist = distFrom(w);
        int smallDist = Integer.MAX_VALUE;
        int common = 0;
        for (int x : wDist.keys()) {
            if (vDist.contains(x)) {
                int dist = vDist.get(x) + wDist.get(x);
                if (dist < smallDist) {
                    smallDist = dist;
                    common = x;
                }
            }
        }
        return common;
    }

    // Length of the shortest ancestral path of vertex subsets A and B.
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        int[] things = triad(subsetA, subsetB);
        SeparateChainingHashST<Integer, Integer> dv = distFrom(things[1]);
        SeparateChainingHashST<Integer, Integer> dw = distFrom(things[2]);
        int sum = (dv.get(things[0]) + dw.get(things[0]));
        return sum;
    }

    // A shortest common ancestor of vertex subsets A and B.
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        int[] sca = triad(subsetA, subsetB);
        return sca[0];
    }

    // Helper: Return a map of vertices reachable from v and their
    // respective shortest distances from v.
    private SeparateChainingHashST<Integer, Integer> distFrom(int v) {
        SeparateChainingHashST<Integer, Integer> st =
        new SeparateChainingHashST<Integer, Integer>();
        LinkedQueue<Integer> queue = new LinkedQueue<Integer>();
        st.put(v, 0);
        queue.enqueue(v);
        while (!queue.isEmpty()) {
            int var = queue.dequeue();
            for (int adj : G.adj(var)) {
                if (!st.contains(adj)) {
                    st.put(adj, st.get(var) + 1);
                    queue.enqueue(adj);
                }
            }
        }
        return st;
    }

    // Helper: Return an array consisting of a shortest common ancestor a
    // of vertex subsets A and B, and vertex v from A and vertex w from B
    // such that the path v-a-w is the shortest ancestral path of A and B.
    private int[] triad(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        int minDistance = Integer.MAX_VALUE;
        int closestAncestor = 0;
        int x = 0;
        int y = 0;
        for (int a : subsetA) {
            for (int b : subsetB) {
                int dist = length(a, b);
                if (dist < minDistance) {
                    minDistance = dist;
                    closestAncestor = ancestor(a, b);
                    x = a;
                    y = b;
                }
            }
        }
        return new int[]{closestAncestor, x, y};
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
