import java.util.*;

// The problem revolves around matching the knights to the dragon heads in a way that ensures all heads are chopped off while minimizing the cost.
// Each dragon head has a size, and each knight has a height.
// A knight can only chop off a head if their height is at least equal to the size of the head.
// Additionally, the cost of hiring a knight is determined by their height.
// The strategy is to first sort both the dragon heads and the knights in ascending order.
// Then, we assign the smallest available knight who can handle a specific head to minimize costs.
// If at any point there are no knights tall enough for a head, the task becomes impossible, and the kingdom is doomed.
// Otherwise, we sum up the costs for all chosen knights and output the total.
public class LoowaterDragon {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read the number of dragon heads (H) and the number of knights (K)
        int H = scanner.nextInt(); // Number of dragon heads
        int K = scanner.nextInt(); // Number of knights

        // Create arrays to store the diameters of the dragon heads and the heights of the knights
        int[] heads = new int[H];
        int[] knights = new int[K];

        // Input the diameters of the dragon heads
        for (int i = 0; i < H; i++) {
            heads[i] = scanner.nextInt();
        }

        // Input the heights of the knights
        for (int i = 0; i < K; i++) {
            knights[i] = scanner.nextInt();
        }

        // Sort the dragon heads and knights in ascending order
        Arrays.sort(heads);
        Arrays.sort(knights);

        // Initialize variables
        int cost = 0; // Total gold cost for slaying the dragon
        int knightIndex = 0; // Index to track the current knight
        boolean doomed = false; // Flag to indicate if slaying the dragon is impossible

        // Iterate through each dragon head
        for (int i = 0; i < H; i++) {
            // Find the first knight tall enough to chop off the current head
            while (knightIndex < K && knights[knightIndex] < heads[i]) {
                knightIndex++; // Move to the next knight
            }

            // Check if all knights have been used up
            if (knightIndex == K) {
                doomed = true; // Not enough knights to slay the dragon
                break;
            }

            // Pay the knight and add the cost
            cost += knights[knightIndex];
            knightIndex++; // Move to the next knight for the next head
        }

        // Output the result
        if (doomed) {
            System.out.println("Loowater is doomed!"); // Not enough knights
        } else {
            System.out.println(cost); // Minimum cost to slay the dragon
        }

        scanner.close(); // Close the scanner to free resources
    }
}
