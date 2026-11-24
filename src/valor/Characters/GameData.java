package valor.Characters;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import valor.Rules;
import valor.data.DataFileReader;
import valor.data.HeroParser;
import valor.data.ItemCatalog;
import valor.data.MonsterParser;
import valor.data.RecordParser;
import valor.item.Item;

// Reads the text files in the Data folder and keeps every hero, monster and item in lists.
// run the game from the LegendsofValor folder so "Data" can be found
public class GameData {
    private static final Path DATA_FOLDER = Paths.get("Data");

    private final List<Hero> heroes = new ArrayList<>();
    private final List<Monster> monsters = new ArrayList<>();
    private final ItemCatalog items;

    public GameData() {
        this(DATA_FOLDER);
    }

    public GameData(Path dataFolder) {
        // each hero file gets a factory that turns its rows into the right kind of hero
        load(dataFolder, "Warriors.txt", new HeroParser(Hero.Warrior::new), heroes);
        load(dataFolder, "Sorcerers.txt", new HeroParser(Hero.Sorcerer::new), heroes);
        load(dataFolder, "Paladins.txt", new HeroParser(Hero.Paladin::new), heroes);

        load(dataFolder, "Dragons.txt", new MonsterParser(Monster.Dragon::new), monsters);
        load(dataFolder, "Exoskeletons.txt", new MonsterParser(Monster.Exoskeleton::new), monsters);
        load(dataFolder, "Spirits.txt", new MonsterParser(Monster.Spirit::new), monsters);

        items = new ItemCatalog(dataFolder);
    }

    private static <T> void load(Path folder, String fileName, RecordParser<T> parser, List<T> destination) {
        DataFileReader.load(folder.resolve(fileName), parser, destination);
    }

    public List<Hero> getHeroes() {
        return Collections.unmodifiableList(heroes);
    }

    public List<Monster> getMonsters() {
        return Collections.unmodifiableList(monsters);
    }

    // a random monster at the given level (a new wave uses the highest hero level)
    public Monster createMonster(int level) {
        Monster template = monsters.get(Rules.randomInt(monsters.size()));
        return template.copyAtLevel(level);
    }

    public ItemCatalog getItemCatalog() {
        return items;
    }

    public List<Item> getAllItems() {
        return items.getAllItems();
    }
}
