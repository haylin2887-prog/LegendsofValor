package valor.item;

// an item a hero wears or holds (weapons and armor). only equipment wears out and can be repaired;
// potions and spells are used up instead
public abstract class Equipment extends Item {
    public Equipment(String name, int price, int requiredLevel, Durability durability) {
        super(name, price, requiredLevel, durability);
    }
}
