import java.util.Scanner;

public class InputValidator {
    private static Scanner scanner = new Scanner(System.in);

    public static int getIntInput() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            UserInterface.printError("Invalid input. Please enter a number.");
            return -1;
        }
    }

    public static double getDoubleInput() {
        try {
            String input = scanner.nextLine();
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            UserInterface.printError("Invalid input. Please enter a valid number.");
            return -1;
        }
    }

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

    public static String getEmail() {
        String email;
        boolean validEmail = false;

        do {
            email = getStringInput("Enter email (or 'b' to go back): ", false);

            if (email.equalsIgnoreCase("b")) {
                return "b";
            }

            if (email.length() < 4) {
                UserInterface.printError("Email must be at least 4 characters long. Please try again.");
                continue;
            }

            if (!email.contains("@") || !email.matches(".*@.*\\..*")) {
                UserInterface.printError("Email must contain '@' and a domain (e.g., @something.com). Please try again.");
                continue;
            }

            validEmail = true;
        } while (!validEmail);

        return email;
    }

    public static String getPhoneNumber() {
        String phoneNumber;
        boolean validPhone = false;

        do {
            phoneNumber = getStringInput("Enter phone number (or 'b' to go back): ", false);

            if (phoneNumber.equalsIgnoreCase("b")) {
                return "b";
            }

            // Remove any non-digit characters for validation
            String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");

            if (digitsOnly.length() != 11) {
                UserInterface.printError("Phone number must be exactly 11 digits long. Please try again.");
                continue;
            }

            phoneNumber = digitsOnly;
            validPhone = true;
        } while (!validPhone);

        return phoneNumber;
    }
}