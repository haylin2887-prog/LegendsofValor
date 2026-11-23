package valor.world;
import valor.item.PotionStat;

// a Tile is one square on the board. it only knows its terrain, not who is standing on it
public abstract class Tile {
    private final int row;
    private final int col;
    private final char symbol;        // N, I, O, P, B, C, K
    private final boolean accessible; // false only for Inaccessible and Obstacle

    protected Tile(int row, int col, char symbol, boolean accessible) {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
        this.accessible = accessible;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean isAccessible() {
        return accessible;
    }

    // the stat this tile boosts while someone stands on it (null = none).
    // Bush, Cave and Koulou override this
    public PotionStat getBonusStat() {
        return null;
    }

    
    // true if a hero can clear this tile (only obstacles)
    public boolean isRemovable() {
        return false;
    }
}
