package valor.world;

// blocks movement until a hero spends a turn removing it, then it becomes a PlainTile
public class ObstacleTile extends Tile {
    public ObstacleTile(int row, int col) {
        super(row, col, 'O', false);
    }

    @Override
    public boolean isRemovable() {
        return true;
    }
}
