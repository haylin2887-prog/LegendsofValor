package valor.battle;

import valor.Characters.Hero;
import valor.Characters.Monster;
import valor.item.Spell;

// Observer #2: only counts things. Combat does not know it exists.
public class BattleStats extends BattleObserverAdapter {
    private int rounds = 0;
    private int monstersSpawned = 0;
    private int monstersDefeated = 0;
    private int heroFaints = 0;
    private int dodges = 0;
    private double damageDealt = 0;
    private double damageTaken = 0;

    @Override
    public void roundStarted(int round) {
        rounds = round;
    }

    @Override
    public void heroAttacked(Hero hero, Monster target, double damage) {
        damageDealt += damage;
    }

    @Override
    public void spellCast(Hero hero, Spell spell, Monster target, double damage, String effectText) {
        damageDealt += damage;
    }

    @Override
    public void attackDodged(String defenderName, String attackName) {
        dodges += 1;
    }

    @Override
    public void monsterDefeated(Monster monster) {
        monstersDefeated += 1;
    }

    @Override
    public void monsterSpawned(Monster monster) {
        monstersSpawned += 1;
    }

    @Override
    public void monsterAttacked(Monster monster, Hero target, double damage) {
        damageTaken += damage;
    }

    @Override
    public void heroFainted(Hero hero) {
        heroFaints += 1;
    }

    public String summary() {
        return "Game summary: " + rounds + " round(s), " + Math.round(damageDealt) + " damage dealt, "
                + Math.round(damageTaken) + " damage taken, " + monstersDefeated + "/" + monstersSpawned
                + " monster(s) defeated, " + heroFaints + " hero faint(s), " + dodges + " dodge(s).";
    }
}
