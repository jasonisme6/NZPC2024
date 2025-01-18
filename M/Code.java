import java.util.*;

// The problem asks if it's possible to travel from the top-left corner of a grid to the bottom-right corner while visiting exactly K music festivals along the way.
// The grid contains towns, some of which host music festivals.
// You can only move right or down, and the goal is to check if there is a path that visits exactly K festivals.
// To solve this, we use a strategy to keep track of all possible numbers of festivals visited at each point in the grid.
// Starting from the top-left corner, we calculate the possible visit counts as we move across the grid.
// At each step, we account for whether the current town hosts a festival and update our counts accordingly.
// By the time we reach the bottom-right corner, we simply check if exactly K festivals can be visited.
// The approach ensures we explore all valid paths efficiently without retracing steps unnecessarily.
public class Main {
    public static void main(String[] args) {
        // Read input using Scanner
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt(); // Number of rows
        int M = scanner.nextInt(); // Number of columns
        int K = scanner.nextInt(); // Target number of festivals to visit
        scanner.nextLine(); // Consume the newline character

        // Initialize the grid: festival[i][j] indicates if town (i, j) hosts a festival
        boolean[][] festival = new boolean[N][M];
        for (int i = 0; i < N; i++) {
            String line = scanner.nextLine();
            for (int j = 0; j < M; j++) {
                festival[i][j] = (line.charAt(j) == '1');
            }
        }

        // Use dynamic programming with space optimization
        // dp[j] stores the reachable states for column j
        BitSet[] dp = new BitSet[M];       // Current row states
        BitSet[] nextDp = new BitSet[M];  // Next row states
        for (int j = 0; j < M; j++) {
            dp[j] = new BitSet(K + 1);
            nextDp[j] = new BitSet(K + 1);
        }

        // Initialize the starting point (0, 0)
        if (festival[0][0]) {
            dp[0].set(1); // Starting at a festival, visit count is 1
        } else {
            dp[0].set(0); // Starting at a non-festival town, visit count is 0
        }

        // Process the first row: Can only move right
        for (int j = 1; j < M; j++) {
            dp[j].or(dp[j - 1]); // Transition from the previous column
            if (festival[0][j]) {
                dp[j] = shiftLeft(dp[j], K); // Increment festival visit count
            }
        }

        // Process rows from the second row to the last row
        for (int i = 1; i < N; i++) {
            // Clear the next row states
            for (int j = 0; j < M; j++) {
                nextDp[j].clear();
            }

            // Process the first column of the current row
            nextDp[0].or(dp[0]); // Transition from the column above
            if (festival[i][0]) {
                nextDp[0] = shiftLeft(nextDp[0], K);
            }

            // Process the rest of the columns in the current row
            for (int j = 1; j < M; j++) {
                // Merge reachable states from above and left
                nextDp[j].or(dp[j]);        // Transition from the above row
                nextDp[j].or(nextDp[j - 1]); // Transition from the left column

                // Increment visit count if the current town hosts a festival
                if (festival[i][j]) {
                    nextDp[j] = shiftLeft(nextDp[j], K);
                }
            }

            // Roll over the dp arrays for the next iteration
            BitSet[] temp = dp;
            dp = nextDp;
            nextDp = temp;
        }

        // Check if we can reach the bottom-right corner with exactly K festivals visited
        System.out.println(dp[M - 1].get(K) ? 1 : 0);
    }

    /**
     * Shifts all bits in the given BitSet to the left by 1 position.
     * This effectively increases the festival visit count by 1.
     * Any bits exceeding the range [0, K] are discarded.
     *
     * @param bs The input BitSet representing reachable states.
     * @param K  The maximum number of festivals allowed.
     * @return   A new BitSet with all bits shifted left by 1.
     */
    private static BitSet shiftLeft(BitSet bs, int K) {
        BitSet res = new BitSet(K + 1);
        // Iterate through all set bits and shift them left by 1
        for (int x = bs.nextSetBit(0); x >= 0; x = bs.nextSetBit(x + 1)) {
            if (x + 1 <= K) { // Ensure the shifted value does not exceed K
                res.set(x + 1);
            }
        }
        return res;
    }
}
