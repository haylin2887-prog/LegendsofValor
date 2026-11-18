package valor.world;

// 8 x 8 grid of Tiles

//     0   [ ] [ ] [X] [ ] [ ] [X] [ ] [ ]
//     1   [ ] [ ] [X] [ ] [ ] [X] [ ] [ ]
//     2   [ ] [ ] [X] [ ] [ ] [X] [ ] [ ]   
//     3   [ ] [ ] [X] [ ] [ ] [X] [ ] [ ]
//     4   [ ] [ ] [X] [ ] [ ] [X] [ ] [ ]
//     5   [ ] [ ] [X] [ ] [ ] [X] [ ] [ ]
//     6   [ ] [ ] [X] [ ] [ ] [X] [ ] [ ]
//     7   [ ] [ ] [X] [ ] [ ] [X] [ ] [ ]
//           Top         Mid         Bot
          
//     - Rows 0 and 7 are nexus tiles
//     - Columns 2 and 5 are Inaccessible


public class World {
    private Tile[][] world;
    // grid knows the position of all Heroes.
    // held in a dictionary data structure keyed on hero's name
    private Position[] heroPositions;

    public World(Tile[][] world) {
        this.world = world;
    }

    // number of rows (and columns, the board is square)
    public int size() {
        return world.length;
    }

    public Tile tileAt(Position position) {
        return world[position.getRow()][position.getCol()];
    }
}
