public class UserInterface {
    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String BG_BLUE = "\u001B[44m";
    private static final String BRIGHT_WHITE = "\u001B[97m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String WHITE = "\u001B[37m";
    private static final String BRIGHT_GREEN = "\u001B[92m";
    private static final String BRIGHT_RED = "\u001B[91m";
    private static final String BRIGHT_CYAN = "\u001B[96m";

    public static void printHeader(String text) {
        System.out.println("\n" + BG_BLUE + BOLD + BRIGHT_WHITE + " " + text + " " + RESET);
    }

    public static void printSubHeader(String text) {
        System.out.println("\n" + BOLD + CYAN + text + RESET);
    }

    public static void printMenuOption(int number, String text) {
        System.out.println(YELLOW + number + ". " + WHITE + text + RESET);
    }

    public static void printPrompt(String text) {
        System.out.print(BRIGHT_GREEN + text + RESET);
    }

    public static void printSuccess(String text) {
        System.out.println(BOLD + BRIGHT_GREEN + text + RESET);
    }

    public static void printError(String text) {
        System.out.println(BRIGHT_RED + text + RESET);
    }

    public static void printInfo(String text) {
        System.out.println(BRIGHT_CYAN + text + RESET);
    }

    public static void printWelcomeBanner() {
        System.out.println(BOLD + BRIGHT_WHITE + "=========================================================================================================" + RESET);
        System.out.println(BOLD + BRIGHT_WHITE + "\t\t\t\t\t\t\t\t\t𝓦𝓮𝓵𝓬𝓸𝓶𝓮 𝓽𝓸 Wingman!" + RESET);
        System.out.println(BOLD + BRIGHT_WHITE + "\t\t\t\t\t\t\t\tMore Than a Jet. Your Travel Buddy." + RESET);
        System.out.println(BOLD + BRIGHT_WHITE + "=========================================================================================================" + RESET);
    }
}