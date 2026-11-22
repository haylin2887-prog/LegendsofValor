package valor.item;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class Potion extends Item {
    private final int effectAmount;
    private final Set<PotionStat> affectedStats; // a set, because Mermaid_Tears and Ambrosia raise several stats

    public Potion(String name, int price, int requiredLevel,
                  int effectAmount, Set<PotionStat> affectedStats, Durability durability) {
        super(name, price, requiredLevel, durability);
        this.effectAmount = effectAmount;
        // an EnumSet always lists the stats in enum order, so messages print the same way every game.
        // unmodifiableSet stops other classes from changing which stats the potion raises
        EnumSet<PotionStat> stats = EnumSet.noneOf(PotionStat.class);
        stats.addAll(affectedStats);
        this.affectedStats = Collections.unmodifiableSet(stats);
    }

    public int getEffectAmount() {
        return effectAmount;
    }

    public Set<PotionStat> getAffectedStats() {
        return affectedStats;
    }

    @Override
    public String describe() {
        return super.describe() + " +" + effectAmount + " to " + affectedStats;
    }

    @Override
    public Potion copy() {
        return new Potion(getName(), getPrice(), getRequiredLevel(), effectAmount, affectedStats, copyDurability());
    }
}
