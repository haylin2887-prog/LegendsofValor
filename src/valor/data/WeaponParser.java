package valor.data;

import valor.item.Durability;
import valor.item.Weapon;

// Name/cost/level/damage/required hands
public class WeaponParser implements RecordParser<Weapon> {
    private final int uses;

    public WeaponParser(int uses) {
        this.uses = uses;
    }

    @Override
    public Weapon parse(String[] row) {
        String name = row[0];
        int price = Integer.parseInt(row[1]);
        int requiredLevel = Integer.parseInt(row[2]);
        int damage = Integer.parseInt(row[3]);
        int handsRequired = Integer.parseInt(row[4]);
        return new Weapon(name, price, requiredLevel, damage, handsRequired, new Durability(uses));
    }
}
