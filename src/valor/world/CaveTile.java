package valor.world;

import valor.item.PotionStat;

// increases the agility of a hero standing on it
public class CaveTile extends Tile {
    public CaveTile(int row, int col) {
        super(row, col, 'C', true);
    }

    @Override
    public PotionStat getBonusStat() {
        return PotionStat.AGILITY;
    }
}
