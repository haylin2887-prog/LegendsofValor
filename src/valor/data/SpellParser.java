package valor.data;

import valor.item.Durability;
import valor.item.Spell;
import valor.item.SpellType;

// Name/cost/required level/damage/mana cost. the spell type comes from which file is being read
public class SpellParser implements RecordParser<Spell> {
    private final SpellType spellType;
    private final int uses;

    public SpellParser(SpellType spellType, int uses) {
        this.spellType = spellType;
        this.uses = uses;
    }

    @Override
    public Spell parse(String[] row) {
        String name = row[0];
        int price = Integer.parseInt(row[1]);
        int requiredLevel = Integer.parseInt(row[2]);
        int damage = Integer.parseInt(row[3]);
        int manaCost = Integer.parseInt(row[4]);
        return new Spell(name, price, requiredLevel, damage, manaCost, spellType, new Durability(uses));
    }
}
