package valor.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import valor.Rules;
import valor.Characters.Hero;
import valor.item.Equipment;
import valor.item.Item;

// the market on the heroes' Nexus. it never runs out: buying hands the hero a new copy
// of the catalog item, and sold items are gone for good.
// it prints nothing: each call returns a message for the market menu to display.
public class Market {
    private final List<Item> catalog;

    public Market(List<Item> catalog) {
        this.catalog = new ArrayList<>(catalog);
    }

    public List<Item> getCatalog() {
        return Collections.unmodifiableList(catalog);
    }

    public int sellPrice(Item item) {
        return item.getPrice() / Rules.SELL_DIVISOR;
    }

    public int repairCost(Item item) {
        return item.getPrice() / Rules.SELL_DIVISOR;
    }

    // gives the hero a fresh copy of a catalog item
    public String buy(Hero hero, Item item) {
        if (!catalog.contains(item)) {
            return item.getName() + " is not sold here.";
        }
        if (hero.getLevel() < item.getRequiredLevel()) {
            return hero.getName() + " must be level " + item.getRequiredLevel()
                    + " to buy " + item.getName() + ".";
        }
        if (!hero.canAfford(item.getPrice())) {
            return hero.getName() + " can't afford " + item.getName() + ".";
        }
        hero.spend(item.getPrice());
        hero.addItem(item.copy());
        return hero.getName() + " bought " + item.getName()
                + " for " + item.getPrice() + " gold.";
    }

    // takes the item away for half its price. equipped items are unequipped first
    public String sell(Hero hero, Item item) {
        if (!hero.removeItem(item)) {
            return hero.getName() + " doesn't have " + item.getName() + ".";
        }
        int price = sellPrice(item);
        hero.earn(price);
        return hero.getName() + " sold " + item.getName()
                + " for " + price + " gold.";
    }

    // takes Equipment because potions and spells are used up, not repaired
    public String repair(Hero hero, Equipment item) {
        if (!hero.getInventory().contains(item)) {
            return hero.getName() + " doesn't have " + item.getName() + ".";
        }
        if (item.isUsable()) {
            return item.getName() + " isn't broken.";
        }
        int cost = repairCost(item);
        if (!hero.canAfford(cost)) {
            return hero.getName() + " can't afford to repair " + item.getName() + ".";
        }
        hero.spend(cost);
        item.repair();
        return hero.getName() + " repaired " + item.getName()
                + " for " + cost + " gold.";
    }
}
