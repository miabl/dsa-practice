import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {

  private static final double CONFIDENCE_95 = 1.96;
  private final int n;
  private final int trials;
  private final double[] thresholds;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException("n and trials must be greater than zero");
    }

    this.n = n;
    this.trials = trials;
    thresholds = new double[trials];

    for (int i = 0; i < trials; i++) {
      Percolation p = new Percolation(n);

      int openCount = 0;

      while (!p.percolates()) {
        openRandom(p);
        openCount++;
      }

      thresholds[i] = (double) openCount / (n * n);
    }
  }

  // sample mean of percolation threshold
  public double mean() {
    return StdStats.mean(thresholds);
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return StdStats.stddev(thresholds);
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials));
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials));
  }

  private void openRandom(Percolation p) {
    boolean nodeOpen = true;
    int row = 0;
    int col = 0;

    while (nodeOpen) {
      row = StdRandom.uniform(1, n + 1);
      col = StdRandom.uniform(1, n + 1);

      nodeOpen = p.isOpen(row, col);
    }
    p.open(row, col);
  }

  // test client (see below)
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);

    PercolationStats s = new PercolationStats(n, trials);

    System.out.println("mean = " + s.mean());
    System.out.println("stddev = " + s.stddev());
    System.out.println("95% confidence interval = [" + s.confidenceLo() + ", " + s.confidenceHi() + "]");
  }
}
