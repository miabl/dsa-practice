import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

  private Item[] queue;
  private int size;

  // construct an empty randomized queue
  public RandomizedQueue() {
    queue = (Item[]) new Object[1];
    size = 0;
  }

  // is the randomized queue empty?
  public boolean isEmpty() {
    return size == 0;
  }

  // return the number of items on the randomized queue
  public int size() {
    return size;
  }

  // add the item
  public void enqueue(Item item) {
    if (item == null)
      throw new IllegalArgumentException();
    if (size == queue.length) {
      Item[] newQueue = (Item[]) new Object[size * 2];
      for (int i = 0; i < size; i++) {
        newQueue[i] = queue[i];
      }
      queue = newQueue;
    }
    queue[size++] = item;
  }

  // remove and return a random item
  public Item dequeue() {
    if (isEmpty())
      throw new NoSuchElementException();
    int n = StdRandom.uniform(size);
    Item tmp = queue[n];
    queue[n] = queue[--size];

    if (size < queue.length / 4) {
      Item[] newQueue = (Item[]) new Object[queue.length / 2];
      for (int i = 0; i < size; i++) {
        newQueue[i] = queue[i];
      }
      queue = newQueue;
    }
    return tmp;
  }

  // return a random item (but do not remove it)
  public Item sample() {
    if (isEmpty())
      throw new NoSuchElementException();
    int n = StdRandom.uniform(size);
    return queue[n];
  }

  private class RQueueIterator implements Iterator<Item> {
    private Item[] itqueue;
    private int itsize;

    public RQueueIterator() {
      itqueue = (Item[]) new Object[size];
      for (int i = 0; i < size; i++) {
        itqueue[i] = queue[i];
      }
      itsize = size;
    }

    public boolean hasNext() {
      return itsize > 0;
    }

    public Item next() {
      if (!hasNext())
        throw new NoSuchElementException();
      int n = StdRandom.uniform(itsize);
      Item tmp = itqueue[n];
      itqueue[n] = itqueue[--itsize];
      return tmp;
    }
  }

  // return an independent iterator over items in random order
  public Iterator<Item> iterator() {
    return new RQueueIterator();
  }

  // unit testing (required)
  public static void main(String[] args) {
    RandomizedQueue<Integer> q = new RandomizedQueue<>();
    System.out.println(q.isEmpty());
    q.enqueue(1);
    q.enqueue(2);
    q.enqueue(3);
    q.enqueue(4);
    System.out.println(q.isEmpty());

    Iterator<Integer> it = q.iterator();

    System.out.println("before dequeue");
    while (it.hasNext()) {
      System.out.println(it.next());
    }

    q.dequeue();

    it = q.iterator();
    System.out.println("after dequeue");
    while (it.hasNext()) {
      System.out.println(it.next());
    }
  }

}
