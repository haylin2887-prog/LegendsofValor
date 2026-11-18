package valor.world;

// the wall columns between lanes. nothing can ever enter these
public class InaccessibleTile extends Tile {
    public InaccessibleTile(int row, int col) {
        super(row, col, 'X', false);
    }
}
