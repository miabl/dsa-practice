import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

  private final WeightedQuickUnionUF grid;
  private final WeightedQuickUnionUF full;
  private final int n;
  private final int top;
  private final int bottom;
  private boolean[] openSites;
  private int openSiteCount;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    if (n <= 0)
      throw new IllegalArgumentException("n must be greater than 0");

    grid = new WeightedQuickUnionUF(n * n + 2);
    full = new WeightedQuickUnionUF(n * n + 1);

    this.n = n;

    // compute coordinates for the 'top' and 'bottom' of the grid
    top = flatIndex(n, n) + 1;
    bottom = flatIndex(n, n) + 2;

    openSites = new boolean[n * n];
    openSiteCount = 0;
  }

  /**
   * Converts the two co-ordinates of the java array (start = 0) to the
   * 'flattened' start=1 array co-ordinate.
   *
   * @param i the row of the site
   * @param j the column of the site
   * @return the flattened array co-ordinate (starts from 1)
   */

  private int flatIndex(int row, int col) {
    checkBounds(row, col);
    return (n * (row - 1) + col) - 1;
  }

  private boolean isValid(int row, int col) {
    return row > 0 && row <= n && col > 0 && col <= n;
  }

  private void checkBounds(int row, int col) {
    if (!isValid(row, col)) {
      throw new IllegalArgumentException("values must be between 0 and " + n);
    }
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    if (isOpen(row, col))
      return;

    int i = flatIndex(row, col);
    openSites[i] = true;

    // union with top if node is in the top row
    if (row == 1 && !(grid.find(i) == grid.find(top))) {
      grid.union(top, i);
      full.union(top, i);
    }

    // union with bottom if node is in the bottom row
    if (row == n) {
      grid.union(bottom, i);
    }

    // union with the surrounding nodes (if valid & open)
    if (isValid(row - 1, col) && isOpen(row - 1, col)) {
      grid.union(flatIndex(row - 1, col), i);
      full.union(flatIndex(row - 1, col), i);
    }

    if (isValid(row + 1, col) && isOpen(row + 1, col)) {
      grid.union(flatIndex(row + 1, col), i);
      full.union(flatIndex(row + 1, col), i);
    }

    if (isValid(row, col - 1) && isOpen(row, col - 1)) {
      grid.union(flatIndex(row, col - 1), i);
      full.union(flatIndex(row, col - 1), i);
    }

    if (isValid(row, col + 1) && isOpen(row, col + 1)) {
      grid.union(flatIndex(row, col + 1), i);
      full.union(flatIndex(row, col + 1), i);
    }

    openSiteCount++;
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    checkBounds(row, col);
    return openSites[flatIndex(row, col)];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    checkBounds(row, col);
    int i = flatIndex(row, col);
    return isOpen(row, col) && full.find(i) == full.find(top);
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return openSiteCount;
  }

  // does the system percolate?
  public boolean percolates() {
    return grid.find(top) == grid.find(bottom);
  }
}
