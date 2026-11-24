package valor.ui;

import java.util.ArrayList;
import java.util.List;
import valor.Characters.Hero;
import valor.Characters.Party;

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

    public Party chooseHeroes(List<Hero> allHeroes) {
        List<Hero> available = new ArrayList<>(allHeroes);
        Party party = new Party();
        while (party.size() < TEAM_SIZE) {
            List<String> labels = new ArrayList<>();
            for (Hero candidate : available) {
                labels.add(candidate.toString());
            }
            Hero hero = console.choose(
                    "\nChoose hero " + (party.size() + 1) + " of " + TEAM_SIZE + ":",
                    available, labels);
            available.remove(hero); // a hero can only be picked once
            party.addHero(hero);
            System.out.println(hero.getName() + " joins your team!");
        }
        return party;
    }
}
