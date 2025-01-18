import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// The solution starts by understanding the crime scene grid setup.
// Each grid cell can hold items, and we are tasked with finding how many items are present in specific cells based on given data.
// First, we read the dimensions of the grid and the number of items found.
// Then, for each item's location, we record its coordinates and count how many items are in that specific cell.
// This is done by treating each cell’s coordinates as a unique identifier.
// After this, we read a list of cells we need to query and look up how many items are present in each of these cells.
// If a cell has no recorded items, it means zero items were found there.
// Finally, we sum up the item counts for all queried cells and print the total.
// This straightforward process mirrors how the problem is presented: mapping input coordinates to cell counts and then querying the required cells to compute the result.
public class GridItemCounter {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input the dimensions of the grid (length X and width Y)
        int X = scanner.nextInt();
        int Y = scanner.nextInt();

        // Input the number of items found at the crime scene
        int M = scanner.nextInt();

        // Use a Map to store the count of items in each grid cell
        Map<String, Integer> itemCount = new HashMap<>();

        // Read the coordinates of the grid cells where items were found
        for (int i = 0; i < M; i++) {
            int x = scanner.nextInt(); // X-coordinate of the item
            int y = scanner.nextInt(); // Y-coordinate of the item
            String key = x + "," + y; // Use "x,y" as the unique key for the grid cell

            // Increment the count for this cell (default is 0 if not already in the Map)
            itemCount.put(key, itemCount.getOrDefault(key, 0) + 1);
        }

        // Input the number of cells to query for the total item count
        int N = scanner.nextInt();

        int totalItems = 0; // Variable to store the total number of items found in queried cells

        // Read the coordinates of the cells to query and calculate the total items
        for (int i = 0; i < N; i++) {
            int x = scanner.nextInt(); // X-coordinate of the queried cell
            int y = scanner.nextInt(); // Y-coordinate of the queried cell
            String key = x + "," + y; // Use "x,y" as the unique key for querying the Map

            // Add the count of items in this cell to the total (default is 0 if not in the Map)
            totalItems += itemCount.getOrDefault(key, 0);
        }

        // Output the total number of items found in the queried cells
        System.out.println(totalItems);

        // Close the scanner to free resources
        scanner.close();
    }
}
