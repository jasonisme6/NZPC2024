import java.util.*;

// The problem is about identifying microspikes in power usage during a simulation.
// A microspike happens when the total power consumption rises above a certain threshold (M) for a short period of time, specifically between 1 and S seconds.
// At the start of the simulation, all appliances are off, and their power levels change over time.
// Each change is recorded with details about the appliance, the time since its last change, and the power adjustment.
// However, these records are not ordered by time across appliances, so we need to calculate the total power at each second during the simulation.
// Once we know the power levels for each second, we look for periods where the power goes above the threshold, stays above for a valid duration, and then drops back to or below the threshold.
// The goal is to count how many such periods (microspikes) occur during the simulation.
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Read the basic input parameters
        long T = in.nextLong(); // Total simulation time in seconds
        long M = in.nextLong(); // Power threshold for microspikes
        long S = in.nextLong(); // Maximum duration for microspikes

        // Initialize the difference array to record power changes over time
        long[] diff = new long[(int) T + 1];
        // Map to store the last recorded time for each appliance
        Map<Integer, Long> lastTime = new HashMap<>();

        // Read appliance records until the end marker is encountered
        while (true) {
            int a = in.nextInt(); // Appliance number
            long t = in.nextLong(); // Time since last change for this appliance
            long p = in.nextLong(); // Power level change

            // Break the loop when the end marker (0, 0, 0) is reached
            if (a == 0 && t == 0 && p == 0) break;

            // Calculate the absolute time for this appliance's event
            long time = lastTime.getOrDefault(a, 0L) + t;

            // Update the difference array if the time is within the simulation period
            if (time <= T) diff[(int) time] += p;

            // Record the updated time for this appliance
            lastTime.put(a, time);
        }

        // Compute the prefix sum to determine actual power levels at each time
        long[] power = new long[(int) T + 1];
        power[0] = diff[0];
        for (int i = 1; i <= T; i++) {
            power[i] = power[i - 1] + diff[i];
        }

        // Identify and count microspikes
        int spikes = 0; // Counter for microspikes
        long start = -1; // Start time of a potential spike
        boolean overThreshold = false; // Flag to track if the power is above the threshold

        for (int i = 0; i <= T; i++) {
            // Check if power exceeds the threshold and was not already in a spike
            if (power[i] > M && !overThreshold) {
                start = i; // Record the start time of the spike
                overThreshold = true; // Mark that the power is above the threshold
            }
            // Check if power falls back to the threshold or below after exceeding it
            else if (power[i] <= M && overThreshold) {
                // Calculate the spike duration and check if it meets the criteria
                if (i - start >= 1 && i - start <= S) {
                    spikes++; // Increment the spike counter
                }
                overThreshold = false; // Reset the threshold flag
            }
        }

        // Output the total number of microspikes observed
        System.out.println(spikes);
        in.close(); // Close the scanner to release resources
    }
}
