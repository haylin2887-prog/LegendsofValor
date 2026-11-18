package valor.ui;

import valor.world.Lane;
import valor.world.Position;
import valor.world.World;

// draws the board as text, with row numbers down the left and lane names along the bottom:
//     0   [N] [N] [X] [N] [N] [X] [N] [N]
//     1   [P] [B] [X] [C] [P] [X] [O] [K]
//     ...
//           Top         Mid         Bot
public class WorldView {
    private static final String ROW_PREFIX = "    0   "; // how much room a row number takes up
    private static final int CELL_WIDTH = 4;             // "[N] "

    public static String render(World world) {
        StringBuilder map = new StringBuilder();
        for (int row = 0; row < world.size(); row++) {
            map.append(rowPrefix(row));
            for (int col = 0; col < world.size(); col++) {
                map.append(cell(world, new Position(row, col)));
            }
            map.append('\n');
        }
        map.append(laneLabels());
        return map.toString();
    }

    // "    3   " for row 3, the same width as ROW_PREFIX
    private static String rowPrefix(int row) {
        return "    " + row + "   ";
    }

    private static String cell(World world, Position position) {
        return "[" + world.tileAt(position).getSymbol() + "] ";
    }

    // puts each lane's name under the middle of its two columns
    private static String laneLabels() {
        StringBuilder labels = new StringBuilder();
        for (Lane lane : Lane.values()) {
            int start = ROW_PREFIX.length() + lane.getLeftCol() * CELL_WIDTH + 2;
            while (labels.length() < start) {
                labels.append(' ');
            }
            labels.append(label(lane));
        }
        return labels.append('\n').toString();
    }

    // TOP -> Top
    private static String label(Lane lane) {
        String name = lane.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}
