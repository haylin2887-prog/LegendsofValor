package valor.Characters;
import valor.Rules;

public abstract class Entity {


// Parent of Hero and Monster: both have a name, a level and HP.

    protected String name;
    protected int level;
    protected double hp;
    protected String label = "";   // short board label: H1, H2, M7, ...
    protected int row;
    protected int col;

    public Entity(String name, int level) {
        this.name = name;
        this.level = level;
        this.hp = level * Rules.HP_PER_LEVEL;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public double getHp() {
        return hp;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    /* public int getLane() {
        return Rules.laneOf(col);
    }*/
    
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isFainted() {
        return hp <= 0;
    }

    public void takeDamage(double amount) {
        hp = hp - amount;
        if (hp < 0) {
            hp = 0;
        }
    }

    // Heroes and monsters calculate their dodge chance differently.
    public abstract double dodgeChance();

    public boolean tryDodge() {
        double chance = dodgeChance();
        if (chance > Rules.MAX_DODGE) {
            chance = Rules.MAX_DODGE;
        }
        return Rules.chance(chance);
    }
}

