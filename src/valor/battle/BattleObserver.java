package valor.battle;

import valor.Characters.Hero;
import valor.Characters.Monster;
import valor.item.Spell;

// Observer pattern: Combat (and the game loop) tell every observer what happens.
// The observers decide what to do with the news (print it, count it).
public interface BattleObserver {
    void roundStarted(int round);

    void heroAttacked(Hero hero, Monster target, double damage);

    void spellCast(Hero hero, Spell spell, Monster target, double damage, String effectText);

    void attackDodged(String defenderName, String attackName);

    void monsterDefeated(Monster monster);

    void monsterAttacked(Monster monster, Hero target, double damage);

    void heroFainted(Hero hero);

    void heroRespawned(Hero hero);

    void monsterSpawned(Monster monster);

    void heroRewarded(Hero hero, int xp, int gold, boolean leveledUp);

    void roundEnded();

    void heroesWon(Hero hero);

    void monstersWon(Monster monster);
}
