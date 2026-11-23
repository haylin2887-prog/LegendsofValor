package valor.battle;

import valor.Characters.Hero;
import valor.Characters.Monster;
import valor.item.Spell;

// Observer #1: prints what happened ("who did what") for the player.
public class BattleLogger extends BattleObserverAdapter {
    @Override
    public void roundStarted(int round) {
        System.out.println("\n################ ROUND " + round + " ################");
    }

    @Override
    public void heroAttacked(Hero hero, Monster target, double damage) {
        System.out.println("  >> " + hero.tag() + " attacked " + target.tag() + " for " + Math.round(damage) + " damage!");
    }

    @Override
    public void spellCast(Hero hero, Spell spell, Monster target, double damage, String effectText) {
        System.out.println("  >> " + hero.tag() + " cast " + spell.getName() + " on " + target.tag()
                + " for " + Math.round(damage) + " damage! " + effectText);
    }

    @Override
    public void attackDodged(String defenderName, String attackName) {
        System.out.println("  >> " + defenderName + " dodged " + attackName + "!");
    }

    @Override
    public void monsterDefeated(Monster monster) {
        System.out.println("  >> " + monster.tag() + " was defeated!");
    }

    @Override
    public void monsterAttacked(Monster monster, Hero target, double damage) {
        System.out.println("  >> " + monster.tag() + " attacked " + target.tag() + " for " + Math.round(damage) + " damage!");
    }

    @Override
    public void heroFainted(Hero hero) {
        System.out.println("  >> " + hero.tag() + " fainted! They respawn at their Nexus next round.");
    }

    @Override
    public void heroRespawned(Hero hero) {
        System.out.println("  >> " + hero.tag() + " respawned at their Nexus with full HP and MP.");
    }

    @Override
    public void monsterSpawned(Monster monster) {
        System.out.println("  >> A new monster appeared: " + monster.statusLine());
    }

    @Override
    public void heroRewarded(Hero hero, int xp, int gold, boolean leveledUp) {
        String text = "     " + hero.getName() + " earned " + xp + " XP and " + gold + " gold.";
        if (leveledUp) {
            text = text + "  LEVEL UP! Now level " + hero.getLevel() + "!";
        }
        System.out.println(text);
    }

    @Override
    public void roundEnded() {
        System.out.println("  (Surviving heroes recover 10% HP and MP.)");
    }

    @Override
    public void heroesWon(Hero hero) {
        System.out.println("\n*****************************************************");
        System.out.println("*  VICTORY! " + hero.tag() + " reached the Monsters' Nexus!");
        System.out.println("*****************************************************");
    }

    @Override
    public void monstersWon(Monster monster) {
        System.out.println("\n*****************************************************");
        System.out.println("*  DEFEAT... " + monster.tag() + " reached the Heroes' Nexus!");
        System.out.println("*****************************************************");
    }
}
