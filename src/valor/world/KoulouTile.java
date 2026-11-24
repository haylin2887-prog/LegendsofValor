package valor.world;

import valor.item.PotionStat;

// increases the strength of a hero standing on it
public class KoulouTile extends Tile {
    public KoulouTile(int row, int col) {
        super(row, col, 'K', true);
    }

    @Override
    public PotionStat getBonusStat() {
        return PotionStat.STRENGTH;
    }
}
