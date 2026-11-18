package valor;

import java.util.Random;
import valor.ui.WorldView;
import valor.world.GenerateWorld;
import valor.world.World;


// run from LegendsofValor folder
// javac -d out src/valor/*.java src/valor/world/*.java src/valor/ui/*.java
// java -cp out valor.Main
public class Main {
    public static void main(String[] args) {
        World world = new World(GenerateWorld.generateWorld(new Random()));
        System.out.println(WorldView.render(world));
    }
}
