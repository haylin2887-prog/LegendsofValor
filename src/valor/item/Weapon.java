package valor.item;

public class Weapon extends Equipment {
    private final int damage;
    private final int handsRequired;

    public Weapon(String name, int price, int requiredLevel,
                  int damage, int handsRequired, Durability durability) {
        super(name, price, requiredLevel, durability);
        this.damage = damage;
        this.handsRequired = handsRequired;
    }

    public int getDamage() {
        return damage;
    }

    public int getHandsRequired() {
        return handsRequired;
    }

    @Override
    public String describe() {
        return super.describe() + " damage " + damage + ", hands " + handsRequired;
    }

    @Override
    public Weapon copy() {
        return new Weapon(getName(), getPrice(), getRequiredLevel(), damage, handsRequired, copyDurability());
    }
}
