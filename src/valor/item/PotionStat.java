package valor.item;

// the hero attributes a potion can raise, named as in Potions.txt
public enum PotionStat {
    HEALTH,
    MANA,
    STRENGTH,
    DEXTERITY,
    AGILITY,
    DEFENSE;

    // "Health" -> HEALTH
    public static PotionStat fromName(String name) {
        return valueOf(name.trim().toUpperCase());
    }
}
