package valor.item;

// A single-use magic attack. The extra effect (ice, fire, lightning) depends on its SpellType.
public class Spell extends Item {
    private final int damage;
    private final int manaCost;
    private final SpellType spellType;

    public Spell(String name, int price, int requiredLevel,
                 int damage, int manaCost, SpellType spellType, Durability durability) {
        super(name, price, requiredLevel, durability);
        this.damage = damage;
        this.manaCost = manaCost;
        this.spellType = spellType;
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

    // TODO (battle): strategy pattern for the side effect.
    // The spell does not care which effect it has; it just asks the strategy.
    /* class IceEffect implements SpellEffect {
        public String apply(Monster target) {
            target.reduceDamage(Rules.SPELL_EFFECT);
            return target.getName() + "'s damage was reduced!";
        }

        public String getName() {
            return "Ice";
        }
    }

    class LightningEffect implements SpellEffect {
        public String apply(Monster target) {
            target.reduceDodge(Rules.SPELL_EFFECT);
            return target.getName() + "'s dodge chance was reduced!";
        }

        public String getName() {
            return "Lightning";
        }
    }

    class FireEffect implements SpellEffect {
        public String apply(Monster target) {
            target.reduceDefense(Rules.SPELL_EFFECT);
            return target.getName() + "'s defense was reduced!";
        }

        public String getName() {
            return "Fire";
        }
    } */
}
