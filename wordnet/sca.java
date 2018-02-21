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
        int ancestor = ancestor(v, w);
        return distFrom(v).get(ancestor) + distFrom(w).get(ancestor);
    }

    // Shortest common ancestor of vertices v and w.
    public int ancestor(int v, int w) { //use separateChainingHashST to do ancestor
        SeparateChainingHashST<Integer, Integer> vDistFrom  = distFrom(v);
        SeparateChainingHashST<Integer, Integer> wDistFrom  = distFrom(w);
        int shortestDistance = Integer.MAX_VALUE;
        int shortestAncestor = -1;
        for (int u : wDistFrom.keys()){
            if (vDistFrom.contains(u)){
                int distance = vDistFrom.get(u) + wDistFrom.get(u);
                if (distance < shortestDistance){
                    shortestDistance = distance;
                    shortestAncestor = u;
                }
            }
        }
        return shortestAncestor;
    }

    // Length of the shortest ancestral path of vertex subsets A and B.
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        /*
         * make new triad by calling it get triad[0] distance between w and ancestor and distance from 
         * v and ancestor 
         * add those things together
         */
       int[] stuff = triad(subsetA, subsetB);
       int v = stuff[1];
       int w = stuff[2];
       SeparateChainingHashST<Integer, Integer> distsv = distFrom(v);
       SeparateChainingHashST<Integer, Integer> distsw = distFrom(w);
       int total = (distsv.get(stuff[0]) + distsw.get(stuff[0]));
       return total;
    }

    // A shortest common ancestor of vertex subsets A and B.
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        /* 
         * get triad method and call upon the first element
         * 
         */
        int[] h = triad(subsetA, subsetB);
        return h[0];
    }

    // Helper: Return a map of vertices reachable from v and their 
    // respective shortest distances from v.
    private SeparateChainingHashST<Integer, Integer> distFrom(int v) {
        SeparateChainingHashST<Integer, Integer> st = 
        new SeparateChainingHashST<Integer, Integer>();
        LinkedQueue<Integer> q = new LinkedQueue<Integer>();
        st.put(v, 0);
        q.enqueue(v);
        while (!q.isEmpty()){
            int x = q.dequeue();
            for (int y : G.adj(x)){
                if (!st.contains(y)){
                    st.put(y, st.get(x) + 1);
                    q.enqueue(y);
                }
            }
        }
        return st;
    }

    // Helper: Return an array consisting of a shortest common ancestor a 
    // of vertex subsets A and B, and vertex v from A and vertex w from B 
    // such that the path v-a-w is the shortest ancestral path of A and B.
    private int[] triad(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
      int shortestDistance = Integer.MAX_VALUE;
      int shortestAncestor = -1;
      int v = -1;
      int w = -1;
      for(int a : subsetA) {
        for(int b : subsetB){
             int distance = length(a, b);
             if (distance < shortestDistance){
                 shortestDistance = distance;
                 shortestAncestor = ancestor(a, b);
                 v = a;
                 w = b;
            }
        }
       }
        return new int[]{shortestAncestor, v, w};
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
