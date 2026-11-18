package valor.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// builds the 8x8 board.
// the layout is fixed: row 0 is the monsters' Nexus, row 7 is the heroes' Nexus,
// and columns 2 and 5 are walls. only the 36 lane cells in rows 1-6 get random terrain.
public class GenerateWorld {
    public static final int WORLD_SIZE = 8;

    // share of the lane cells for each terrain type; whatever is left over is plain
    private static final double BUSH_SHARE = 0.1;  // 36 x 0.1 then take the ceiling.
    private static final double CAVE_SHARE = 0.1;
    private static final double KOULOU_SHARE = 0.1;
    private static final double OBSTACLE_SHARE = 0.1;

    // 64 total tiles
    // 12 nexus tiles
    // 16 walls
    // = 36 accessible tiles
    // take the ceiling so 4 bushes, caves, koulou, obstacle tiles leaving 20 plain tiles.

    // takes a Random value as seed
    public static Tile[][] generateWorld(Random random) {
        Tile[][] world = placeTiles(random);
        // keep trying until every lane has a path from the heroes' Nexus to the monsters' Nexus
        while (!everyLaneConnected(world)) {
            world = placeTiles(random);
        }
        return world;
    }

    // places the fixed walls and Nexus rows, then shuffles terrain into the lane cells
    private static Tile[][] placeTiles(Random random) {
        Tile[][] world = new Tile[WORLD_SIZE][WORLD_SIZE];
        List<Position> laneSpots = new ArrayList<>();

        for (int row = 0; row < WORLD_SIZE; row++) {
            for (int col = 0; col < WORLD_SIZE; col++) {
                if (Lane.isWall(col)) {
                    world[row][col] = new InaccessibleTile(row, col);
                } else if (row == 0) {
                    world[row][col] = new NexusTile(row, col, NexusTile.Owner.MONSTERS);
                } else if (row == WORLD_SIZE - 1) {
                    world[row][col] = new NexusTile(row, col, NexusTile.Owner.HEROES);
                } else {
                    laneSpots.add(new Position(row, col)); // filled in below
                }
            }
        }

        Collections.shuffle(laneSpots, random);

        // 4 of each special type, the remaining 20 are plain
        int total = laneSpots.size();
        int bushEnd = countOf(BUSH_SHARE, total);
        int caveEnd = bushEnd + countOf(CAVE_SHARE, total);
        int koulouEnd = caveEnd + countOf(KOULOU_SHARE, total);
        int obstacleEnd = koulouEnd + countOf(OBSTACLE_SHARE, total);

        // the spots are shuffled, so handing them out in order gives a random layout
        for (int i = 0; i < total; i++) {
            int row = laneSpots.get(i).getRow();
            int col = laneSpots.get(i).getCol();
            if (i < bushEnd) {
                world[row][col] = new BushTile(row, col);
            } else if (i < caveEnd) {
                world[row][col] = new CaveTile(row, col);
            } else if (i < koulouEnd) {
                world[row][col] = new KoulouTile(row, col);
            } else if (i < obstacleEnd) {
                world[row][col] = new ObstacleTile(row, col);
            } else {
                world[row][col] = new PlainTile(row, col);
            }
        }
        return world;
    }

    // rounds up, so 10% of 36 is 4, and any share above 0 gives at least 1 tile
    private static int countOf(double share, int total) {
        return (int) Math.ceil(total * share);
    }

    // package-private (no "private") so GenerateWorldTest can check hand-built boards
    static boolean everyLaneConnected(Tile[][] world) {
        for (Lane lane : Lane.values()) {
            if (!laneConnected(world, lane)) {
                return false;
            }
        }
        return true;
    }

    // true if something starting on this lane's heroes' Nexus can walk to its monsters' Nexus.
    // the walls keep the walk inside the lane
    private static boolean laneConnected(Tile[][] world, Lane lane) {
        boolean[][] seen = new boolean[WORLD_SIZE][WORLD_SIZE];
        int heroRow = WORLD_SIZE - 1;
        markReachable(world, seen, heroRow, lane.getLeftCol()); // start left tile on hero nexus
        markReachable(world, seen, heroRow, lane.getRightCol()); // start right tile on hero nexus
        return seen[0][lane.getLeftCol()] || seen[0][lane.getRightCol()]; // the enemy nexus tiles
    }

    // walks outward from one tile, marking every tile it can reach
    private static void markReachable(Tile[][] world, boolean[][] seen, int row, int col) {
        boolean offMap = row < 0 || row >= WORLD_SIZE || col < 0 || col >= WORLD_SIZE;
        if (offMap || seen[row][col] || !canPassThrough(world[row][col])) {
            return;
        }
        seen[row][col] = true;
        markReachable(world, seen, row - 1, col); // up
        markReachable(world, seen, row + 1, col); // down
        markReachable(world, seen, row, col - 1); // left
        markReachable(world, seen, row, col + 1); // Right
    }

    // obstacles count as open here because a hero can remove them
    private static boolean canPassThrough(Tile tile) {
        return tile.isAccessible() || tile.isRemovable();
    }
}
