import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.StdOut;

// Models a board in the 8-puzzle game or its generalization.
public class Board {
    private int[][] tiles;
    private int N;
    private int hamming;
    private int manhattan;


    // Construct a board from an N-by-N array of tiles, where 
    // tiles[i][j] = tile at row i and column j, and 0 represents the blank 
    // square.
    public Board(int[][] tiles) {
        this.N = tiles[0].length;
        this.tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
        // Calculates hamming.
        int misplaced = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                // Disregards blank tile
                if (tiles[i][j] == 0) { continue; }
                if (tiles[i][j] != i * N + j + 1) {
                    ++misplaced;
                }
            }
        }
        hamming = misplaced;
        //Calculates manhattan.
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int ideal = i * N + j + 1;
                if (tiles[i][j] != 0 && tiles[i][j] != ideal) {
                    int idealI = (tileAt(i, j) - 1) / N;
                    int idealJ = tileAt(i, j) - 1 - idealI * N;
                    manhattan += Math.abs(i - idealI) + Math.abs(j - idealJ);
                }
            }
        }
    }

    // Tile at row i and column j.
    public int tileAt(int i, int j) {
        return tiles[i][j];
    }
    
    // Size of this board.
    public int size() {
        return N * N;
    }

    // Number of tiles out of place.
    public int hamming() {
        return hamming;
    }

    // Sum of Manhattan distances between tiles and goal.
    public int manhattan() {
        return manhattan;
    }

    // Is this board the goal board?
    public boolean isGoal() {
        // Checks if blank tile is in in right position
        // and if the number of inversions is 0.
        return blankPos() == N * N - 1 && inversions() == 0;
    }

    // Is this board solvable?
    public boolean isSolvable() {
        // Checks if odd board size is solvable.
        if (N % 2 != 0) {
            if (inversions() % 2 != 0) { return false; }
            return true;
        }
        // Checks if even board size is solvable.
        else {
            int blankRow = blankPos() / N;
            int sum = blankRow + inversions();
            if (sum % 2 == 0) { return false; }
            return true;
        }
    }

    // Does this board equal that?
    public boolean equals(Board that) {
        if (this.N != that.N) { return false; }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != that.tiles[i][j]) { return false; }
            }
        }
        return true;
    }

    // All neighboring boards.
    public Iterable<Board> neighbors() {
        LinkedQueue<Board> lq = new LinkedQueue<Board>();
        int[][] neighbor;
        int blankPos = blankPos();
        int i = blankPos / N;
        int j = blankPos % N;
        int temp;
        // if conditions handle out of range issues.
        if (i - 1 >= 0 && i - 1 < N) {
            neighbor = cloneTiles();
            temp = neighbor[i][j];
            neighbor[i][j] = neighbor[i - 1][j]; // North
            neighbor[i - 1][j] = temp;
            Board board = new Board(neighbor);
            lq.enqueue(board);
        }
        if (j + 1 >= 0 && j + 1 < N) {
            neighbor = cloneTiles();
            temp = neighbor[i][j];
            neighbor[i][j] = neighbor[i][j + 1]; // East
            neighbor[i][j + 1] = temp;
            Board board = new Board(neighbor);
            lq.enqueue(board);
        }
        if (i + 1 >= 0 && i + 1 < N) {
            neighbor = cloneTiles();
            temp = neighbor[i][j];
            neighbor[i][j] = neighbor[i + 1][j]; // South
            neighbor[i + 1][j] = temp;
            Board board = new Board(neighbor);
            lq.enqueue(board);
        }
        if (j - 1 >= 0 && j - 1 < N) {
            neighbor = cloneTiles();
            temp = neighbor[i][j];
            neighbor[i][j] = neighbor[i][j - 1]; // West
            neighbor[i][j - 1] = temp;
            Board board = new Board(neighbor);
            lq.enqueue(board);
        }
        return lq;
    }

    // String representation of this board.
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (j < N - 1) {
                    s.append(String.format("%2d ", tiles[i][j]));
                }
                else { s.append(String.format("%2d", tiles[i][j])); }
            }
            if (i < N - 1) { s.append("\n"); }
        }
        return s.toString();
    }

    // Helper method that returns the position (in row-major order) of the 
    // blank (zero) tile.
    private int blankPos() {
        int pos = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) { pos = i * N + j; }
            }
        }
        return pos;
    }

    // Helper method that returns the number of inversions.
    private int inversions() {
        int[] inv = new int[N * N - 1];
        int x = 0;
        int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                // Checks that tile is not blank tile.
                if (tileAt(i, j) != 0) {
                    inv[x] = tileAt(i, j);
                    x++;
                }
            }
        }
        for (int m = 0; m < inv.length; m++) {
            for (int n = m + 1; n < inv.length; n++) {
                if (inv[m] > inv[n]) { count++; }
            }
        }
        return count;
    }

    // Helper method that clones the tiles[][] array in this board and 
    // returns it.
    private int[][] cloneTiles() {
        int[][] clone = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                clone[i][j] = tiles[i][j];
            }
        }
        return clone;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.isGoal());
        StdOut.println(board.isSolvable());
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
    }
}
