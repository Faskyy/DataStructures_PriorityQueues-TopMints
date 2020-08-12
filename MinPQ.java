*  Compilation:  javac MinPQ.java
 *  Execution:    java MinPQ
 *
 *  Generic min priority queue implementation with a binary heap.
 *
 *  % java MinPQ
 *  	Processing characters in [P Q E - X A M - P L E -]
 * 	    by placing each character in a min-priority queue and
 * 	    by removing the largest character when I see a '-':
 *  E
 *  A
 *  E (6 left on pq)
 *
 *  We use a one-based array to simplify parent and child calculations.
 ******************************************************************************/

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

// Class MinPQ} represents a priority queue of generic keys.
public class MinPQ<Key> implements Iterable<Key> {
   private Key[] pq;                    // store items at indices 1 to n
   private int n;                       // number of items on priority queue
   private Comparator<Key> comparator;  // optional comparator

   // Initializes an empty priority queue with the given initial capacity.
   public MinPQ(int initCapacity) {
      pq = (Key[]) new Object[initCapacity + 1];
      n = 0;
   }

   // Initializes an empty priority queue.
   public MinPQ() {
      this(1);
   }


   // Initializes a priority queue from the array of keys.
   // Takes time proportional to the number of keys, using sink-based heap construction.
   public MinPQ(Key[] keys) {
      n = keys.length;
      pq = (Key[]) new Object[keys.length + 1];
      for (int i = 0; i < n; i++)
         pq[i+1] = keys[i];
      for (int k = n/2; k >= 1; k--)
         sink(k);
   }

   // Returns true if this priority queue is empty.
   public boolean isEmpty() { return n == 0; }

   // Returns the number of keys on this priority queue.
   public int size() { return n; }

   // Returns a smallest key on this priority queue.
    // Throws NoSuchElementException if this priority queue is empty
   public Key min() {
      if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
      return pq[1];
   }

   // helper function to double the size of the heap array
   private void resize(int capacity) {
      assert capacity > n;
      Key[] temp = (Key[]) new Object[capacity];
      for (int i = 1; i <= n; i++) {
         temp[i] = pq[i];
      }
      pq = temp;
   }

   // Adds a new key to this priority queue.
   public void insert(Key x) {
      // double size of array if necessary
      if (n == pq.length - 1) resize(2 * pq.length);

      // add x, and percolate it up to maintain heap invariant
      pq[++n] = x;
      swim(n);
   }

   // Removes and returns a smallest key on this priority queue.
   public Key delMin() {
      if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
      Key min = pq[1];
      exch(1, n--);
      sink(1);
      pq[n+1] = null;     // to avoid loiterig and help with garbage collection
      if ((n > 0) && (n == (pq.length - 1) / 4)) resize(pq.length / 2);
      return min;
   }


   /***************************************************************************
    * Helper functions to restore the heap invariant.
    ***************************************************************************/

   private void swim(int k) {
      // YOUR CODE HERE
      while (k > 1 && greater(k/2, k)){
         exch(k, k/2);
         k = k/2;
      }
   }

   private void sink(int k) {
      // YOUR CODE HERE
      while(2*k <= n){
         int j = 2*k;
         if (j < n && greater(j, j+1))
            j++;
         if (!greater(k ,j))
            break;
         exch(k, j);
         k = j;
      }
   }

   /***************************************************************************
    * Helper functions for compares and swaps.
    ***************************************************************************/
   private boolean greater(int i, int j) {
      if (comparator == null) {
         return ((Comparable<Key>) pq[i]).compareTo(pq[j]) > 0;
      }
      else {
         return comparator.compare(pq[i], pq[j]) > 0;
      }
   }

   private void exch(int i, int j) {
      Key swap = pq[i];
      pq[i] = pq[j];
      pq[j] = swap;
   }

   public Iterator<Key> iterator() {
      return new HeapIterator();
   }

   private class HeapIterator implements Iterator<Key> {
      // create a new pq
      private MinPQ<Key> copy;

      // add all items to copy of heap
      // takes linear time since already in heap order so no keys move
      public HeapIterator() {
         if (comparator == null) copy = new MinPQ<Key>(size());
         for (int i = 1; i <= n; i++)
            copy.insert(pq[i]);
      }

      public boolean hasNext()  { return !copy.isEmpty();                     }
      public void remove()      { throw new UnsupportedOperationException();  }

      public Key next() {
         if (!hasNext()) throw new NoSuchElementException();
         return copy.delMin();
      }
   }

   // Unit tests the MinPQ data type.
   public static void main(String[] args) {
      MinPQ<Character> pq = new MinPQ<>();   // create pq of Characters
      Character[] input = {'P', 'Q', 'E', '-', 'X', 'A', 'M', '-', 'P', 'L', 'E', '-'};
      System.out.println("\n\tProcessing characters in [P Q E - X A M - P L E -]");
      System.out.println("\tby placing each character in a min-priority queue and ");
      System.out.println("\tby removing the smallest character when I see a '-':");
      for (Character character : input) {
         if (!character.equals('-'))  pq.insert(character);
         else if (!pq.isEmpty()) System.out.println(pq.delMin() + " ");
      }
      System.out.println("(" + pq.size() + " left on pq)");
   }
}
