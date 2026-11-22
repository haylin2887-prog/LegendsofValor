package valor.data;

import java.util.EnumSet;
import java.util.Set;
import valor.item.Durability;
import valor.item.Potion;
import valor.item.PotionStat;

// Name/cost/required level/attribute increase/attribute affected
public class PotionParser implements RecordParser<Potion> {
    private final int uses;

    public PotionParser(int uses) {
        this.uses = uses;
    }

    @Override
    public Potion parse(String[] row) {
        String name = row[0];
        int price = Integer.parseInt(row[1]);
        int requiredLevel = Integer.parseInt(row[2]);
        int effectAmount = Integer.parseInt(row[3]);

        // the stats are always the last column, so Ambrosia's extra "All" column is skipped
        String statList = row[row.length - 1];
        Set<PotionStat> affectedStats = EnumSet.noneOf(PotionStat.class);
        for (String statName : statList.split("/")) {
            affectedStats.add(PotionStat.fromName(statName));
        }

        return new Potion(name, price, requiredLevel, effectAmount, affectedStats, new Durability(uses));
    }
}
