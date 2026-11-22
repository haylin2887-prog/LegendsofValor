package valor.world;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import valor.Characters.Hero;

// 8 x 8 grid of Tiles

//     0   [ ] [ ] [X] [ ] [ ] [X] [ ] [ ]
//     1   [ ] [ ] [X] [ ] [ ] [X] [ ] [ ]
//     2   [ ] [ ] [X] [ ] [ ] [X] [ ] [ ]   
//     3   [ ] [ ] [X] [ ] [ ] [X] [ ] [ ]
//     4   [ ] [ ] [X] [ ] [ ] [X] [ ] [ ]
//     5   [ ] [ ] [X] [ ] [ ] [X] [ ] [ ]
//     6   [ ] [ ] [X] [ ] [ ] [X] [ ] [ ]
//     7   [H1] [ ] [X] [H2] [ ] [X] [H3] [ ]
//           Top         Mid         Bot
          
//     - Rows 0 and 7 are nexus tiles
//     - Columns 2 and 5 are Inaccessible


public class World {
    private final Tile[][] world;
    // grid knows the position of all Heroes.
    // held in a dictionary keyed on the hero. LinkedHashMap keeps them in spawn order (H1, H2, H3)
    private final Map<Hero, Position> heroPositions = new LinkedHashMap<>();

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

    // puts hero i on the left column of lane i, on the heroes' Nexus (the bottom row),
    // and labels them H1, H2, H3 for the map
    public void spawnHeroes(List<Hero> heroes) {
        Lane[] lanes = Lane.values();
        if (heroes.size() > lanes.length) {
            throw new IllegalArgumentException("only one hero per lane: " + heroes.size() + " heroes");
        }
        int nexusRow = size() - 1;
        for (int i = 0; i < heroes.size(); i++) {
            Hero hero = heroes.get(i);
            hero.setLabel("H" + (i + 1));
            placeHero(hero, new Position(nexusRow, lanes[i].getLeftCol()));
        }
    }

    public void placeHero(Hero hero, Position position) {
        heroPositions.put(hero, position);
    }

    // null if the hero isn't on the board
    public Position positionOf(Hero hero) {
        return heroPositions.get(hero);
    }

    // the hero standing here, or null if there is none
    public Hero heroAt(Position position) {
        for (Map.Entry<Hero, Position> entry : heroPositions.entrySet()) {
            if (entry.getValue().sameAs(position)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
