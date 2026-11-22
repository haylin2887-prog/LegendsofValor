package valor.ui;

import valor.Characters.Hero;
import valor.world.Lane;
import valor.world.Position;
import valor.world.World;

// draws the board as text, with row numbers down the left and lane names along the bottom.
// every cell is 2 characters wide so a hero label like H1 fits:
//     0   [N ] [N ] [X ] [N ] [N ] [X ] [N ] [N ]
//     1   [P ] [B ] [X ] [C ] [P ] [X ] [O ] [K ]
//     ...
//     7   [H1] [N ] [X ] [H2] [N ] [X ] [H3] [N ]
//            Top            Mid            Bot
public class WorldView {
    private static final String ROW_PREFIX = "    0   "; // how much room a row number takes up
    private static final int CELL_WIDTH = 5;             // "[N ] "
    private static final int CELL_INSIDE = 2;            // room between the brackets

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

    // a hero's label (H1) covers the tile symbol, otherwise the tile's terrain is shown
    private static String cell(World world, Position position) {
        String text = String.valueOf(world.tileAt(position).getSymbol());
        Hero hero = world.heroAt(position);
        if (hero != null) {
            text = hero.getLabel();
        }
        StringBuilder inside = new StringBuilder(text);
        while (inside.length() < CELL_INSIDE) {
            inside.append(' ');
        }
        return "[" + inside + "] ";
    }

    // puts each lane's name under the middle of its two columns
    private static String laneLabels() {
        StringBuilder labels = new StringBuilder();
        for (Lane lane : Lane.values()) {
            int start = ROW_PREFIX.length() + lane.getLeftCol() * CELL_WIDTH + 3;
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
