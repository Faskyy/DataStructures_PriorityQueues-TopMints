*  Compilation:  javac MaxPQ.java
 *  Execution:    java MaxPQ
 *
 *  Generic max priority queue implementation with a binary heap.
 *
 *  % java MaxPQ
 *  	Processing characters in [P Q E - X A M - P L E -]
 * 	    by placing each character in a max-priority queue and
 * 	    by removing the largest character when I see a '-':
 *  Q
 *  X
 *  P (6 left on pq)
 *
 *  We use a one-based array to simplify parent and child calculations.
 ******************************************************************************/

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Scanner;

// Class MaxPQ represents a priority queue of generic keys.
public class MaxPQ<Key> {
   private Key[] pq;                    // store items at indices 1 to n
   private int n;                       // number of items on priority queue
   private Comparator<Key> comparator;  // optional comparator

   //Initializes an empty priority queue with the given initial capacity.
   public MaxPQ(int initCapacity) {
      pq = (Key[]) new Object[initCapacity + 1];
      n = 0;
   }

   // Initializes an empty priority queue
   public MaxPQ() {
      this(1);
   }

   // Takes time proportional to the number of keys, using sink-based heap construction.
   public MaxPQ(Key[] keys) {
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

   //Throws NoSuchElementException if this priority queue is empty
   public Key max() {
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

   //Removes and returns a largest key on this priority queue.
   public Key delMax() {
      if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
      Key max = pq[1];
      exch(1, n--);
      sink(1);
      pq[n+1] = null;     // to avoid loitering and help with garbage collection
      if ((n > 0) && (n == (pq.length - 1) / 4)) resize(pq.length / 2);
      return max;
   }


   /***************************************************************************
    * Helper functions to restore the heap invariant.
    ***************************************************************************/

   private void swim(int k) { //involves swapping nodes if child < parent
      // YOUR CODE HERE
      while (k>1 && less(k/2, k)){ //while k is less than one  and comparator is comparatoble
         exch(k, k/2); // swap two values
         k = k /2;
      }

   }

   private void sink(int k) { //involves swapping nodes if parent > child
         // YOUR CODE HERE
      while(2*k <= n){
         int j = 2*k;
         if (j < n && less(j, j+1))
            j = j +1;
         if (!less(k,j)) break;
         exch(k,j);
         k = j;
      }
   }

   /***************************************************************************
    * Helper functions for compares and swaps.
    ***************************************************************************/
   private boolean less(int i, int j) {
      if (comparator == null) {
         return ((Comparable<Key>) pq[i]).compareTo(pq[j]) < 0;
      }
      else {
         return comparator.compare(pq[i], pq[j]) < 0;
      }
   }

   private void exch(int i, int j) {
      Key swap = pq[i];
      pq[i] = pq[j];
      pq[j] = swap;
   }

   // Unit tests the MaxPQ data type.
   public static void main(String[] args) {
      MaxPQ<Character> pq = new MaxPQ<>();   // create pq of Characters
      Character[] input = {'P', 'Q', 'E', '-', 'X', 'A', 'M', '-', 'P', 'L', 'E', '-'};
      System.out.println("\n\tProcessing characters in [P Q E - X A M - P L E -]");
      System.out.println("\tby placing each character in a max-priority queue and ");
      System.out.println("\tby removing the largest character when I see a '-':");
      for (Character character : input) {
         if (!character.equals('-'))  pq.insert(character);
         else if (!pq.isEmpty()) System.out.println(pq.delMax() + " ");
      }
      System.out.println("(" + pq.size() + " left on pq)");
   }
}
