package valor.item;

public class Armor extends Equipment {
    private final int damageReduction;

    public Armor(String name, int price, int requiredLevel,
                 int damageReduction, Durability durability) {
        super(name, price, requiredLevel, durability);
        this.damageReduction = damageReduction;
    }

    public int getDamageReduction() {
        return damageReduction;
    }

    @Override
    public String describe() {
        return super.describe() + " damage reduction " + damageReduction;
    }

    @Override
    public Armor copy() {
        return new Armor(getName(), getPrice(), getRequiredLevel(), damageReduction, copyDurability());
    }
}
