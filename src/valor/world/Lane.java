package valor.world;

// lane is divided into top mid and bot with 2 columns to indicate a wall
public enum Lane {
    TOP(0, 1),
    MID(3, 4),
    BOT(6, 7);

    private final int leftCol;
    private final int rightCol;

    Lane(int leftCol, int rightCol) {
        this.leftCol = leftCol;
        this.rightCol = rightCol;
    }

    public int getLeftCol() {
        return leftCol;
    }

    public int getRightCol() {
        return rightCol;
    }

    // the lane a column belongs to, or null for the wall columns
    public static Lane of(int col) {
        for (Lane lane : values()) {
            if (lane.contains(col)) {
                return lane;
            }
        }
        return null;
    }

    public boolean contains(int col) {
        return col == leftCol || col == rightCol;
    }

    // true for the wall columns between lanes
    public static boolean isWall(int col) {
        for (Lane lane : values()) {
            if (lane.contains(col)) {
                return false;
            }
        }
        return true;
    }
}
