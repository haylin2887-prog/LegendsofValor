package valor;

import java.util.Random;

public class Rules {
    public static final int HEROES = 3;                     // one hero per lane
    public static final double TERRAIN_BONUS = 0.10;        // Bush / Cave / Koulou: +10% to one stat
    public static final int SPAWN_EASY = 8;                 // a new monster wave every N rounds
    public static final int SPAWN_MEDIUM = 6;
    public static final int SPAWN_HARD = 4;                 // on hard, monsters also get terrain bonuses

    public static final int HP_PER_LEVEL = 100;
    public static final int XP_PER_LEVEL = 10;
    public static final double LEVEL_UP_SKILL_GAIN = 0.05;
    public static final double FAVORED_EXTRA_GAIN = 0.05;
    public static final double FAVORED_START_BONUS = 0.10;
    public static final double MP_LEVEL_UP = 1.1;
    public static final double REGEN_PCT = 0.10;            // end of round: +10% of max HP and max MP

    public static final double ATTACK_FACTOR = 0.05;
    public static final double TWO_HAND_BONUS = 1.5;
    public static final double HERO_DODGE_FACTOR = 0.002;
    public static final double MONSTER_DODGE_FACTOR = 0.01;
    public static final double MAX_DODGE = 0.5;
    public static final double DEFENSE_FACTOR = 0.05;
    public static final double ARMOR_FACTOR = 0.05;
    public static final double SPELL_SCALE = 0.1;
    public static final double SPELL_EFFECT = 0.1;
    public static final double MONSTER_DAMAGE_FACTOR = 0.2;
    public static final double MONSTER_FAVORED_BONUS = 1.1;

    public static final int XP_PER_MONSTER_LEVEL = 2;
    public static final int GOLD_PER_MONSTER_LEVEL = 500;
    public static final int WEAPON_USES = 10;
    public static final int ARMOR_USES = 10;
    public static final int POTION_USES = 1;
    public static final int SPELL_USES = 1;
    public static final int SELL_DIVISOR = 2;   // items sell for half price, and repairs cost half price

    private static Random random = new Random();

    private Rules() {
    }

   
    public static boolean chance(double probability) {
        return random.nextDouble() < probability;
    }

    public static int randomInt(int bound) {
        return random.nextInt(bound);
    }

    public static Random getRandom() {
        return random;
    }
}
