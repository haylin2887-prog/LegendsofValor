package valor.Characters;

import java.util.ArrayList;

import valor.Rules;
import valor.item.Armor;
import valor.item.Item;
import valor.item.Weapon;

public abstract class Hero extends Entity {
    
    private double mp;
    private double maxMp;
    private double strength;
    private double dexterity;
    private double agility;
    private int gold;
    private int xp;

    // each hero type favors two skills, which grow faster when leveling up
    private final boolean favorsStrength;
    private final boolean favorsDexterity;
    private final boolean favorsAgility;

    private ArrayList<Item> inventory = new ArrayList<Item>();
    private Weapon weapon;      // equipped weapon (null if none)
    private boolean bothHands;  // is the weapon held with both hands?
    private Armor armor;        // equipped armor (null if none)


    public Hero(String name, double mp, double str, double agi, double dex, int gold, int xp,
                boolean favorsStrength, boolean favorsDexterity, boolean favorsAgility) {
        super(name, 1);
        this.mp = mp;
        this.maxMp = mp;
        this.gold = gold;
        this.xp = xp;
        this.favorsStrength = favorsStrength;
        this.favorsDexterity = favorsDexterity;
        this.favorsAgility = favorsAgility;

        // favored skills start a little higher
        double bonus = 1 + Rules.FAVORED_START_BONUS;
        this.strength = str;
        if (favorsStrength) {
            this.strength = str * bonus;
        }
        this.agility = agi;
        if (favorsAgility) {
            this.agility = agi * bonus;
        }
        this.dexterity = dex;
        if (favorsDexterity) {
            this.dexterity = dex * bonus;
        }
    }

    public abstract String heroType();

    // ---------- getters ----------
    public double getMp() {
        return mp;
    }

    public double getMaxMp() {
        return maxMp;
    }

    public double getStrength() {
        return strength;
    }

    public double getDexterity() {
        return dexterity;
    }

    public double getAgility() {
        return agility;
    }

    public int getXp() {
        return xp;
    }

    public boolean favorsStrength() {
        return favorsStrength;
    }

    public boolean favorsDexterity() {
        return favorsDexterity;
    }

    public boolean favorsAgility() {
        return favorsAgility;
    }


    public double maxHp() {
        return level * Rules.HP_PER_LEVEL;
    }



    public int getGold() {
        return gold;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }


    // ---------- money and items ----------
    public boolean canAfford(int cost) {
        return gold >= cost;
    }

    public void spend(int amount) {
        gold -= amount;
    }

    public void earn(int amount) {
        gold += amount;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    // returns false if the hero doesn't have this item. an equipped item is unequipped first
    public boolean removeItem(Item item) {
        if (!inventory.remove(item)) {
            return false;
        }
        if (item == weapon) {
            weapon = null;
            bothHands = false;
        }
        if (item == armor) {
            armor = null;
        }
        return true;
    }
// ---------- equipment ----------
    public Weapon getWeapon() {
        return weapon;
    }

    public Armor getArmor() {
        return armor;
    }

// ---------- fighting ----------
    @Override
    public double dodgeChance() {
        return agility * Rules.HERO_DODGE_FACTOR;
    }

    // ---------- text shown to the player ----------
    public String statusLine(boolean inBattle) {
        String weaponText = "none";
        if (weapon != null) {
            weaponText = weapon.getName();
            if (bothHands) {
                weaponText = weaponText + " (2 hands)";
            }
        }
        String armorText = "none";
        if (armor != null) {
            armorText = armor.getName();
        }
        String equipment = "weapon: " + weaponText + ", armor: " + armorText;

        String text = name + " the " + heroType() + "  Lv" + level
                + "  HP " + Math.round(hp) + "  MP " + Math.round(mp);
        if (inBattle) {
            if (isFainted()) {
                text = text + "  [FAINTED]";
            }
            return text + "  " + equipment;
        }
        return text + "  XP " + xp + "  Gold " + gold
                + "  STR " + Math.round(strength) + " DEX " + Math.round(dexterity)
                + " AGI " + Math.round(agility) + "  " + equipment;
    }

    @Override
    public String toString() {
        return statusLine(false);
    }

    // ---------- the three hero types with different favored skills: strength, dexterity, agility ----------
    public static class Warrior extends Hero {
        public Warrior(String name, double mp, double str, double agi, double dex, int gold, int xp) {
            super(name, mp, str, agi, dex, gold, xp, true, false, true);
        }

        @Override
        public String heroType() {
            return "Warrior";
        }
    }

    public static class Sorcerer extends Hero {
        public Sorcerer(String name, double mp, double str, double agi, double dex, int gold, int xp) {
            super(name, mp, str, agi, dex, gold, xp, false, true, true);
        }

        @Override
        public String heroType() {
            return "Sorcerer";
        }
    }

    public static class Paladin extends Hero {
        public Paladin(String name, double mp, double str, double agi, double dex, int gold, int xp) {
            super(name, mp, str, agi, dex, gold, xp, true, true, false);
        }

        @Override
        public String heroType() {
            return "Paladin";
        }
    }


}
