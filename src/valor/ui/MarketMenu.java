package valor.ui;

import java.util.ArrayList;
import java.util.List;
import valor.Characters.Hero;
import valor.item.Equipment;
import valor.item.Item;
import valor.market.Market;

// the shop a hero opens while standing on the heroes' Nexus.
// each hero shops alone, with their own gold and inventory
public class MarketMenu {
    private static final int LEAVE = 0;
    private static final int BUY = 1;
    private static final int SELL = 2;
    private static final int REPAIR = 3;
    private static final int INVENTORY = 4;

    private final Console console;

    public MarketMenu(Console console) {
        this.console = console;
    }

    public void open(Hero hero, Market market) {
        System.out.println("\n" + hero.getName() + " enters the market.");
        while (true) {
            System.out.println("\n" + hero.getName() + " (level " + hero.getLevel() + ") has "
                    + hero.getGold() + " gold.");
            System.out.println("0) Leave  1) Buy  2) Sell  3) Repair  4) View inventory");
            int choice = console.readNumber("> ", LEAVE, INVENTORY);
            if (choice == LEAVE) {
                System.out.println(hero.getName() + " leaves the market.");
                return;
            } else if (choice == BUY) {
                buy(hero, market);
            } else if (choice == SELL) {
                sell(hero, market);
            } else if (choice == REPAIR) {
                repair(hero, market);
            } else {
                showInventory(hero);
            }
        }
    }

    private void buy(Hero hero, Market market) {
        Item item = pick("Items for sale:", "This market has nothing to sell.", market.getCatalog());
        if (item != null) {
            System.out.println(market.buy(hero, item));
        }
    }

    private void sell(Hero hero, Market market) {
        Item item = pick("Sell which item? (they sell for half price)",
                hero.getName() + " has nothing to sell.", hero.getInventory());
        if (item != null) {
            System.out.println(market.sell(hero, item));
        }
    }

    private void repair(Hero hero, Market market) {
        List<Equipment> broken = new ArrayList<>();
        for (Item item : hero.getInventory()) {
            if (item instanceof Equipment && !item.isUsable()) {
                broken.add((Equipment) item);
            }
        }
        Equipment item = pick("Repair which item? (repairs cost half price)",
                hero.getName() + " has nothing broken.", broken);
        if (item != null) {
            System.out.println(market.repair(hero, item));
        }
    }

    private void showInventory(Hero hero) {
        if (hero.getInventory().isEmpty()) {
            System.out.println(hero.getName() + " has no items.");
            return;
        }
        System.out.println(hero.getName() + "'s items:");
        for (Item item : hero.getInventory()) {
            String equipped = (item == hero.getWeapon() || item == hero.getArmor()) ? " [equipped]" : "";
            System.out.println("  - " + item.describe() + equipped);
        }
    }

    // shows the items in a numbered list with 0 to go back.
    // returns null if there is nothing to pick or the player goes back
    private <T extends Item> T pick(String title, String emptyMessage, List<T> items) {
        if (items.isEmpty()) {
            System.out.println(emptyMessage);
            return null;
        }
        System.out.println(title);
        System.out.println("  0) Back");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + items.get(i).describe());
        }
        int choice = console.readNumber("> ", 0, items.size());
        return (choice == 0) ? null : items.get(choice - 1);
    }
}
