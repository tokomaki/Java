import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

// Deque implementation using a linked list.
public class LinkedDeque<Item> implements Iterable<Item> {
    private int N;
    private Node first;
    private Node last;

    // Helper doubly-linked list class.
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    // Construct an empty deque.
    public LinkedDeque() {
        N = 0;
        first = null;
        last = null;
    }

    // Is the dequeue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // The number of items on the deque.
    public int size() {
        return N;
    }

    // Add item to the front of the deque.
    public void addFirst(Item item) {
        if (item != null) {
            Node front = new Node();
            front.item = item;
            if (first != null) {
                front.next = first;
                first.prev = front;
            }
            first = front;
            if (last == null) { last = first; }
            N++;
        }
        else { throw new NullPointerException("Item is null."); }
    }

    // Add item to the end of the deque.
    public void addLast(Item item) {
        if (item != null) {
            Node end = new Node();
            end.item = item;
            if (last != null) {
                end.prev = last;
                last.next = end;
            }
            last = end;
            if (first == null) { first = last; }
            N++;
        }
        else { throw new NullPointerException("Item is null."); }
    }

    // Remove and return item from the front of the deque.
    public Item removeFirst() {
        if (isEmpty()) { throw new NoSuchElementException("Deque is empty"); }
        N--;
        Item front = first.item;
        first = first.next;
        return front;
    }

    // Remove and return item from the end of the deque.
    public Item removeLast() {
        if (isEmpty()) { throw new NoSuchElementException("Deque is empty"); }
        N--;
        Item end = last.item;
        last = last.prev;
        return end;
    }

    // An iterator over items in the queue in order from front to end.
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    // An iterator, doesn't implement remove() since it's optional.
    private class DequeIterator implements Iterator<Item> {
        private Node current;
        
        DequeIterator() {
            current = first;
        }

        public boolean hasNext()  { return current != null; }

        public void remove() { throw new UnsupportedOperationException(); }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items");
            }
            Item temp = current.item;
            current = current.next;
            return temp;
        }
    }

    // A string representation of the deque.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item + " ");
        }
        return s.toString().substring(0, s.length() - 1);
    }
    
    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        LinkedDeque<Character> deque = new LinkedDeque<Character>();
        String quote = "There is grandeur in this view of life, with its " 
            + "several powers, having been originally breathed into a few " 
            + "forms or into one; and that, whilst this planet has gone " 
            + "cycling on according to the fixed law of gravity, from so " 
            + "simple a beginning endless forms most beautiful and most " 
            + "wonderful have been, and are being, evolved. ~ " 
            + "Charles Darwin, The Origin of Species";
        int r = StdRandom.uniform(0, quote.length());
        for (int i = quote.substring(0, r).length() - 1; i >= 0; i--) {
            deque.addFirst(quote.charAt(i));
        }
        for (int i = 0; i < quote.substring(r).length(); i++) {
            deque.addLast(quote.charAt(r + i));
        }
        StdOut.println(deque.isEmpty());
        StdOut.printf("(%d characters) ", deque.size());
        for (char c : deque) {
            StdOut.print(c);
        }
        StdOut.println();
        double s = StdRandom.uniform();
        for (int i = 0; i < quote.length(); i++) {
            if (StdRandom.bernoulli(s)) {
                deque.removeFirst();
            }
            else {
                deque.removeLast();
            }
        }
        StdOut.println(deque.isEmpty());
    }
}
