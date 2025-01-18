import java.io.*;
import java.util.*;

// To solve this problem, we need to minimize the final score of an array by repeatedly replacing groups of K consecutive elements with their bitwise OR value until the array's length is less than K.
// The key is to find the optimal sequence of groups to minimize the final sum of the remaining elements.
// Each replacement reduces the array's size, and the challenge lies in deciding which groups to replace at each step.
// Since the bitwise OR operation is associative, the resulting value of each group depends only on the elements within that group.
// We can use dynamic programming to track the minimum possible score for different stages of the array and optimize the calculations by grouping values with the same bitwise OR results.
public class Main {
    // Constants to define the maximum size of the array and bit length
    static final int MAXN = 400005; // Maximum number of elements in the array
    static final int MAXL = 31;     // Maximum number of bits for OR operation

    static int l;                   // Length of subarray to process (K-1)
    static long[] dp;               // Dynamic programming array for minimum score
    static long[][] sprs;           // Sparse table for range minimum queries
    static int[] val;               // Precomputed log2 values for sparse table indices
    static int[][] bit;             // Stores bit positions for OR calculation
    static int[][] pos;             // Stores positions associated with bit calculations

    // Function to query the minimum value in a range using the sparse table
    static long query(int p, int q) {
        int k = val[q - p]; // Determine the power of 2 range using precomputed values
        return Math.min(sprs[q][k], sprs[p + ((1 << k) - 1) * l][k]);
    }

    // Main function to solve the problem
    static long Resi(int N, int[] A, int K) {
        // Initialize arrays
        dp = new long[MAXN];
        sprs = new long[MAXN][21];
        val = new int[MAXN];
        bit = new int[2][MAXL];
        pos = new int[2][MAXL];

        l = K - 1; // Subarray length to reduce in each step

        // Precompute logarithmic values for efficient sparse table access
        int c = 0;
        for (int j = 1; j * l < MAXN; j++) {
            if (2 * (1 << c) <= j) c++;
            val[j * l] = c;
        }

        // Initialize bit positions for OR calculation
        for (int j = 0; j < MAXL; j++) {
            bit[0][j] = j;
            bit[1][j] = j;
        }

        int g = 0; // Bit array toggle

        // Iterate through each element in the array
        for (int i = 1; i <= N; i++) {
            int count = 0; // Count of significant bits

            // Update positions and bits for the current element
            for (int j = 0; j < MAXL; j++) {
                if ((A[i] & (1 << bit[g][j])) != 0) {
                    pos[g ^ 1][count] = i;
                    bit[g ^ 1][count] = bit[g][j];
                    count++;
                }
            }

            // Update remaining bit positions
            for (int j = 0; j < MAXL; j++) {
                if ((A[i] & (1 << bit[g][j])) == 0) {
                    pos[g ^ 1][count] = pos[g][j];
                    bit[g ^ 1][count] = bit[g][j];
                    count++;
                }
            }

            g ^= 1; // Toggle bit array
            dp[i] = A[i] + dp[i - 1]; // Default DP value is the current element plus previous

            int o = 0; // OR result accumulator
            c = (i - 1); // Last processed index
            int fst = c - l * (c / l); // First index in the current group

            // Calculate minimum DP value for the current element
            for (int j = 0; j < MAXL; j++) {
                if (pos[g][j] == 0) break;

                if (pos[g][j] == i || (j != 0 && pos[g][j] == pos[g][j - 1])) {
                    o |= (1 << bit[g][j]); // Update OR result
                    continue;
                }

                int start = c - l * ((c - (pos[g][j])) / l);
                dp[i] = Math.min(dp[i], query(start, c) + o); // Update DP with minimum value
                o |= (1 << bit[g][j]); // Include the current bit in OR result

                // If no improvement, terminate early
                if (query(fst, start) + o > dp[i]) break;
            }

            dp[i] = Math.min(dp[i], query(fst, c) + o); // Final DP update for current index
            sprs[i][0] = dp[i]; // Store DP value in sparse table

            // Build sparse table for range minimum queries
            for (int j = 1; i - ((1 << j) - 1) * l >= 0; j++) {
                sprs[i][j] = Math.min(sprs[i][j - 1], sprs[i - ((1 << (j - 1))) * l][j - 1]);
            }
        }

        return dp[N]; // Return the minimum possible score
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken()); // Number of elements in the array
        int K = Integer.parseInt(st.nextToken()); // Group size for OR operation

        int[] A = new int[MAXN];
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= N; i++) {
            A[i] = Integer.parseInt(st.nextToken()); // Read array elements
        }

        long result = Resi(N, A, K); // Solve the problem
        System.out.println(result); // Output the result
    }
}

