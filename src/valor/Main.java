package valor;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import valor.ui.Console;
import valor.ui.SetupMenu;
import valor.ui.WorldView;
import valor.world.GenerateWorld;
import valor.world.World;


// run from LegendsofValor folder
// javac -d out src/valor/*.java src/valor/world/*.java src/valor/ui/*.java
// java -cp out valor.Main
public class Main {
    public static void main(String[] args) {
        Console console = new Console();
        SetupMenu setup = new SetupMenu(console);

        setup.greet();

        // placeholder
        List<String> heroNames = Arrays.asList("Hero A", "Hero B", "Hero C", "Hero D", "Hero E");
        List<String> team = setup.chooseHeroes(heroNames);
        System.out.println("\nYour team: " + String.join(", ", team) + "\n");

        World world = new World(GenerateWorld.generateWorld(new Random()));
        System.out.println(WorldView.render(world));
    }
}
