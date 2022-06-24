import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class Board {

  private final int[] board;
  private final int n;

  // create a board from an n-by-n array of tiles, where tiles[row][col] = tile at
  // (row, col)
  public Board(int[][] tiles) {
    n = tiles.length;
    board = new int[n * n];
    int i = 0;
    for (int[] row : tiles) {
      for (int tile : row) {
        board[i++] = tile;
      }
    }
  }

  private Board(int[] board, int n) {
    this.n = n;
    this.board = board.clone();
  }

  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append(n + "\n");
    for (int i = 0; i < n * n; i++) {
      s.append(String.format("%2d ", board[i]));
      if (i % n == n - 1 && i != n * n - 1) {
        s.append("\n");
      }
    }
    return s.toString();
  }

  // board dimension n
  public int dimension() {
    return n;
  }

  // number of tiles out of place
  public int hamming() {
    int count = 0;
    for (int i = 0; i < n * n; i++) {
      if (board[i] != 0 && board[i] - 1 != i) {
        count++;
      }
    }
    return count;
  }

  // sum of Manhattan distances between tiles and goal
  public int manhattan() {
    int sum = 0;

    for (int i = 0; i < board.length; i++) {
      if (board[i] != 0) {
        int goalRow = (board[i] - 1) / n;
        int goalCol = (board[i] - 1) % n;

        int row = i / n;
        int col = i % n;

        int rowDist = Math.abs(goalRow - row);
        int colDist = Math.abs(goalCol - col);

        sum += rowDist;
        sum += colDist;
      }
    }

    return sum;
  }

  // is this board the goal board?
  public boolean isGoal() {
    for (int i = 0; i < n * n; i++) {
      if (i == n * n - 1 && board[i] != 0) {
        return false;
      } else if (i < n * n - 1 && board[i] != i + 1) {
        return false;
      }
    }
    return true;
  }

  // does this board equal y?
  @Override
  public boolean equals(Object y) {
    if (y == null)
      return false;
    if (y.getClass() != this.getClass())
      return false;

    Board b = (Board) y;

    if (dimension() != b.dimension()) {
      return false;
    }

    for (int i = 0; i < dimension() * dimension(); i++) {
      if (this.board[i] != b.board[i]) {
        return false;
      }
    }

    return true;
  }

  // all neighboring boards
  public Iterable<Board> neighbors() {
    int empty = 0;

    while (board[empty] != 0)
      empty++;

    int row = empty / n;
    int col = empty % n;

    Queue<Board> q = new Queue<>();

    int[] positions = { empty - 1, empty + 1, empty - n, empty + n };

    for (int position : positions) {
      if (position >= 0 && position < n * n) {
        int posRow = position / n;
        int posCol = position % n;
        // only enqueue non-diagonal moves
        if (posRow == row || posCol == col) {
          q.enqueue(new Board(swap(position, empty), n));
        }
      }
    }

    return q;
  }

  private int[] swap(int i, int j) {
    int[] arr = board.clone();
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
    return arr;
  }

  // a board that is obtained by exchanging any pair of tiles
  public Board twin() {
    int i = 0, t1 = 0, t2 = 0, count = 0;

    while (count < 2 && i < n * n) {
      if (board[i] == 0) {
        count = 0;
        i = (i / n) + n - 1;
      } else if (count < 1) {
        t1 = i;
        count++;
      } else {
        t2 = i;
        count++;
      }

      i++;
    }

    return new Board(swap(t1, t2), n);
  }

  // unit testing (not graded)
  public static void main(String[] args) {
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];

    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        tiles[i][j] = in.readInt();
    Board b = new Board(tiles);

    System.out.println(b.toString());
    System.out.print("hamming distance: ");
    System.out.println(b.hamming());
    System.out.print("manhattan distance: ");
    System.out.println(b.manhattan());

    System.out.println("neighbours:");

    Iterator<Board> it = b.neighbors().iterator();

    while (it.hasNext()) {
      System.out.println(it.next().toString());
      System.out.println();
    }

    System.out.println("twin:");
    System.out.println(b.twin().toString());

    System.out.println("isGoal");
    System.out.println(b.isGoal());
  }
}
