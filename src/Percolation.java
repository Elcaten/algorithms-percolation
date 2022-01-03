import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int BLOCKED = 0;
    private static final int OPEN = 1;

    private int[] sites;
    private int n;
    private int topRoot;
    private int bottomRoot;
    private WeightedQuickUnionUF UF;
    private int openCount;

    private int getIndex(int row, int col) {
       return  (row - 1) * n + col - 1;
    }

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        this.sites = new int[n * n];
        for (int i = 0; i < sites.length; i++) {
            sites[i] = BLOCKED;
        }

        this.n = n;
        this.topRoot = n * n;
        this.bottomRoot = n * n + 1;
        this.openCount = 0;

        this.UF = new WeightedQuickUnionUF(n * n + 2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }

        int target = getIndex(row, col);
        int left = target - 1;
        int right = target + 1;
        int top = target - n;
        int bottom = target + n;

        if (!isOpen(row, col)) {
            openCount += 1;
            sites[target] = OPEN;

            if (row == 1) {
                UF.union(target, topRoot);
            }
            if (row == n) {
                UF.union(target, bottomRoot);
            }
            if (col > 1 && isOpen(row, col - 1)) {
                UF.union(target, left);
            }
            if (col < n && isOpen(row, col + 1)) {
                UF.union(target, right);
            }
            if (row > 1 && isOpen(row - 1, col)) {
                UF.union(target, top);
            }
            if (row < n && isOpen(row + 1, col)) {
                UF.union(target, bottom);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }

        return sites[getIndex(row, col)] == OPEN;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }

        return isOpen(row, col) && UF.find(getIndex(row, col)) == UF.find(topRoot);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return UF.find(topRoot) == UF.find(bottomRoot);
    }

    // test client (optional)
    public static void main(String[] args) {
        var p = new Percolation(3);
        p.open(2, 1);
        StdOut.print(p.isFull(2, 1));
    }
}