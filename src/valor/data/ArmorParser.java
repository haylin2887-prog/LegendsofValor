package valor.data;

import valor.item.Armor;
import valor.item.Durability;

// Name/cost/required level/damage reduction
public class ArmorParser implements RecordParser<Armor> {
    private final int uses;

    public ArmorParser(int uses) {
        this.uses = uses;
    }

    @Override
    public Armor parse(String[] row) {
        String name = row[0];
        int price = Integer.parseInt(row[1]);
        int requiredLevel = Integer.parseInt(row[2]);
        int damageReduction = Integer.parseInt(row[3]);
        return new Armor(name, price, requiredLevel, damageReduction, new Durability(uses));
    }
}
