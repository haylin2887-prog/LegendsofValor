package valor.battle;

import valor.Characters.Hero;
import valor.Characters.Monster;
import valor.item.Spell;

// Adapter pattern: the interface has many methods, but most observers only care about a few.
// This class implements all of them with empty bodies, so a subclass only overrides what it needs.
public abstract class BattleObserverAdapter implements BattleObserver {
    public void roundStarted(int round) {
    }

    public void heroAttacked(Hero hero, Monster target, double damage) {
    }

    public void spellCast(Hero hero, Spell spell, Monster target, double damage, String effectText) {
    }

    public void attackDodged(String defenderName, String attackName) {
    }

    public void monsterDefeated(Monster monster) {
    }

    public void monsterAttacked(Monster monster, Hero target, double damage) {
    }

    public void heroFainted(Hero hero) {
    }

    public void heroRespawned(Hero hero) {
    }

    public void monsterSpawned(Monster monster) {
    }

    public void heroRewarded(Hero hero, int xp, int gold, boolean leveledUp) {
    }

    public void roundEnded() {
    }

    public void heroesWon(Hero hero) {
    }

    public void monstersWon(Monster monster) {
    }
}
