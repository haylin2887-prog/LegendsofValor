package valor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import valor.Characters.GameData;
import valor.Characters.Hero;
import valor.Characters.Monster;
import valor.Characters.Party;
import valor.battle.BattleLogger;
import valor.battle.BattleStats;
import valor.battle.Combat;
import valor.item.Spell;
import valor.market.Market;
import valor.ui.Console;
import valor.ui.HeroMenu;
import valor.ui.MarketMenu;
import valor.ui.WorldView;
import valor.world.Directions;
import valor.world.Lane;
import valor.world.NexusTile;
import valor.world.Position;
import valor.world.Tile;
import valor.world.World;

// The game loop. One round = every hero takes ONE action, then every monster acts
// (attacks a hero in range, otherwise steps south). New monsters spawn every few rounds.
// The game ends when a hero reaches the monsters' Nexus (win) or a monster reaches the heroes' Nexus (lose).
public final class Game {
    // one thing a hero can type at the menu. run() returns true if it used up the hero's turn
    private interface Action {
        boolean run(Hero hero);
    }

    private static class Command {
        final String label;
        final boolean free;      // free commands (info, board, market) don't use the turn
        final Action action;

        Command(String label, boolean free, Action action) {
            this.label = label;
            this.free = free;
            this.action = action;
        }
    }

    private final Console console;
    private final GameData data;
    private final Party party;
    private final World world;
    private final Market market;
    private final MarketMenu marketMenu;
    private final HeroMenu heroMenu;
    private final Combat combat;
    private final BattleStats stats = new BattleStats();
    private final Map<String, Command> commands = new LinkedHashMap<>();   // menu key -> command

    private int round = 0;
    private int spawnInterval = Rules.SPAWN_EASY;
    private boolean hardMode = false;                  // hard: monsters also get terrain bonuses
    private int nextMonsterId = 1;
    private final boolean[] spawnPending = new boolean[Lane.values().length];
    private boolean gameOver = false;

    public Game(Console console, GameData data, Party party, World world, Market market, MarketMenu marketMenu) {
        this.console = console;
        this.data = data;
        this.party = party;
        this.world = world;
        this.market = market;
        this.marketMenu = marketMenu;
        this.heroMenu = new HeroMenu(console);
        this.combat = new Combat(party, world);
        combat.addObserver(new BattleLogger());   // prints the events
        combat.addObserver(stats);                // counts the events
        registerCommands();
    }

    // the menu a hero sees each turn. keys 1-9 use the turn, the letters are free
    public void registerCommands() {
        commands.put("1", new Command("Move", false, this::move));
        commands.put("2", new Command("Attack", false, this::attack));
        commands.put("3", new Command("Cast spell", false, this::castSpell));
        commands.put("4", new Command("Use potion", false, heroMenu::usePotion));
        commands.put("5", new Command("Equip", false, heroMenu::equip));
        commands.put("6", new Command("Teleport", false, this::teleport));
        commands.put("7", new Command("Recall", false, this::recall));
        commands.put("8", new Command("Remove obstacle", false, this::removeObstacle));
        commands.put("9", new Command("Pass turn", false, this::pass));
        commands.put("i", new Command("Info", true, this::info));
        commands.put("b", new Command("Show board", true, this::showBoard));
        commands.put("m", new Command("Market (at the Heroes' Nexus)", true, this::openMarket));
    }

    public void run() {
        chooseDifficulty();
        for (int lane = 0; lane < spawnPending.length; lane++) {
            spawnPending[lane] = true;
        }
        System.out.println("\nThree monsters appear at the Monsters' Nexus...");
        spawnWaiting();

        while (!gameOver) {
            round++;
            combat.notifyRoundStarted(round);
            respawnHeroes();
            heroesPhase();
            if (gameOver) {
                break;
            }
            monstersPhase();
            if (gameOver) {
                break;
            }
            endRound();
        }
        System.out.println(stats.summary());
        System.out.println("Thanks for playing Legends of Valor!");
    }

    private void chooseDifficulty() {
        System.out.println("Difficulty (how often a new wave of monsters spawns):");
        System.out.println("  1) Easy   - every " + Rules.SPAWN_EASY + " rounds");
        System.out.println("  2) Medium - every " + Rules.SPAWN_MEDIUM + " rounds");
        System.out.println("  3) Hard   - every " + Rules.SPAWN_HARD + " rounds, and monsters get terrain bonuses too");
        int choice = console.readNumber("> ", 1, 3);
        if (choice == 1) {
            spawnInterval = Rules.SPAWN_EASY;
        } else if (choice == 2) {
            spawnInterval = Rules.SPAWN_MEDIUM;
        } else {
            spawnInterval = Rules.SPAWN_HARD;
            hardMode = true;
        }
    }

    // ---------- start / end of a round ----------
    // heroes that fainted last round come back on their Nexus with full HP and MP
    private void respawnHeroes() {
        for (Hero hero : party.getHeroes()) {
            if (!hero.isFainted()) {
                continue;
            }
            Position spot = world.respawnSpot(hero);
            if (spot == null) {
                continue;   // both Nexus tiles of the lane are taken: wait another round
            }
            hero.respawn();
            world.placeHero(hero, spot);
            combat.notifyHeroRespawned(hero);
        }
    }

    private void endRound() {
        for (Hero hero : party.getHeroes()) {
            hero.regenerate();
        }
        combat.notifyRoundEnded();

        if (round % spawnInterval == 0) {
            for (int lane = 0; lane < spawnPending.length; lane++) {
                spawnPending[lane] = true;
            }
        }
        spawnWaiting();
    }

    // a new monster in every lane that is waiting for one (if its spawn tile is free).
    // its level is the highest hero level
    private void spawnWaiting() {
        Lane[] lanes = Lane.values();
        for (int i = 0; i < lanes.length; i++) {
            Position spawn = new Position(world.monsterNexusRow(), lanes[i].getRightCol());
            if (!spawnPending[i] || world.monsterAt(spawn) != null) {
                continue;
            }
            Monster monster = data.createMonster(party.highestLevel());
            monster.setLabel("M" + nextMonsterId);
            nextMonsterId++;
            world.placeMonster(monster, spawn);
            spawnPending[i] = false;
            combat.notifyMonsterSpawned(monster);
        }
    }

    // ---------- the heroes' turn ----------
    private void heroesPhase() {
        for (Hero hero : party.getHeroes()) {
            if (gameOver) {
                return;
            }
            if (hero.isFainted()) {
                continue;   // waiting for a free Nexus tile
            }
            takeTurn(hero);
        }
    }

    private void takeTurn(Hero hero) {
        System.out.println();
        System.out.println(WorldView.render(world));
        for (Hero h : party.getHeroes()) {
            Position p = world.positionOf(h);
            System.out.println("  " + h.statusLine(true) + "  @ row " + p.getRow() + ", col " + p.getCol());
        }
        System.out.println("\n=== " + hero.tag() + "'s turn ===");

        boolean turnUsed = false;
        while (!turnUsed && !gameOver) {
            printMenu(hero);
            String key = console.readLine("> ").toLowerCase();
            Command command = commands.get(key);
            if (command == null) {
                System.out.println("  Unknown command.");
            } else if (command.free) {
                command.action.run(hero);
            } else {
                turnUsed = command.action.run(hero);
            }
        }
    }

    private void printMenu(Hero hero) {
        Tile here = world.tileAt(world.positionOf(hero));
        System.out.println(hero.getName() + " is standing on: " + tileName(here));
        StringBuilder turnLine = new StringBuilder(" ");
        StringBuilder freeLine = new StringBuilder(" ");
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            String text = "  " + entry.getKey() + ") " + entry.getValue().label;
            if (entry.getValue().free) {
                freeLine.append(text);
            } else {
                turnLine.append(text);
            }
        }
        System.out.println(turnLine);
        System.out.println(freeLine + "  (the letters don't use your turn)");
    }

    private String tileName(Tile tile) {
        switch (tile.getSymbol()) {
            case 'N':
                return "Nexus";
            case 'B':
                return "Bush (+10% DEX)";
            case 'C':
                return "Cave (+10% AGI)";
            case 'K':
                return "Koulou (+10% STR)";
            default:
                return "Plain";
        }
    }

    // ---------- the commands ----------
    private boolean move(Hero hero) {
        Directions direction = askDirection();
        if (direction == null) {
            return false;
        }
        Position target = world.positionOf(hero).moved(direction);
        String problem = world.checkHeroMove(hero, target);
        if (problem != null) {
            System.out.println("  Illegal move: " + problem);
            return false;
        }
        world.placeHero(hero, target);
        System.out.println("  " + hero.getName() + " moved to row " + target.getRow() + ", col " + target.getCol()
                + " (" + tileName(world.tileAt(target)) + ").");
        checkHeroesWon();
        return true;
    }

    // W = north (towards the monsters' Nexus), S = south, A = west, D = east
    private Directions askDirection() {
        while (true) {
            String key = console.readLine("  Move W(up) A(left) S(down) D(right), 0 to cancel: ").toLowerCase();
            switch (key) {
                case "w":
                    return Directions.UP;
                case "s":
                    return Directions.DOWN;
                case "a":
                    return Directions.LEFT;
                case "d":
                    return Directions.RIGHT;
                case "0":
                    return null;
                default:
                    System.out.println("  Please type W, A, S, D or 0.");
            }
        }
    }

    private boolean attack(Hero hero) {
        List<Monster> targets = world.monstersInRange(hero);
        if (targets.isEmpty()) {
            System.out.println("  No monster is in range (your tile or any tile around it).");
            return false;
        }
        Monster target = heroMenu.chooseTarget(targets);
        if (target == null) {
            return false;
        }
        combat.heroAttack(hero, target);
        return true;
    }

    private boolean castSpell(Hero hero) {
        if (hero.getItems(Spell.class).isEmpty()) {
            System.out.println("  " + hero.getName() + " has no spells.");
            return false;
        }
        List<Monster> targets = world.monstersInRange(hero);
        if (targets.isEmpty()) {
            System.out.println("  No monster is in range (your tile or any tile around it).");
            return false;
        }
        Spell spell = heroMenu.chooseSpell(hero);
        if (spell == null) {
            return false;
        }
        if (hero.getMp() < spell.getManaCost()) {
            System.out.println("  Not enough mana (" + Math.round(hero.getMp()) + "/" + spell.getManaCost() + ").");
            return false;
        }
        Monster target = heroMenu.chooseTarget(targets);
        if (target == null) {
            return false;
        }
        return combat.heroCast(hero, spell, target);
    }

    // jump next to a hero in another lane: beside them or behind them, never ahead
    private boolean teleport(Hero hero) {
        List<Hero> friends = new ArrayList<>();
        for (Hero other : party.getHeroes()) {
            if (other != hero && !other.isFainted() && world.laneOf(other) != world.laneOf(hero)) {
                friends.add(other);
            }
        }
        if (friends.isEmpty()) {
            System.out.println("  There is no hero in another lane to teleport to.");
            return false;
        }
        List<String> heroLabels = new ArrayList<>();
        for (Hero friend : friends) {
            heroLabels.add(friend.tag() + " " + where(friend));
        }
        Hero target = heroMenu.pick("Teleport next to which hero?", friends, heroLabels);
        if (target == null) {
            return false;
        }
        List<Position> spots = world.teleportOptions(hero, target);
        if (spots.isEmpty()) {
            System.out.println("  No legal tile next to " + target.getName()
                    + " (not ahead of them, not on a hero, not behind a monster).");
            return false;
        }
        List<String> spotLabels = new ArrayList<>();
        for (Position p : spots) {
            spotLabels.add("row " + p.getRow() + ", col " + p.getCol()
                    + " (" + tileName(world.tileAt(p)) + ")");
        }
        Position spot = heroMenu.pick("Teleport to which tile?", spots, spotLabels);
        if (spot == null) {
            return false;
        }
        world.placeHero(hero, spot);
        System.out.println("  " + hero.getName() + " teleported to row " + spot.getRow() + ", col " + spot.getCol() + ".");
        return true;
    }

    private String where(Hero hero) {
        Position p = world.positionOf(hero);
        return "@ row " + p.getRow() + ", col " + p.getCol();
    }

    // back to the Nexus tile the hero spawned on
    private boolean recall(Hero hero) {
        String problem = world.checkRecall(hero);
        if (problem != null) {
            System.out.println("  Can't recall: " + problem);
            return false;
        }
        world.placeHero(hero, world.homeOf(hero));
        System.out.println("  " + hero.getName() + " recalled to the home Nexus.");
        return true;
    }

    private boolean removeObstacle(Hero hero) {
        List<Position> obstacles = world.adjacentObstacles(hero);
        if (obstacles.isEmpty()) {
            System.out.println("  There is no obstacle next to " + hero.getName() + ".");
            return false;
        }
        List<String> obstacleLabels = new ArrayList<>();
        for (Position p : obstacles) {
            obstacleLabels.add("row " + p.getRow() + ", col " + p.getCol());
        }
        Position spot = heroMenu.pick("Remove which obstacle?", obstacles, obstacleLabels);
        if (spot == null) {
            return false;
        }
        world.removeObstacle(spot);
        System.out.println("  " + hero.getName() + " cleared the obstacle. The tile is now Plain.");
        return true;
    }

    private boolean pass(Hero hero) {
        System.out.println("  " + hero.getName() + " passes the turn.");
        return true;
    }

    // ---------- free commands ----------
    private boolean info(Hero hero) {
        for (Hero h : party.getHeroes()) {
            System.out.println("  " + h.statusLine(false));
        }
        return false;
    }

    private boolean showBoard(Hero hero) {
        System.out.println(WorldView.render(world));
        return false;
    }

    private boolean openMarket(Hero hero) {
        Tile tile = world.tileAt(world.positionOf(hero));
        boolean atHeroNexus = tile instanceof NexusTile && ((NexusTile) tile).isHeroNexus();
        if (!atHeroNexus) {
            System.out.println("  The market is only open at the Heroes' Nexus.");
            return false;
        }
        marketMenu.open(hero, market);
        return false;
    }

    // ---------- win / lose ----------
    private void checkHeroesWon() {
        Hero winner = world.heroAtMonsterNexus();
        if (winner != null) {
            gameOver = true;
            combat.notifyHeroesWon(winner);
        }
    }

    // simple insertion sort: the monster with the biggest row number (closest to the heroes' Nexus) first
    private void sortByRowDescending(List<Monster> monsters) {
        for (int i = 1; i < monsters.size(); i++) {
            Monster current = monsters.get(i);
            int currentRow = world.positionOf(current).getRow();
            int j = i - 1;
            while (j >= 0 && world.positionOf(monsters.get(j)).getRow() < currentRow) {
                monsters.set(j + 1, monsters.get(j));
                j--;
            }
            monsters.set(j + 1, current);
        }
    }

    // ---------- the monsters' turn ----------
    private void monstersPhase() {
        System.out.println("\n--- Monsters' turn ---");
        // the monsters closest to the heroes' Nexus go first, so a lane never jams
        List<Monster> order = world.getMonsters();
        sortByRowDescending(order);

        for (Monster monster : order) {
            if (gameOver) {
                return;
            }
            List<Hero> inRange = world.heroesInRange(monster);
            if (!inRange.isEmpty()) {
                Hero target = inRange.get(Rules.randomInt(inRange.size()));
                combat.monsterAttack(monster, target);
            } else {
                moveMonster(monster);
            }
        }
    }

    // a monster steps south. if that is blocked it steps sideways inside its lane
    private void moveMonster(Monster monster) {
        Position now = world.positionOf(monster);
        Position south = now.moved(Directions.DOWN);
        Position next = null;
        if (world.monsterCanEnter(south)) {
            next = south;
        } else {
            for (Directions side : new Directions[] {Directions.LEFT, Directions.RIGHT}) {
                Position beside = now.moved(side);
                boolean sameLane = world.inBounds(beside) && Lane.of(beside.getCol()) == Lane.of(now.getCol());
                if (sameLane && world.monsterCanEnter(beside) && world.canEnter(beside.moved(Directions.DOWN))) {
                    next = beside;
                    break;
                }
            }
        }
        if (next == null) {
            return;   // stuck this turn
        }
        world.placeMonster(monster, next);
        if (hardMode) {
            monster.applyTerrainBonus(world.tileAt(next).getBonusStat());
        }
        Monster arrived = world.monsterAtHeroNexus();
        if (arrived != null) {
            gameOver = true;
            combat.notifyMonstersWon(arrived);
        }
    }
}
