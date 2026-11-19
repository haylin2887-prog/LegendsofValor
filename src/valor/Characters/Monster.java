package valor.Characters;

import valor.Rules;

public class Monster extends Entity {
    protected double baseDamage;
    protected double defense;
    protected double dodge;

    public Monster(String name, int level, double baseDamage, double defense, double dodge) {
        super(name, level);
        this.baseDamage = baseDamage;
        this.defense = defense;
        this.dodge = dodge;
    }
    
    public double attackDamage() {
        return baseDamage * Rules.MONSTER_DAMAGE_FACTOR;
    }
    
    @Override 
    public double dodgeChance() {
        return dodge * Rules.MONSTER_DODGE_FACTOR;
    }

    // Damage left after the monster's natural defense.
    public double afterDefense(double damage) {
        double result = damage - defense * Rules.DEFENSE_FACTOR;
        if (result < 0) 
            {
            result = 0;
        }
        return result;
    }
}
