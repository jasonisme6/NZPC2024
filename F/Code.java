import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// The solution involves reading a grid of characters and separating the valid hexadecimal numbers (made up of 0-9 and A-F) from everything else.
// The program goes through the grid row by row, checking each part to see if it is a hexadecimal number.
// If it is, the number is stored and later converted into a regular decimal number.
// If it’s not, the characters are collected as non-hexadecimal.
// At the end, all the non-hexadecimal characters are printed first, followed by the decimal values of the hexadecimal numbers, keeping everything in the order they appeared in the grid.
public class HexProcessor {

    // Check if a character is a valid hexadecimal digit
    public static boolean isHexadecimal(char ch) {
        // Hexadecimal digits include 0-9 and A-F
        return Character.isDigit(ch) || (ch >= 'A' && ch <= 'F');
    }

    // Convert a hexadecimal string to its decimal equivalent
    public static int hexToDecimal(String hex) {
        int result = 0; // Initialize the decimal result
        for (char ch : hex.toCharArray()) {
            result *= 16; // Multiply by the base (16)
            if (Character.isDigit(ch)) {
                result += ch - '0'; // Convert '0'-'9' to numeric values
            } else if (ch >= 'A' && ch <= 'F') {
                result += ch - 'A' + 10; // Convert 'A'-'F' to 10-15
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read the dimension of the grid (N x N)
        int N = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character after reading N

        // List to store the input grid
        List<String> grid = new ArrayList<>();

        // Read the grid, line by line
        for (int i = 0; i < N; i++) {
            grid.add(scanner.nextLine());
        }

        StringBuilder nonHex = new StringBuilder(); // Stores all non-hexadecimal characters
        List<Integer> hexValues = new ArrayList<>(); // Stores decimal values of hexadecimal numbers

        // Process each row of the grid
        for (int i = 0; i < N; i++) {
            // Split the row into tokens based on spaces
            String[] tokens = grid.get(i).split("\\s+");
            StringBuilder hexNum = new StringBuilder(); // Temporary buffer for hexadecimal numbers

            for (String token : tokens) {
                boolean validHex = true; // Flag to determine if the token is hexadecimal

                // Check if the token consists only of hexadecimal digits
                for (char ch : token.toCharArray()) {
                    if (!isHexadecimal(ch)) {
                        validHex = false;
                        break;
                    }
                }

                if (validHex) {
                    // If the token is a hexadecimal number, append it to the buffer
                    hexNum.append(token);
                } else {
                    // If a non-hexadecimal character is encountered:
                    // 1. Convert and store any buffered hexadecimal number
                    if (hexNum.length() > 0) {
                        hexValues.add(hexToDecimal(hexNum.toString()));
                        hexNum.setLength(0); // Clear the buffer
                    }
                    // 2. Add the non-hexadecimal token to the result
                    nonHex.append(token);
                }
            }

            // At the end of the row, handle any remaining hexadecimal number
            if (hexNum.length() > 0) {
                hexValues.add(hexToDecimal(hexNum.toString()));
            }
        }

        // Output the non-hexadecimal characters (if any)
        if (nonHex.length() > 0) {
            System.out.println(nonHex);
        }

        // Output the decimal equivalents of hexadecimal numbers
        for (int val : hexValues) {
            System.out.println(val);
        }

        scanner.close(); // Close the scanner to release resources
    }
}