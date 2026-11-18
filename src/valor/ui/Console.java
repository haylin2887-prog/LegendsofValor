package valor.ui;

import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

// all keyboard input goes through here, so every prompt handles bad input and Q the same way
public class Console {
    private final Scanner input;

    public Console() {
        this.input = new Scanner(System.in);
    }

    // asks until the player types something. Q quits the game from any prompt
    public String readLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readRaw();
            if (line.equalsIgnoreCase("q")) {
                quit();
            } else if (!line.isEmpty()) {
                return line;
            }
        }
    }

    // asks until the player types a whole number from min to max
    public int readNumber(String prompt, int min, int max) {
        while (true) {
            String line = readLine(prompt);
            try {
                int number = Integer.parseInt(line);
                if (number >= min && number <= max) {
                    return number;
                }
            } catch (NumberFormatException e) {
                // handled by the message below
            }
            System.out.println("Please enter a number from " + min + " to " + max + ".");
        }
    }

    // shows a numbered list and returns the option the player picks.
    // label turns each option into the text shown for it
    public <T> T choose(String title, List<T> options, Function<T, String> label) {
        System.out.println(title);
        for (int i = 0; i < options.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + label.apply(options.get(i)));
        }
        int choice = readNumber("> ", 1, options.size());
        return options.get(choice - 1);
    }

    // running out of input counts as quitting, so the game can't get stuck waiting
    private String readRaw() {
        if (!input.hasNextLine()) {
            quit();
        }
        return input.nextLine().trim();
    }

    private void quit() {
        System.out.println("\nYou left the game. Thanks for playing!");
        System.exit(0);
    }
}
