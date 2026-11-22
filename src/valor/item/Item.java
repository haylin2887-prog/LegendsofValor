package valor.item;

// anything a hero can buy from the market: weapons, armor, potions and spells
public abstract class Item {
    private final String name;
    private final int price;
    private final int requiredLevel;
    private final Durability durability;

    public Item(String name, int price, int requiredLevel, Durability durability) {
        this.name = name;
        this.price = price;
        this.requiredLevel = requiredLevel;
        this.durability = durability;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public int getUsesLeft() {
        return durability.getUsesLeft();
    }

    public void use() {
        durability.consume();
    }

    public void repair() {
        durability.restore();
    }

    public boolean isUsable() {
        return durability.isUsable();
    }

    // one line of details for menus, subclasses add their own values
    public String describe() {
        return name + " (cost " + price + ", level " + requiredLevel + ", uses left " + getUsesLeft() + ")";
    }

    // the market hands out copies, so each hero's item wears out separately.
    // abstract because only the subclass knows how to rebuild itself as a Weapon, Potion, etc.
    public abstract Item copy();

    // gives a copy its own durability, otherwise both items would share the same uses
    protected Durability copyDurability() {
        return new Durability(durability);
    }
}
