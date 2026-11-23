package valor.world;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import valor.Characters.Hero;
import valor.Characters.Monster;

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

    // the Nexus tile each hero spawned on
    private final Map<Hero, Position> heroHomes = new LinkedHashMap<>();   
    private final Map<Monster, Position> monsterPositions = new LinkedHashMap<>();

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

    public int monsterNexusRow() {
        return 0;
    }

    public int heroNexusRow() {
        return size() - 1;
    }

    // ---------- heroes ----------
    // puts hero i on the left column of lane i, on the heroes' Nexus (the bottom row),
    // and labels them H1, H2, H3 for the map
    public void spawnHeroes(List<Hero> heroes) {
        Lane[] lanes = Lane.values();
        if (heroes.size() > lanes.length) {
            throw new IllegalArgumentException("only one hero per lane: " + heroes.size() + " heroes");
        }
        int nexusRow = heroNexusRow();
        for (int i = 0; i < heroes.size(); i++) {
            Hero hero = heroes.get(i);
            hero.setLabel("H" + (i + 1));
            Position home = new Position(nexusRow, lanes[i].getLeftCol());
            heroHomes.put(hero, home);
            placeHero(hero, home);
        }
    }

    // moves the hero to a tile. the old terrain bonus ends and the new tile's bonus (if any) starts
    public void placeHero(Hero hero, Position position) {
        heroPositions.put(hero, position);
        hero.applyTerrainBonus(tileAt(position).getBonusStat());
    }

    // null if the hero isn't on the board
    public Position positionOf(Hero hero) {
        return heroPositions.get(hero);
    }
    public Position homeOf(Hero hero) {
        return heroHomes.get(hero);
    }

    public Lane laneOf(Hero hero) {
        return Lane.of(heroPositions.get(hero).getCol());
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

    // ---------- monsters ----------
    public void placeMonster(Monster monster, Position position) {
        monsterPositions.put(monster, position);
    }

    public void removeMonster(Monster monster) {
        monsterPositions.remove(monster);
    }

    public Position positionOf(Monster monster) {
        return monsterPositions.get(monster);
    }

    public Lane laneOf(Monster monster) {
        return Lane.of(monsterPositions.get(monster).getCol());
    }

    public Monster monsterAt(Position position) {
        for (Map.Entry<Monster, Position> entry : monsterPositions.entrySet()) {
            if (entry.getValue().sameAs(position)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public List<Monster> getMonsters() {
        return new ArrayList<>(monsterPositions.keySet());
    }

    // ---------- general rules ----------
    public boolean inBounds(Position p) {
        return p.getRow() >= 0 && p.getRow() < size() && p.getCol() >= 0 && p.getCol() < size();
    }

    // on the board and not a wall or an obstacle
    public boolean canEnter(Position p) {
        return inBounds(p) && tileAt(p).isAccessible();
    }

    // is p behind a monster in its lane? ("behind" = closer to the monsters' Nexus than that monster)
    private boolean behindMonster(Position p) {
        Lane lane = Lane.of(p.getCol());
        for (Map.Entry<Monster, Position> entry : monsterPositions.entrySet()) {
            Position m = entry.getValue();
            if (Lane.of(m.getCol()) == lane && m.getRow() > p.getRow()) {
                return true;
            }
        }
        return false;
    }

    // is p behind a hero in its lane? ("behind" = closer to the heroes' Nexus than that hero)
    private boolean behindHero(Position p) {
        Lane lane = Lane.of(p.getCol());
        for (Map.Entry<Hero, Position> entry : heroPositions.entrySet()) {
            Position h = entry.getValue();
            if (!entry.getKey().isFainted() && Lane.of(h.getCol()) == lane && h.getRow() < p.getRow()) {
                return true;
            }
        }
        return false;
    }

    // attack range: the tile itself and the 8 tiles around it
    private boolean inRange(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow()) <= 1 && Math.abs(a.getCol() - b.getCol()) <= 1;
    }

    public List<Monster> monstersInRange(Hero hero) {
        List<Monster> found = new ArrayList<>();
        Position h = heroPositions.get(hero);
        for (Map.Entry<Monster, Position> entry : monsterPositions.entrySet()) {
            if (inRange(h, entry.getValue())) {
                found.add(entry.getKey());
            }
        }
        return found;
    }

    public List<Hero> heroesInRange(Monster monster) {
        List<Hero> found = new ArrayList<>();
        Position m = monsterPositions.get(monster);
        for (Map.Entry<Hero, Position> entry : heroPositions.entrySet()) {
            if (!entry.getKey().isFainted() && inRange(entry.getValue(), m)) {
                found.add(entry.getKey());
            }
        }
        return found;
    }

    // ---------- hero movement rules ----------
    // null if the hero may step onto p, otherwise the reason it may not
    public String checkHeroMove(Hero hero, Position p) {
        if (!inBounds(p)) {
            return "that is the edge of the world.";
        }
        Tile tile = tileAt(p);
        if (tile.isRemovable()) {
            return "an obstacle blocks that tile (use 'Remove obstacle' first).";
        }
        if (!tile.isAccessible()) {
            return "that tile is inaccessible.";
        }
        Hero other = heroAt(p);
        if (other != null && other != hero) {
            return "another hero is already on that tile.";
        }
        if (behindMonster(p)) {
            return "you can't move behind a monster without killing it first.";
        }
        return null;
    }

    // tiles next to the target hero where the hero can teleport to:
    // the target must be in another lane; beside the target or below it (never ahead of it),
    // not on another hero, and not behind a monster
    public List<Position> teleportOptions(Hero hero, Hero target) {
        List<Position> options = new ArrayList<>();
        Position t = heroPositions.get(target);
        if (laneOf(hero) == laneOf(target)) {
            return options;
        }
        Position[] candidates = {
            t.moved(Directions.LEFT), t.moved(Directions.RIGHT), t.moved(Directions.DOWN)
        };
        for (Position p : candidates) {
            boolean inTargetLane = inBounds(p) && Lane.of(p.getCol()) == laneOf(target);
            if (inTargetLane && canEnter(p) && heroAt(p) == null && !behindMonster(p)) {
                options.add(p);
            }
        }
        return options;
    }

    // null if the hero may recall to its home Nexus tile, otherwise the reason it may not
    public String checkRecall(Hero hero) {
        Position home = heroHomes.get(hero);
        if (heroPositions.get(hero).sameAs(home)) {
            return hero.getName() + " is already at the home Nexus.";
        }
        Hero other = heroAt(home);
        if (other != null && other != hero) {
            return "another hero is standing on your Nexus tile.";
        }
        return null;
    }

    // where a fainted hero respawns: its home Nexus tile, or the other Nexus tile of its lane.
    // null if both are taken (it waits another round)
    public Position respawnSpot(Hero hero) {
        Position home = heroHomes.get(hero);
        Position beside = home.moved(Directions.RIGHT);
        if (heroAt(home) == null) {
            return home;
        }
        if (heroAt(beside) == null) {
            return beside;
        }
        return null;
    }

    // obstacles directly above, below, left or right of the hero
    public List<Position> adjacentObstacles(Hero hero) {
        List<Position> found = new ArrayList<>();
        Position h = heroPositions.get(hero);
        for (Directions direction : Directions.values()) {
            Position p = h.moved(direction);
            if (inBounds(p) && tileAt(p).isRemovable()) {
                found.add(p);
            }
        }
        return found;
    }

    // the obstacle turns into a plain tile
    public void removeObstacle(Position p) {
        world[p.getRow()][p.getCol()] = new PlainTile(p.getRow(), p.getCol());
    }

    // ---------- monster movement rules ----------
    // a monster may enter a tile that is open, has no other monster and is not behind a hero
    public boolean monsterCanEnter(Position p) {
        return canEnter(p) && monsterAt(p) == null && !behindHero(p);
    }

    // ---------- win / lose ----------
    // a hero on the monsters' Nexus wins the game
    public boolean heroesWon() {
        for (Map.Entry<Hero, Position> entry : heroPositions.entrySet()) {
            if (!entry.getKey().isFainted() && entry.getValue().getRow() == monsterNexusRow()) {
                return true;
            }
        }
        return false;
    }

    // a monster on the heroes' Nexus wins the game for the monsters
    public Monster monsterAtHeroNexus() {
        for (Map.Entry<Monster, Position> entry : monsterPositions.entrySet()) {
            if (entry.getValue().getRow() == heroNexusRow()) {
                return entry.getKey();
            }
        }
        return null;
    }

    // the hero standing on the monsters' Nexus (null if nobody)
    public Hero heroAtMonsterNexus() {
        for (Map.Entry<Hero, Position> entry : heroPositions.entrySet()) {
            if (!entry.getKey().isFainted() && entry.getValue().getRow() == monsterNexusRow()) {
                return entry.getKey();
            }
        }
        return null;
    }
}
