import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

  private Node first, last = null;
  private int size;

  // construct an empty deque
  public Deque() {
    last = new Node(null);
    first = new Node(null);
    last.previous = first;
    first.next = last;
  }

  // is the deque empty?
  public boolean isEmpty() {
    return first.item == null && last.item == null;
  }

  // return the number of items on the deque
  public int size() {
    return size;
  }

  // add the item to the front
  public void addFirst(Item item) {
    if (item == null) {
      throw new IllegalArgumentException();
    }
    if (isEmpty()) {
      first.item = item;
      first.next = null;
      last = first;
    } else if (first.item == null) {
      first.item = item;
    } else {
      Node newNode = new Node(item);
      newNode.next = first;
      first.previous = newNode;
      first = newNode;
    }
    size++;
  }

  // add the item to the back
  public void addLast(Item item) {
    if (item == null) {
      throw new IllegalArgumentException();
    }
    if (isEmpty()) {
      first.item = item;
      first.next = null;
      last = first;
    } else if (last.item == null) {
      last.item = item;
    } else {
      Node newNode = new Node(item);
      newNode.previous = last;
      last.next = newNode;
      last = newNode;
    }
    size++;
  }

  // remove and return the item from the front
  public Item removeFirst() {
    if (isEmpty())
      throw new NoSuchElementException();
    Item toReturn = first.item;
    if (first.next == null) {
      first.item = null;
    } else {
      first = first.next;
      first.previous = null;
    }
    size--;
    return toReturn;
  }

  // remove and return the item from the back
  public Item removeLast() {
    if (isEmpty())
      throw new NoSuchElementException();
    Item toReturn = last.item;
    if (last.previous != null) {
      last = last.previous;
      last.next = null;
    } else {
      last.item = null;
    }
    size--;
    return toReturn;
  }

  private void printList() {
    Iterator<Item> it = iterator();
    while (it.hasNext()) {
      Item item = it.next();
      System.out.print(item);
      System.out.print(" ");
    }
    System.out.print("\n");

  }

  private class DequeIterator implements Iterator<Item> {
    private Node current = first;

    public boolean hasNext() {
      return current != null && current.item != null;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public Item next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      if (current == null) {
        throw new NoSuchElementException();
      }
      Item next = current.item;
      current = current.next;
      return next;
    }
  }

  // return an iterator over items in order from front to back
  public Iterator<Item> iterator() {
    return new DequeIterator();
  }

  // make nodes for linked list
  private class Node {
    Item item;
    Node next;
    Node previous;

    private Node(Item item) {
      this.item = item;
      this.next = null;
      this.previous = null;
    };

    private Node(Item item, Node next, Node previous) {
      this.item = item;
      this.next = next;
      this.previous = previous;
    }
  }

  // unit testing (required)
  public static void main(String[] args) {
    Deque<Integer> deque = new Deque<>();

    System.out.println(String.format("empty? %b", deque.isEmpty()));

    deque.addFirst(1);

    System.out.println(deque.first.item);
    System.out.println(deque.last.item);

    deque.printList();

    System.out.println(deque.removeLast());
  }

}
