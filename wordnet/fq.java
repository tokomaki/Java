// FrequencyCounter.java: Reads in a command-line integer and sequence of words
// from standard input and prints out all the words (whose length exceeds the
// threshold) that occur most frequently to standard output. It also prints out
// the number of words whose length exceeds the threshold and the number of
// distinct such words.

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
public class FrequencyCounter {
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        String[] a = StdIn.readAllStrings();
        String[] b = new String[a.length];
        ArrayST<String, Integer> input = new ArrayST<String, Integer>();
        int words = 0;
        int distinct = 0;
        int i = 0;
        for (String s: a){
            if (s.length() < N){
                continue;
            }
            if (input.contains(s)){
                int g = input.get(s);
                input.put(s, g += 1);
                words += 1;
            } else {
                words += 1;
                distinct += 1;
                input.put(s, 1);
                b[i] = s;
                i += 1;
            }
        }
        String max = "";
        input.put(max, 0);
        for (String word: input.keys()){
            if (input.get(word) > input.get(max)){
                max = word;
            }
        }
        for (String s: b){
            if (input.get(s) == input.get(max)){
               StdOut.print(s + " ");
            }
        }
        StdOut.print(input.get(max));
        StdOut.println("\ndistinct = " + distinct);
        StdOut.println("words = " + words);
    }
}
