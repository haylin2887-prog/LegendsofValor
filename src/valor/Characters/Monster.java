package valor.Characters;

import valor.Rules;
import valor.item.PotionStat;

// abstract: every monster is a Dragon, Exoskeleton or Spirit (nested classes at the bottom)
public abstract class Monster extends Entity {
    protected double baseDamage;
    protected double defense;
    protected double dodge;
    private PotionStat terrainBonus = null;   // hard mode only: the tile's stat helps the monster too

    public Monster(String name, int level, double baseDamage, double defense, double dodge) {
        super(name, level);
        this.baseDamage = baseDamage;
        this.defense = defense;
        this.dodge = dodge;
    }

    public abstract String getKind();

    // a new monster of the same kind and stats at another level, for new waves.
    // damage and defense grow with the level; dodge stays the same so it can't pass 100%
    public abstract Monster copyAtLevel(int newLevel);

    protected double levelRatio(int newLevel) {
        return (double) newLevel / level;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    public double getDefense() {
        return defense;
    }

    public double getDodge() {
        return dodge;
    }

    // hard mode: Koulou (STRENGTH) = more damage, Cave (AGILITY) = more dodge, Bush (DEXTERITY) = more defense.
    // pass tile.getBonusStat(), null removes the bonus
    public void applyTerrainBonus(PotionStat stat) {
        terrainBonus = stat;
    }

    private double boost(PotionStat stat) {
        if (stat == terrainBonus) {
            return 1 + Rules.TERRAIN_BONUS;
        }
        return 1;
    }

    public double attackDamage() {
        return baseDamage * boost(PotionStat.STRENGTH) * Rules.MONSTER_DAMAGE_FACTOR;
    }

    @Override
    public double dodgeChance() {
        return dodge * boost(PotionStat.AGILITY) * Rules.MONSTER_DODGE_FACTOR;
    }

    // Damage left after the monster's natural defense.
    public double afterDefense(double damage) {
        double result = damage - defense * boost(PotionStat.DEXTERITY) * Rules.DEFENSE_FACTOR;
        if (result < 0) {
            result = 0;
        }
        return result;
    }

    // These three are used by the spell effects.
    public void reduceDamage(double percent) {
        baseDamage = baseDamage * (1 - percent);
    }

    public void reduceDefense(double percent) {
        defense = defense * (1 - percent);
    }

    public void reduceDodge(double percent) {
        dodge = dodge * (1 - percent);
    }

    public String statusLine() {
        String prefix = "";
        if (!label.isEmpty()) {
            prefix = "[" + label + "] ";
        }
        return prefix + name + " the " + getKind() + "  Lv" + level
                + "  HP " + Math.round(hp)
                + "  DMG " + Math.round(attackDamage())
                + "  DEF " + Math.round(defense)
                + "  DODGE " + Math.round(Math.min(dodgeChance(), Rules.MAX_DODGE) * 100) + "%";
    }

    @Override
    public String toString() {
        return statusLine();
    }

    // ---------- the three monster types (each is stronger in one stat) ----------
    public static class Dragon extends Monster {
        public Dragon(String name, int level, double damage, double defense, double dodge) {
            super(name, level, damage, defense, dodge);
            this.baseDamage = this.baseDamage * Rules.MONSTER_FAVORED_BONUS;
        }

        @Override
        public String getKind() {
            return "Dragon";
        }

        @Override
        public Monster copyAtLevel(int newLevel) {
            double r = levelRatio(newLevel);
            return new Dragon(name, newLevel, baseDamage / Rules.MONSTER_FAVORED_BONUS * r, defense * r, dodge);
        }
    }

    public static class Exoskeleton extends Monster {
        public Exoskeleton(String name, int level, double damage, double defense, double dodge) {
            super(name, level, damage, defense, dodge);
            this.defense = this.defense * Rules.MONSTER_FAVORED_BONUS;
        }

        @Override
        public String getKind() {
            return "Exoskeleton";
        }

        @Override
        public Monster copyAtLevel(int newLevel) {
            double r = levelRatio(newLevel);
            return new Exoskeleton(name, newLevel, baseDamage * r, defense / Rules.MONSTER_FAVORED_BONUS * r, dodge);
        }
    }

    public static class Spirit extends Monster {
        public Spirit(String name, int level, double damage, double defense, double dodge) {
            super(name, level, damage, defense, dodge);
            this.dodge = this.dodge * Rules.MONSTER_FAVORED_BONUS;
        }

        @Override
        public String getKind() {
            return "Spirit";
        }

        @Override
        public Monster copyAtLevel(int newLevel) {
            double r = levelRatio(newLevel);
            return new Spirit(name, newLevel, baseDamage * r, defense * r, dodge / Rules.MONSTER_FAVORED_BONUS);
        }
    }
}
