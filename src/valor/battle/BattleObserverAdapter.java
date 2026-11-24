package valor.battle;

import valor.Characters.Hero;
import valor.Characters.Monster;
import valor.item.Spell;

// Adapter pattern: the interface has many methods, but most observers only care about a few.
// This class implements all of them with empty bodies, so a subclass only overrides what it needs.
public abstract class BattleObserverAdapter implements BattleObserver {

    @Override
    public void roundStarted(int round) {
    }
    @Override
    public void heroAttacked(Hero hero, Monster target, double damage) {
    }
    @Override
    public void spellCast(Hero hero, Spell spell, Monster target, double damage, String effectText) {
    }
    @Override
    public void attackDodged(String defenderName, String attackName) {
    }
    @Override
    public void monsterDefeated(Monster monster) {
    }
    @Override
    public void monsterAttacked(Monster monster, Hero target, double damage) {
    }
    @Override
    public void heroFainted(Hero hero) {
    }
    @Override
    public void heroRespawned(Hero hero) {
    }
    @Override
    public void monsterSpawned(Monster monster) {
    }
    @Override
    public void heroRewarded(Hero hero, int xp, int gold, boolean leveledUp) {
    }
    @Override
    public void roundEnded() {
    }
    @Override
    public void heroesWon(Hero hero) {
    }
    @Override
    public void monstersWon(Monster monster) {
    }
}
