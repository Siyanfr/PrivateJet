import java.util.Scanner;

public class InputValidator {
    private static Scanner scanner = new Scanner(System.in);

    // Method to get an integer input from the user
    public static int getIntInput() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            UserInterface.printError("Invalid input. Please enter a number.");
            return -1;
        }
    }

    // Method to get a double input from the user
    public static double getDoubleInput() {
        try {
            String input = scanner.nextLine();
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            UserInterface.printError("Invalid input. Please enter a valid number.");
            return -1;
        }
    }

    // Method to get a string input with an optional empty check
    public static String getStringInput(String prompt, boolean allowEmpty) {
        String input;
        do {
            UserInterface.printPrompt(prompt);
            input = scanner.nextLine().trim();
            if (!allowEmpty && input.isEmpty()) {
                UserInterface.printError("Input cannot be empty. Please try again.");
            } else {
                break;
            }
        } while (true);

        return input;
    }

    // Method to get a valid email input from the user
    public static String getEmail() {
        String email;
        boolean validEmail = false;

        do {
            email = getStringInput("Enter email (or 'b' to go back): ", false);

            if (email.equalsIgnoreCase("b")) {
                return "b";
            }

            // Validate email length
            if (email.length() < 4) {
                UserInterface.printError("Email must be at least 4 characters long. Please try again.");
                continue;
            }

            // Validate email format
            if (!email.contains("@") || !email.matches(".*@.*\\..*")) {
                UserInterface.printError("Email must contain '@' and a domain (e.g., @something.com). Please try again."); // Error message for invalid format
                continue;
            }

            validEmail = true;
        } while (!validEmail);

        return email;
    }

    // Method to get a valid phone number input from the user
    public static String getPhoneNumber() {
        String phoneNumber;
        boolean validPhone = false;

        do {
            phoneNumber = getStringInput("Enter phone number (or 'b' to go back): ", false);

            if (phoneNumber.equalsIgnoreCase("b")) {
                return "b";
            }

            String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");

            // Validate phone number length
            if (digitsOnly.length() != 11) {
                UserInterface.printError("Phone number must be exactly 11 digits long. Please try again.");
                continue;
            }

            phoneNumber = digitsOnly;
            validPhone = true;
        } while (!validPhone);

        return phoneNumber;
    }

    // Method to get a yes/no input from the user
    public static boolean getYesNoInput(String prompt) {
        String input;
        do {
            UserInterface.printPrompt(prompt + " (y/n): ");
            input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                UserInterface.printError("Invalid input. Please enter 'y' or 'n'.");
            }
        } while (true);
    }

    // Method to get a string input that does not accept integers
    public static String doNotAcceptInt(String prompt, boolean allowEmpty) {
        String input;
        do {
            UserInterface.printPrompt(prompt);
            input = scanner.nextLine().trim();
            if (!allowEmpty && input.isEmpty()) {
                UserInterface.printError("Input cannot be empty. Please try again.");
            } else if (input.matches(".*\\d.*")) {
                UserInterface.printError("Input cannot contain numbers. Please try again.");
            } else {
                break;
            }
        } while (true);

        return input;
    }
}
