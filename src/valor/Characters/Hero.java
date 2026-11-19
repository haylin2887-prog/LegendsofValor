package valor.Characters;

public abstract class Hero extends Entity {
    
    private double mp;
    private double maxMp;
    private double strength;
    private double dexterity;
    private double agility;
    private int gold;
    private int xp;

public Hero(String name, double mp, double str, double agi, double dex, int gold, int xp,
                boolean favorsStrength, boolean favorsDexterity, boolean favorsAgility) {
        super(name, 1);
        this.mp = mp;
        this.maxMp = mp;
        this.gold = gold;
        this.xp = xp;
    
    }

}
