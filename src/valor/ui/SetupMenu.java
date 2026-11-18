package valor.ui;

import java.util.ArrayList;
import java.util.List;

// set up menu greets the user, displays the game being played
// how to win, and asks them to choose 3 heroes.
public class SetupMenu {
    public static final int TEAM_SIZE = 3; // one hero per lane

    private static final String WELCOME =
            "==============================================\n"
            + "        Welcome to Legends of Valor!\n"
            + "==============================================\n"
            + "Monsters are marching on your Nexus. Lead a team of\n"
            + TEAM_SIZE + " heroes, one in each lane, and fight your way north.\n"
            + "\n"
            + "HOW TO WIN\n"
            + "  Get any hero into the monsters' Nexus (the top row).\n"
            + "HOW TO LOSE\n"
            + "  Any monster reaches your Nexus (the bottom row).\n"
            + "\n"
            + "Type Q at any prompt to quit.\n";

    private final Console console;

    public SetupMenu(Console console) {
        this.console = console;
    }

    public void greet() {
        System.out.println(WELCOME);
    }

    // TODO: change String to Hero once heroes are added
    public List<String> chooseHeroes(List<String> allHeroes) {
        List<String> available = new ArrayList<>(allHeroes);
        List<String> chosen = new ArrayList<>();
        while (chosen.size() < TEAM_SIZE) {
            String hero = console.choose(
                    "\nChoose hero " + (chosen.size() + 1) + " of " + TEAM_SIZE + ":",
                    available, name -> name);
            available.remove(hero); // a hero can only be picked once
            chosen.add(hero);
            System.out.println(hero + " joins your team!");
        }
        return chosen;
    }
}
