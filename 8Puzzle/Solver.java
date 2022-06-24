import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

  private SearchNode solved;

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    if (initial == null) {
      throw new IllegalArgumentException();
    }

    solved = null;

    SearchNode init = new SearchNode(initial, 0, null);
    SearchNode twinInit = new SearchNode(initial.twin(), 0, null);

    solve(init, twinInit);
  }

  private void addChildren(SearchNode parent, MinPQ<SearchNode> q) {
    for (Board child : parent.board.neighbors()) {
      if (parent.previous == null || !child.equals(parent.previous.board))
        q.insert(new SearchNode(child, parent.moves + 1, parent));
    }
  }

  // is the initial board solvable?
  public boolean isSolvable() {
    return solved != null;
  }

  // min number of moves to solve initial board, -1 if unsolvable
  public int moves() {
    if (solved == null)
      return -1;
    return solved.moves;
  }

  private void solve(SearchNode init, SearchNode twinInit) {
    MinPQ<SearchNode> pq = new MinPQ<>();
    MinPQ<SearchNode> twinpq = new MinPQ<>();

    pq.insert(init);
    twinpq.insert(twinInit);

    SearchNode parent = pq.delMin();
    SearchNode twinParent = twinpq.delMin();
    addChildren(parent, pq);
    addChildren(twinParent, twinpq);

    while (!parent.board.isGoal() && !twinParent.board.isGoal()) {
      parent = pq.delMin();
      addChildren(parent, pq);

      twinParent = twinpq.delMin();
      addChildren(twinParent, twinpq);
    }

    if (parent.board.isGoal()) {
      solved = parent;
    }
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    if (solved == null)
      return null;

    SearchNode current = solved;
    Stack<Board> s = new Stack<>();

    s.push(current.board);

    while (current.previous != null) {
      s.push(current.previous.board);
      current = current.previous;
    }
    return s;
  }

  public static void main(String[] args) {

    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
      StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution()) {
        StdOut.println(board);
        StdOut.println();
      }
    }
  }

  private class SearchNode implements Comparable<SearchNode> {

    Board board;
    int moves;
    SearchNode previous;
    int priority;

    private SearchNode(Board board, int moves, SearchNode prev) {
      this.board = board;
      this.moves = moves;
      previous = prev;
      priority = moves + board.manhattan();
    }

    public int compareTo(SearchNode that) {
      return this.priority - that.priority;
    }

  }
}
