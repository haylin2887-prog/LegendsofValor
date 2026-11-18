package valor.world;

// a spot on the grid. it never changes, moving creates a new Position
public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean sameAs(Position other) {
        return row == other.row && col == other.col;
    }

    public Position moved(Directions direction) {
        switch (direction) {
            case UP:
                return new Position(row - 1, col);
            case DOWN:
                return new Position(row + 1, col);
            case LEFT:
                return new Position(row, col - 1);
            default:
                return new Position(row, col + 1);
        }
    }
}
