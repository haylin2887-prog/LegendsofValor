package valor.ui;

import java.util.List;
import valor.Characters.Entity;
import valor.world.Lane;
import valor.world.Position;
import valor.world.World;

// draws the board as text, with row numbers down the left and lane names along the bottom.
// every cell is 3 lines tall so several entities can share a tile:
//         [M1 M2 M3 ]    monsters on the tile
//     3   [--- B ---]    the tile's terrain
//         [H1 H2 H3 ]    heroes on the tile
// the walls between lanes are filled with X's
// cells get wider when some tile needs more room
public class WorldView {
    private static final String ROW_PREFIX = "    0   "; // how much room a row number takes up
    private static final int MIN_INSIDE = 9;             // "--- B ---" or "M1 M2 M3 "
    private static final int CELL_BORDER = 3;            // "[", "]" and the space between cells

    public static String render(World world) {
        int inside = insideWidth(world);
        StringBuilder map = new StringBuilder();
        for (int row = 0; row < world.size(); row++) {
            StringBuilder monsters = new StringBuilder(" ".repeat(ROW_PREFIX.length()));
            StringBuilder terrain = new StringBuilder(rowPrefix(row));
            StringBuilder heroes = new StringBuilder(" ".repeat(ROW_PREFIX.length()));
            for (int col = 0; col < world.size(); col++) {
                Position position = new Position(row, col);
                if (Lane.isWall(col)) {
                    String wall = cell("X".repeat(inside), inside);
                    monsters.append(wall);
                    terrain.append(wall);
                    heroes.append(wall);
                    continue;
                }
                monsters.append(cell(labels(world.monstersAt(position)), inside));
                terrain.append(cell(terrain(world, position, inside), inside));
                heroes.append(cell(labels(world.heroesAt(position)), inside));
            }
            map.append(monsters).append('\n');
            map.append(terrain).append('\n');
            map.append(heroes).append('\n');
            map.append('\n');
        }
        map.append(laneLabels(inside));
        return map.toString();
    }

    // room between the brackets: enough for the most crowded tile
    private static int insideWidth(World world) {
        int width = MIN_INSIDE;
        for (int row = 0; row < world.size(); row++) {
            for (int col = 0; col < world.size(); col++) {
                Position position = new Position(row, col);
                width = Math.max(width, labels(world.monstersAt(position)).length());
                width = Math.max(width, labels(world.heroesAt(position)).length());
            }
        }
        return width;
    }

    // "    3   " for row 3, the same width as ROW_PREFIX
    private static String rowPrefix(int row) {
        return "    " + row + "   ";
    }

    // "H1 H2 H3", or "" if nobody is there
    private static String labels(List<? extends Entity> entities) {
        StringBuilder text = new StringBuilder();
        for (Entity entity : entities) {
            if (text.length() > 0) {
                text.append(' ');
            }
            text.append(entity.getLabel());
        }
        return text.toString();
    }

    // the terrain symbol in the middle of a dashed line: "--- B ---"
    private static String terrain(World world, Position position, int inside) {
        String symbol = " " + world.tileAt(position).getSymbol() + " ";
        int left = (inside - symbol.length()) / 2;
        int right = inside - symbol.length() - left;
        return "-".repeat(left) + symbol + "-".repeat(right);
    }

    // one line of a cell, padded to the cell's width
    private static String cell(String text, int inside) {
        return "[" + text + " ".repeat(inside - text.length()) + "] ";
    }

    // puts each lane's name under the middle of its two columns
    private static String laneLabels(int inside) {
        int cellWidth = inside + CELL_BORDER;
        StringBuilder labels = new StringBuilder();
        for (Lane lane : Lane.values()) {
            String name = label(lane);
            int laneWidth = 2 * cellWidth - 1;   // two cells and the space between them
            int start = ROW_PREFIX.length() + lane.getLeftCol() * cellWidth + (laneWidth - name.length()) / 2;
            while (labels.length() < start) {
                labels.append(' ');
            }
            labels.append(name);
        }
        return labels.append('\n').toString();
    }

    // TOP -> Top
    private static String label(Lane lane) {
        String name = lane.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}
