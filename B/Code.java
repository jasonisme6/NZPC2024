import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

// The problem requires calculating the savings for customers under a "buy X, get 1 free" discount scheme.
// First, we take the product name and its price in dollars and cents, which are converted into a single value in cents for easier calculations.
// The discount rule specifies how many items must be bought to get one free.
// For each given quantity of items a customer wants to purchase, we calculate how many items they have to pay for and how many they get for free.
// Using this, we compute the total cost with and without the discount and find the savings.
// Finally, we format and output the results, showing the number of items bought, paid for, and received for free, along with the total savings in dollars and cents.
public class DiscountCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input product name (can contain spaces)
        String productName = scanner.nextLine();

        // Input price information: PD (dollars) and PC (cents)
        int PD = scanner.nextInt(); // Dollar part of the price
        int PC = scanner.nextInt(); // Cent part of the price

        // Input the discount rule: Buy B items, get 1 free
        int B = scanner.nextInt();

        // Input the number of test cases (E)
        int E = scanner.nextInt();

        // Input the quantities for each test case
        List<Integer> quantities = new ArrayList<>();
        for (int i = 0; i < E; i++) {
            quantities.add(scanner.nextInt());
        }

        // Calculate the price per item in cents
        int pricePerItem = PD * 100 + PC;

        // Output the product name
        System.out.println(productName);

        // Process each quantity and calculate savings
        for (int qty : quantities) {
            // Calculate the number of free items and paid items
            int freeItems = qty / (B + 1);        // Free items based on the discount rule
            int paidItems = qty - freeItems;     // Items that need to be paid for

            // Calculate the total cost without discount
            int totalCostWithoutDiscount = qty * pricePerItem;

            // Calculate the total cost with discount
            int totalCostWithDiscount = paidItems * pricePerItem;

            // Calculate the total savings in cents
            int savings = totalCostWithoutDiscount - totalCostWithDiscount;

            // Convert savings to dollars and cents
            int dollars = savings / 100;
            int cents = savings % 100;

            // Output the result for the current quantity
            System.out.printf(
                    "Buy %d, pay for %d, get %d free. Save $%d.%02d.\n",
                    qty, paidItems, freeItems, dollars, cents
            );
        }

        // Close the scanner to avoid resource leaks
        scanner.close();
    }
}
