package valor.item;

import valor.Characters.Monster;

// A single-use magic attack. The extra effect (ice, fire, lightning) depends on its SpellType.
public class Spell extends Item {
    private final int damage;
    private final int manaCost;
    private final SpellType spellType;
    // strategy: what the spell does to the monster besides damage
    private final SpellEffect effect; 

    public Spell(String name, int price, int requiredLevel,
                 int damage, int manaCost, SpellType spellType, Durability durability) {
        super(name, price, requiredLevel, durability);
        this.damage = damage;
        this.manaCost = manaCost;
        this.spellType = spellType;
        this.effect = SpellEffect.forType(spellType);
    }

    public int getDamage() {
        return damage;
    }

    public int getManaCost() {
        return manaCost;
    }

    public SpellType getSpellType() {
        return spellType;
    }

    @Override
    public String describe() {
        return super.describe() + " " + spellType + " damage " + damage + ", mana " + manaCost;
    }

    @Override
    public Spell copy() {
        return new Spell(getName(), getPrice(), getRequiredLevel(), damage, manaCost, spellType, copyDurability());
    }

    public String applyEffect(Monster target) {
        
        return effect.apply(target);
    }


}
