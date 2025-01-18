import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// The core of this problem is to generate a check digit for a given sequence of numbers (student ID) to ensure its validity and to detect any potential tampering.
// The process to generate the check digit is straightforward: starting from the rightmost digit of the ID, multiply each digit by an incrementing weight, sum up the results, then take the remainder when divided by 11.
// Based on this remainder, the check digit is determined.
// If the result is between 1 and 9, it is appended to the ID; if it is 11, a 0 is appended; and if it is 10, the ID is considered invalid, and a temporary ID must be assigned manually.
// To verify the validity of a new ID with the check digit, a similar process is used, but with a different weighting scheme, checking if the total sum is divisible by 11.
// The program's logic involves reading a series of IDs from user input, calculating the check digit according to the given rules, and outputting the result with either the new ID or a rejection message.
public class IDValidator {

    // Calculate the weighted sum of the digits in the ID
    public static int calculateWeightedSum(String id) {
        int sum = 0;
        int weight = 2; // Initial weight starts at 2
        // Traverse the ID from right to left
        for (int i = id.length() - 1; i >= 0; --i) {
            // Convert the character to a number and multiply by the weight
            sum += (id.charAt(i) - '0') * weight;
            weight++; // Increment the weight for the next digit
        }
        return sum;
    }

    // Generate the check digit for the given ID
    public static char generateCheckDigit(String id) {
        int sum = calculateWeightedSum(id); // Calculate the weighted sum
        int remainder = sum % 11;          // Find the remainder when divided by 11
        int checkDigit = 11 - remainder;   // Calculate the check digit

        // Determine the check digit based on the calculated value
        if (checkDigit == 10) return 'X';  // If the check digit is 10, reject the ID
        if (checkDigit == 11) return '0';  // If the check digit is 11, append '0'
        return (char) ('0' + checkDigit);  // For other cases, return the numeric check digit
    }

    // Validate whether the ID with its check digit is valid
    public static boolean validateID(String id) {
        int sum = calculateWeightedSum(id); // Calculate the weighted sum
        return (sum % 11 == 0);             // Valid if the sum is divisible by 11
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> inputIDs = new ArrayList<>(); // Store all input IDs

        // Read input IDs until '0' is encountered
        while (true) {
            String id = scanner.next();
            if (id.equals("0")) break; // Stop when the input is '0'
            inputIDs.add(id);         // Add the ID to the list
        }

        // Process each input ID
        for (String id : inputIDs) {
            // Generate the check digit for the ID
            char checkDigit = generateCheckDigit(id);

            // Output the result for the ID
            System.out.print(id + " -> ");
            if (checkDigit == 'X') {
                System.out.println("rejected"); // If the ID is rejected
            } else {
                String newID = id + checkDigit; // Append the check digit to the ID
                System.out.println(newID);      // Print the new ID with the check digit
            }
        }

        scanner.close(); // Close the scanner to release resources
    }
}
