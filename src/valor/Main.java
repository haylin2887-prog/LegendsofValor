package valor;

import java.util.Random;
import valor.Characters.GameData;
import valor.Characters.Hero;
import valor.Characters.Party;
import valor.market.Market;
import valor.ui.Console;
import valor.ui.MarketMenu;
import valor.ui.SetupMenu;
import valor.ui.WorldView;
import valor.world.GenerateWorld;
import valor.world.World;


// run from LegendsofValor folder
// javac -d out src/valor/*.java src/valor/world/*.java src/valor/ui/*.java src/valor/Characters/*.java src/valor/item/*.java src/valor/data/*.java src/valor/market/*.java src/valor/battle/*.java
// java -cp out valor.Main
public class Main {
    public static void main(String[] args) {
        Console console = new Console();
        SetupMenu setup = new SetupMenu(console);
        GameData data = new GameData();

        setup.greet();

        Party party = setup.chooseHeroes(data.getHeroes());
        System.out.println("\nYour team:");
        for (Hero hero : party.getHeroes()) {
            System.out.println("  " + hero.statusLine(false));
        }
        System.out.println();

        World world = new World(GenerateWorld.generateWorld(new Random()));
        world.spawnHeroes(party.getHeroes()); // one hero per lane, on the heroes' Nexus
        System.out.println(WorldView.render(world));

        // one market for the heroes' Nexus. the turn loop opens it with
        // marketMenu.open(hero, market) when a hero is standing on a hero Nexus tile
        Market market = new Market(data.getAllItems());
        MarketMenu marketMenu = new MarketMenu(console);
        new Game(console, data, party, world, market, marketMenu).run();
    }

}
