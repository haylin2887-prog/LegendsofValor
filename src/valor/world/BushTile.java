package valor.world;

import valor.item.PotionStat;

// increases the dexterity of a hero standing on it
public class BushTile extends Tile {
    public BushTile(int row, int col) {
        super(row, col, 'B', true);
    }

    @Override
    public PotionStat getBonusStat() {
        return PotionStat.DEXTERITY;
    }
}
