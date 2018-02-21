import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

// An immutable WordNet data type.
public class WordNet {
    private RedBlackBST<String, SET<Integer>> st;
    private RedBlackBST<Integer, String> rst;
    private Digraph G;
    private ShortestCommonAncestor sca;
    
    // Construct a WordNet object given the names of the input (synset and
    // hypernym) files.
    public WordNet(String synsets, String hypernyms) {
        this.st = new RedBlackBST<String, SET<Integer>>();
        this.rst = new RedBlackBST<Integer, String>();
        In in = new In(synsets);
        int id = 0;
        while (!in.isEmpty()){
            String line = in.readLine();
            String[] token = line.split(",");
            id = Integer.parseInt(token[0]);
            String[] synset = token[1].split("\\s");
            for (String  noun : synset) {
                if (!st.contains(noun)) {
                    st.put(noun, new SET<Integer>());
                }
                st.get(noun).add(id);
            }
            rst.put(id, token[1]);
        }
        in.close();
        this.G = new Digraph(id + 1);// <- this was added
        //Digraph G = new Digraph(id + 1);
        in = new In(hypernyms);
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] token = line.split(",");
            int v = Integer.parseInt(token[0]);
            for (int i = 1; i < token.length; i++){
                int u = Integer.parseInt(token[i]);
                G.addEdge(v, u);
            }
        }
        in.close();
        this.sca = new ShortestCommonAncestor(G);
    }

    // All WordNet nouns.
    public Iterable<String> nouns() {
        return st.keys();
    }

    // Is the word a WordNet noun?
    public boolean isNoun(String word) {
        return st.contains(word);
    }

    // A synset that is a shortest common ancestor of noun1 and noun2.
    public String sca(String noun1, String noun2) {
       int h = sca.ancestor(st.get(noun1), st.get(noun2));
       return rst.get(h);
    }

    // Distance between noun1 and noun2.
    public int distance(String noun1, String noun2) {
        int b =  sca.length(st.get(noun1), st.get(noun2));
        return b;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        String word1 = args[2];
        String word2 = args[3];        
        int nouns = 0;
        for (String noun : wordnet.nouns()) {
            nouns++;
        }
        StdOut.println("# of nouns = " + nouns);
        StdOut.println("isNoun(" + word1 + ") = " + wordnet.isNoun(word1));
        StdOut.println("isNoun(" + word2 + ") = " + wordnet.isNoun(word2));
        StdOut.println("isNoun(" + (word1 + " " + word2) + ") = "
                       + wordnet.isNoun(word1 + " " + word2));
        StdOut.println("sca(" + word1 + ", " + word2 + ") = "
                       + wordnet.sca(word1, word2));
        StdOut.println("distance(" + word1 + ", " + word2 + ") = "
                       + wordnet.distance(word1, word2));
    }
}
