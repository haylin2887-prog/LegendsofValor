package valor.data;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import valor.Rules;
import valor.item.Armor;
import valor.item.Item;
import valor.item.Potion;
import valor.item.Spell;
import valor.item.SpellType;
import valor.item.Weapon;

// every item in the game, read from the Data folder.
// run the game from the LegendsofValor folder so "Data" can be found
public class ItemCatalog {
    private static final Path DATA_FOLDER = Paths.get("Data");

    private final List<Weapon> weapons = new ArrayList<>();
    private final List<Armor> armor = new ArrayList<>();
    private final List<Potion> potions = new ArrayList<>();
    private final List<Spell> spells = new ArrayList<>();

    public ItemCatalog() {
        this(DATA_FOLDER);
    }

    public ItemCatalog(Path dataFolder) {
        load(dataFolder, "Weaponry.txt", new WeaponParser(Rules.WEAPON_USES), weapons);
        load(dataFolder, "Armory.txt", new ArmorParser(Rules.ARMOR_USES), armor);
        load(dataFolder, "Potions.txt", new PotionParser(Rules.POTION_USES), potions);
        load(dataFolder, "FireSpells.txt", new SpellParser(SpellType.FIRE, Rules.SPELL_USES), spells);
        load(dataFolder, "IceSpells.txt", new SpellParser(SpellType.ICE, Rules.SPELL_USES), spells);
        load(dataFolder, "LightningSpells.txt", new SpellParser(SpellType.LIGHTNING, Rules.SPELL_USES), spells);
    }

    private static <T> void load(Path folder, String fileName, RecordParser<T> parser, List<T> destination) {
        DataFileReader.load(folder.resolve(fileName), parser, destination);
    }

    public List<Weapon> getWeapons() {
        return Collections.unmodifiableList(weapons);
    }

    public List<Armor> getArmor() {
        return Collections.unmodifiableList(armor);
    }

    public List<Potion> getPotions() {
        return Collections.unmodifiableList(potions);
    }

    public List<Spell> getSpells() {
        return Collections.unmodifiableList(spells);
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        items.addAll(weapons);
        items.addAll(armor);
        items.addAll(potions);
        items.addAll(spells);
        return items;
    }
}
