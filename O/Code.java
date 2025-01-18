import java.util.*;

// The problem involves navigating through a cave represented as a grid while minimizing the number of cells that need to be dug.
// You start at the top-left corner and aim to reach the bottom of the cave.
// You can move left or right if the cell is empty, and you fall straight down until hitting solid rock or the ground.
// However, the maximum falling distance is limited to avoid getting hurt.
// To proceed, you may need to dig certain cells, but you can only dig adjacent cells below you if the cell above them is empty.
// The goal is to find a path to the bottom while minimizing the number of cells dug.
// If reaching the bottom is not possible, the output is "No"; otherwise, the output is "Yes" followed by the minimum number of digs required.
public class Main {

    // A large constant representing an infinite number of digs (used for initialization)
    static final int MANY = 0x7F7F7F7F;

    // Maximum number of rows and columns allowed
    static final int MAXRC = 50;

    // Variables to store the number of rows (r), columns (c), and the maximum falling distance (f)
    static int r, c, f;

    // 2D array representing the cave grid. Each cell can be empty ('.') or solid rock ('#')
    static char[][] ma = new char[MAXRC + 1][MAXRC];

    // 4D array to store the minimum number of digs required to reach a specific state
    // dst[row][current_column][left_bound][right_bound]
    static int[][][][] dst = new int[MAXRC][MAXRC][MAXRC][MAXRC];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Number of test cases. Currently set to 1.
        int nt = 1;
        for (int it = 0; it < nt; it++) {
            load(sc);   // Load the cave grid and parameters
            solve();     // Solve the problem for the loaded grid
            System.out.println(); // Print a newline after each test case
        }
        sc.close(); // Close the scanner
    }

    // Method to load the input data
    static void load(Scanner sc) {
        // Read the number of rows, columns, and maximum falling distance
        r = sc.nextInt();
        c = sc.nextInt();
        f = sc.nextInt();

        // Read the cave grid row by row
        for (int i = 0; i < r; i++) {
            String line = sc.next();
            for (int j = 0; j < c; j++) {
                ma[i][j] = line.charAt(j); // Populate the grid with '.' or '#'
            }
        }

        // Set the ground row (r-th row) as solid rock ('#') to represent the bottom of the cave
        for (int j = 0; j < c; j++) {
            ma[r][j] = '#';
        }
    }

    // Method to solve the cave navigation problem
    static void solve() {
        // Initialize the dst array with MANY (infinite digs) to represent unreachable states
        for (int i = 0; i < MAXRC; i++) {
            for (int j = 0; j < MAXRC; j++) {
                for (int k = 0; k < MAXRC; k++) {
                    Arrays.fill(dst[i][j][k], MANY);
                }
            }
        }

        // Starting position: top-left corner with 0 digs and bounds set to the starting column
        dst[0][0][0][0] = 0;

        // Iterate through each row up to the second-to-last row
        for (int i = 0; i < r - 1; i++) {
            // Iterate through each column in the current row
            for (int j = 0; j < c; j++) {
                // Iterate through all possible left bounds
                for (int j1 = 0; j1 < c; j1++) {
                    // Iterate through all possible right bounds starting from j1
                    for (int j2 = j1; j2 < c; j2++) {
                        // If the current state is unreachable, skip it
                        if (dst[i][j][j1][j2] == MANY) continue;

                        // Current number of digs required to reach this state
                        int cr = dst[i][j][j1][j2];

                        // Initialize left and right query bounds to the current column
                        int lq = j, rq = j;

                        // Explore moving left from the current column
                        for (int q = j - 1; q >= 0; q--) {
                            // If a solid rock is encountered outside the current bounds, stop searching left
                            if (ma[i][q] == '#' && (q < j1 || q > j2)) break;

                            // If the cell below is empty, attempt to fall down
                            if (ma[i + 1][q] == '.') {
                                int nr = i + 1;

                                // Continue falling straight down until hitting solid rock or the ground
                                while (ma[nr + 1][q] == '.') {
                                    nr++;
                                }

                                // Check if the falling distance does not exceed the maximum allowed
                                if (nr - i <= f && dst[nr][q][q][q] > cr) {
                                    dst[nr][q][q][q] = cr; // Update the destination state with the current digs
                                }
                                break; // Stop searching further left after a fall
                            }
                            lq = q; // Update the left query bound
                        }

                        // Explore moving right from the current column
                        for (int q = j + 1; q < c; q++) {
                            // If a solid rock is encountered outside the current bounds, stop searching right
                            if (ma[i][q] == '#' && (q < j1 || q > j2)) break;

                            // If the cell below is empty, attempt to fall down
                            if (ma[i + 1][q] == '.') {
                                int nr = i + 1;

                                // Continue falling straight down until hitting solid rock or the ground
                                while (ma[nr + 1][q] == '.') {
                                    nr++;
                                }

                                // Check if the falling distance does not exceed the maximum allowed
                                if (nr - i <= f && dst[nr][q][q][q] > cr) {
                                    dst[nr][q][q][q] = cr; // Update the destination state with the current digs
                                }
                                break; // Stop searching further right after a fall
                            }
                            rq = q; // Update the right query bound
                        }

                        // Iterate through all possible new left and right bounds within [lq, rq]
                        for (int nj1 = lq; nj1 <= rq; nj1++) {
                            for (int nj2 = nj1; nj2 <= rq; nj2++) {
                                // Calculate the number of new digs required for the current bounds
                                int nextr = cr + (nj2 - nj1 + 1);

                                // If the new left bound is different from the original left query bound
                                if (nj1 != lq) {
                                    int ni = i + 1;

                                    // Continue falling straight down from the new left bound
                                    while (ma[ni + 1][nj1] == '.') {
                                        ni++;
                                    }

                                    // Check if the falling distance does not exceed the maximum allowed
                                    if (ni - i <= f) {
                                        if (ni == i + 1 && dst[ni][nj1][nj1][nj2] > nextr) {
                                            // Update the destination state if it's a direct fall
                                            dst[ni][nj1][nj1][nj2] = nextr;
                                        } else if (dst[ni][nj1][nj1][nj1] > nextr) {
                                            // Otherwise, update the destination state with the new digs
                                            dst[ni][nj1][nj1][nj1] = nextr;
                                        }
                                    }
                                }

                                // If the new right bound is different from the original right query bound
                                if (nj2 != rq) {
                                    int ni = i + 1;

                                    // Continue falling straight down from the new right bound
                                    while (ma[ni + 1][nj2] == '.') {
                                        ni++;
                                    }

                                    // Check if the falling distance does not exceed the maximum allowed
                                    if (ni - i <= f) {
                                        if (ni == i + 1 && dst[ni][nj2][nj1][nj2] > nextr) {
                                            // Update the destination state if it's a direct fall
                                            dst[ni][nj2][nj1][nj2] = nextr;
                                        } else if (dst[ni][nj2][nj2][nj2] > nextr) {
                                            // Otherwise, update the destination state with the new digs
                                            dst[ni][nj2][nj2][nj2] = nextr;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Variable to store the minimum number of digs required to reach the bottom
        int mans = MANY;

        // Iterate through all possible states in the last row to find the minimum digs
        for (int j = 0; j < c; j++) {
            for (int j1 = 0; j1 < c; j1++) {
                for (int j2 = j1; j2 < c; j2++) {
                    mans = Math.min(mans, dst[r - 1][j][j1][j2]);
                }
            }
        }

        // If the minimum number of digs is still MANY, it means reaching the bottom is impossible
        if (mans == MANY) {
            System.out.print("No");
        } else {
            // Otherwise, print "Yes" followed by the minimum number of digs required
            System.out.print("Yes " + mans);
        }
    }
}