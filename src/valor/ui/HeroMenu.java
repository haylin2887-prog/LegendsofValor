package valor.ui;

import java.util.ArrayList;
import java.util.List;

import valor.Characters.Hero;
import valor.Characters.Monster;
import valor.item.Armor;
import valor.item.Item;
import valor.item.Potion;
import valor.item.Spell;
import valor.item.Weapon;

// the menus a hero uses during its turn: pick a target, a spell, a potion, or equipment.
// every method returns null / false if the hero has nothing to pick or the player goes back,
// so the turn is not used up
public class HeroMenu {
    private final Console console;

    public HeroMenu(Console console) {
        this.console = console;
    }

    // one target is picked for the player
    public Monster chooseTarget(List<Monster> targets) {
        if (targets.size() == 1) {
            return targets.get(0);
        }
        List<String> labels = new ArrayList<>();
        for (Monster monster : targets) {
            labels.add(monster.statusLine());
        }
        return pick("Choose a target:", targets, labels);
    }

    public Spell chooseSpell(Hero hero) {
        List<Spell> spells = hero.getItems(Spell.class);
        if (spells.isEmpty()) {
            System.out.println(hero.getName() + " has no spells.");
            return null;
        }
        return pick("Cast which spell?", spells, describeItems(spells));
    }

    // drinks the chosen potion. returns true if one was used
    public boolean usePotion(Hero hero) {
        List<Potion> potions = hero.getItems(Potion.class);
        if (potions.isEmpty()) {
            System.out.println(hero.getName() + " has no potions.");
            return false;
        }
        Potion potion = pick("Drink which potion?", potions, describeItems(potions));
        if (potion == null) {
            return false;
        }
        System.out.println(hero.usePotion(potion));
        return true;
    }

    // equips a weapon or armor. returns true if something was equipped
    public boolean equip(Hero hero) {
        List<Item> options = new ArrayList<>();
        for (Item item : hero.getInventory()) {
            if ((item instanceof Weapon || item instanceof Armor) && item.isUsable()) {
                options.add(item);
            }
        }
        if (options.isEmpty()) {
            System.out.println(hero.getName() + " has no usable weapons or armor.");
            return false;
        }
        Item item = pick("Equip which item?", options, describeItems(options));
        if (item == null) {
            return false;
        }
        if (item instanceof Armor) {
            hero.equipArmor((Armor) item);
            System.out.println(hero.getName() + " put on " + item.getName() + ".");
            return true;
        }
        Weapon weapon = (Weapon) item;
        boolean bothHands = weapon.getHandsRequired() == 2;
        if (!bothHands) {
            bothHands = askBothHands(weapon);
        }
        hero.equipWeapon(weapon, bothHands);
        System.out.println(hero.getName() + " equipped " + weapon.getName()
                + (bothHands ? " (both hands)." : "."));
        return true;
    }

    private boolean askBothHands(Weapon weapon) {
        System.out.println(weapon.getName() + " is one-handed. Hold it with both hands for +50% weapon damage?");
        System.out.println("  1) One hand   2) Both hands");
        return console.readNumber("> ", 1, 2) == 2;
    }

    // the text shown for each item in a menu
    private List<String> describeItems(List<? extends Item> items) {
        List<String> labels = new ArrayList<>();
        for (Item item : items) {
            labels.add(item.describe());
        }
        return labels;
    }

    // numbered list with 0 to go back. returns null if the player goes back.
    // labels.get(i) is the text shown for options.get(i), so both lists must have the same size
    public <T> T pick(String title, List<T> options, List<String> labels) {
        System.out.println(title);
        System.out.println("  0) Back");
        for (int i = 0; i < options.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + labels.get(i));
        }
        int choice = console.readNumber("> ", 0, options.size());
        if (choice == 0) {
            return null;
        }
        return options.get(choice - 1);
    }
}
