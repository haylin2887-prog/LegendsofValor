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
    
    }
    public abstract String heroType();

    // ---------- getters ----------
    public double getMp() {
        return mp;
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


}
