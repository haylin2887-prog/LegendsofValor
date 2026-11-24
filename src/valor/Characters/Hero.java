package valor.Characters;

import java.util.ArrayList;

import valor.Rules;
import valor.item.Armor;
import valor.item.Item;
import valor.item.Potion;
import valor.item.PotionStat;
import valor.item.Spell;
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

    // temporary stat boost from the tile the hero stands on (Bush / Cave / Koulou)
    private PotionStat bonusStat = null;

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

        // favored skills start 10% higher
        this.strength = startValue(str, favorsStrength);
        this.agility = startValue(agi, favorsAgility);
        this.dexterity = startValue(dex, favorsDexterity);
    }
    private static double startValue(double base, boolean favored) {
        if (favored) {
            return base * (1 + Rules.FAVORED_START_BONUS);
        }
        return base;
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

    public int getXp() {
        return xp;
    }

    public double getMaxMp() {
        return maxMp;
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

    public ArrayList<Item> getInventory() {
        return inventory;
    }


    // the three skills include the terrain boost, if the hero has one right now
    public double getStrength() {
        return boosted(PotionStat.STRENGTH, strength);
    }

    public double getDexterity() {
        return boosted(PotionStat.DEXTERITY, dexterity);
    }

    public double getAgility() {
        return boosted(PotionStat.AGILITY, agility);
    }

    @Override
    public double dodgeChance() {
        return getAgility() * Rules.HERO_DODGE_FACTOR;
    }
    
    // ---------- terrain boost ----------
    // call when the hero steps on a tile: pass tile.getBonusStat() (null = no boost)
    public void applyTerrainBonus(PotionStat stat) {
        bonusStat = stat;
    }

    // call when the hero leaves the tile
    public void clearTerrainBonus() {
        bonusStat = null;
    }

    public PotionStat getBonusStat() {
        return bonusStat;
    }

    private double boosted(PotionStat stat, double base) {
        if (stat == bonusStat) {
            return base * (1 + Rules.TERRAIN_BONUS);
        }
        return base;
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

    // all items of one kind, e.g. getItems(Spell.class)
    public <T extends Item> ArrayList<T> getItems(Class<T> type) {
        ArrayList<T> list = new ArrayList<T>();
        for (Item item : inventory) {
            if (type.isInstance(item)) {
                list.add(type.cast(item));
            }
        }
        return list;
    }
// ---------- equipment ----------
    public Weapon getWeapon() {
        return weapon;
    }

    public Armor getArmor() {
        return armor;
    }
    public boolean isBothHands() {
        return bothHands;
    }

    // a two-handed weapon is always held with both hands. a one-handed one can be, for +50% damage
    public void equipWeapon(Weapon newWeapon, boolean useBothHands) {
        weapon = newWeapon;
        bothHands = useBothHands || newWeapon.getHandsRequired() == 2;
    }

    public void equipArmor(Armor newArmor) {
        armor = newArmor;
    }

    // ---------- using potions ----------
    // raises the stats of the potion, then the potion is used up. returns a message to show
    public String usePotion(Potion potion) {
        for (PotionStat stat : potion.getAffectedStats()) {
            addToStat(stat, potion.getEffectAmount());
        }
        potion.use();
        if (!potion.isUsable()) {
            removeItem(potion);
        }
        return name + " drank " + potion.getName() + " (+" + potion.getEffectAmount()
                + " " + potion.getAffectedStats() + ").";
    }

    private void addToStat(PotionStat stat, double amount) {
        switch (stat) {
            case HEALTH:
                hp += amount;
                break;
            case MANA:
                mp += amount;
                break;
            case STRENGTH:
                strength += amount;
                break;
            case DEXTERITY:
                dexterity += amount;
                break;
            case AGILITY:
                agility += amount;
                break;
            default:
                break;   // heroes have no defense stat, so DEFENSE does nothing
        }
    }


// ---------- fighting ----------
   // a broken weapon (no uses left) counts as no weapon until it is repaired
    public double attackDamage() {
        double weaponDamage = 0;
        if (weapon != null && weapon.isUsable()) {
            weaponDamage = weapon.getDamage();
            if (bothHands && weapon.getHandsRequired() == 1) {
                weaponDamage = weaponDamage * Rules.TWO_HAND_BONUS;
            }
        }
        return (getStrength() + weaponDamage) * Rules.ATTACK_FACTOR;
    }

    // call after every attack that hit: the weapon wears out
    public void weaponUsed() {
        if (weapon != null && weapon.isUsable()) {
            weapon.use();
        }
    }

    // spell damage grows with dexterity
    public double spellDamage(Spell spell) {
        return spell.getDamage() * (1 + getDexterity() / 10000.0) * Rules.SPELL_SCALE;
    }

    public boolean spendMana(double cost) {
        if (mp < cost) {
            return false;
        }
        mp -= cost;
        return true;
    }

    // armor lowers the damage and wears out; returns the damage the hero really took
    public double receiveDamage(double damage) {
        double taken = damage;
        if (armor != null && armor.isUsable()) {
            taken = damage - armor.getDamageReduction() * Rules.ARMOR_FACTOR;
            if (taken < 0) {
                taken = 0;
            }
            armor.use();
        }
        takeDamage(taken);
        return taken;
    }

    // end of a round: a living hero regains 10% of max HP and MP (never above the max)
    public void regenerate() {
        if (isFainted()) {
            return;
        }
        if (hp < maxHp()) {
            hp = Math.min(maxHp(), hp + maxHp() * Rules.REGEN_PCT);
        }
        if (mp < maxMp) {
            mp = Math.min(maxMp, mp + maxMp * Rules.REGEN_PCT);
        }
    }

    // a fainted hero comes back at its Nexus with full HP and MP
    public void respawn() {
        hp = maxHp();
        mp = maxMp;
        clearTerrainBonus();
    }

    // ---------- experience and levels ----------
    // returns true if the hero reached a new level
    public boolean gainExperience(int amount) {
        xp += amount;
        boolean leveledUp = false;
        while (xp >= level * Rules.XP_PER_LEVEL) {
            levelUp();
            leveledUp = true;
        }
        return leveledUp;
    }

    private void levelUp() {
        level += 1;
        strength = strength * (1 + skillGain(favorsStrength));
        dexterity = dexterity * (1 + skillGain(favorsDexterity));
        agility = agility * (1 + skillGain(favorsAgility));
        maxMp = maxMp * Rules.MP_LEVEL_UP;
        mp = mp * Rules.MP_LEVEL_UP;
        if (!isFainted()) {
            hp = maxHp();   // a fainted hero is not revived by a level up, it respawns instead
        }
    }

    // every skill grows 5%, favored skills grow 5% more
    private double skillGain(boolean favored) {
        double gain = Rules.LEVEL_UP_SKILL_GAIN;
        if (favored) {
            gain += Rules.FAVORED_EXTRA_GAIN;
        }
        return gain;
    }

    // ---------- text shown to the player ----------
    public String statusLine(boolean inBattle) {
        String weaponText = "none";
        if (weapon != null) {
            weaponText = weapon.getName();
            if (bothHands) {
                weaponText = weaponText + " (2 hands)";
            }
            if (!weapon.isUsable()) {
                weaponText = weaponText + " (broken)";
            }
        }
        String armorText = "none";
        if (armor != null) {
            armorText = armor.getName();
            if (!armor.isUsable()) {
                armorText = armorText + " (broken)";
            }
        }
        String equipment = "weapon: " + weaponText + ", armor: " + armorText;

        String prefix = "";
        if (!label.isEmpty()) {
            prefix = "[" + label + "] ";
        }
        String terrain = "";
        if (bonusStat != null) {
            terrain = " (+" + Math.round(Rules.TERRAIN_BONUS * 100) + "% " + bonusStat + ")";
        }
        String text = prefix + name + " the " + heroType() + "  Lv" + level
                + "  HP " + Math.round(hp) + "/" + Math.round(maxHp())
                + "  MP " + Math.round(mp) + "/" + Math.round(maxMp);
        if (inBattle) {
            if (isFainted()) {
                text = text + "  [FAINTED - respawns next round]";
            }
            return text + terrain + "  " + equipment;
        }
        return text + "  XP " + xp + "  Gold " + gold
                + "  STR " + Math.round(getStrength()) + " DEX " + Math.round(getDexterity())
                + " AGI " + Math.round(getAgility()) + terrain + "  " + equipment;
    }

    // used by the target menus (teleport)
    @Override
    public String toString() {
        return statusLine(true);
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
