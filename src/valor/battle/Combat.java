package valor.battle;

import java.util.ArrayList;
import java.util.List;
import valor.Characters.Hero;
import valor.Characters.Monster;
import valor.Characters.Party;
import valor.Rules;
import valor.item.Spell;
import valor.world.World;

// Resolves single attacks and spells: dodge, damage, equipment wear, rewards.
// It knows nothing about the board: the world checks range and movement BEFORE calling Combat.
// When a monster dies, Combat tells the world to take it off the grid.
public class Combat {
    private final Party party;
    private final World world;
    private final List<BattleObserver> observers = new ArrayList<>();

    public Combat(Party party, World world) {
        this.party = party;
        this.world = world;
    }

    public void addObserver(BattleObserver observer) {
        observers.add(observer);
    }

    // ---------- events the game loop reports (Game calls these) ----------
    public void notifyRoundStarted(int round) {
        for (BattleObserver observer : observers) {
            observer.roundStarted(round);
        }
    }

    public void notifyRoundEnded() {
        for (BattleObserver observer : observers) {
            observer.roundEnded();
        }
    }

    public void notifyHeroRespawned(Hero hero) {
        for (BattleObserver observer : observers) {
            observer.heroRespawned(hero);
        }
    }

    public void notifyMonsterSpawned(Monster monster) {
        for (BattleObserver observer : observers) {
            observer.monsterSpawned(monster);
        }
    }

    public void notifyHeroesWon(Hero hero) {
        for (BattleObserver observer : observers) {
            observer.heroesWon(hero);
        }
    }

    public void notifyMonstersWon(Monster monster) {
        for (BattleObserver observer : observers) {
            observer.monstersWon(monster);
        }
    }

    // ---------- heroes ----------
    public void heroAttack(Hero hero, Monster target) {
        if (target.tryDodge()) {
            notifyAttackDodged(target.tag(), hero.getName() + "'s attack");
            return;
        }
        double damage = target.afterDefense(hero.attackDamage());
        target.takeDamage(damage);
        notifyHeroAttacked(hero, target, damage);
        hero.weaponUsed();
        checkDefeated(target);
    }

    // returns false (and does nothing) if the hero has not enough mana
    public boolean heroCast(Hero hero, Spell spell, Monster target) {
        if (!hero.spendMana(spell.getManaCost())) {
            return false;
        }
        spell.use();
        hero.removeItem(spell);   // a spell can only be cast once
        if (target.tryDodge()) {
            notifyAttackDodged(target.tag(), spell.getName());
            return true;
        }
        double damage = target.afterDefense(hero.spellDamage(spell));
        target.takeDamage(damage);
        String effectText = spell.applyEffect(target);
        notifySpellCast(hero, spell, target, damage, effectText);
        checkDefeated(target);
        return true;
    }

    // a dead monster leaves the board and every hero gets its XP and gold
    private void checkDefeated(Monster monster) {
        if (!monster.isFainted()) {
            return;
        }
        world.removeMonster(monster);
        notifyMonsterDefeated(monster);

        int xp = monster.getLevel() * Rules.XP_PER_MONSTER_LEVEL;
        int gold = monster.getLevel() * Rules.GOLD_PER_MONSTER_LEVEL;
        for (Hero hero : party.getHeroes()) {
            hero.earn(gold);
            boolean leveledUp = hero.gainExperience(xp);
            notifyHeroRewarded(hero, xp, gold, leveledUp);
        }
    }

    // ---------- monsters ----------
    public void monsterAttack(Monster monster, Hero target) {
        if (target.tryDodge()) {
            notifyAttackDodged(target.tag(), monster.getName() + "'s attack");
            return;
        }
        double damage = target.receiveDamage(monster.attackDamage());
        notifyMonsterAttacked(monster, target, damage);
        if (target.isFainted()) {
            target.clearTerrainBonus();
            notifyHeroFainted(target);
        }
    }

    // ---------- telling the observers (Observer pattern) ----------
    // One small method per event: loop over every observer and call the matching method.
    private void notifyAttackDodged(String defenderName, String attackName) {
        for (BattleObserver observer : observers) {
            observer.attackDodged(defenderName, attackName);
        }
    }

    private void notifyHeroAttacked(Hero hero, Monster target, double damage) {
        for (BattleObserver observer : observers) {
            observer.heroAttacked(hero, target, damage);
        }
    }

    private void notifySpellCast(Hero hero, Spell spell, Monster target, double damage, String effectText) {
        for (BattleObserver observer : observers) {
            observer.spellCast(hero, spell, target, damage, effectText);
        }
    }

    private void notifyMonsterDefeated(Monster monster) {
        for (BattleObserver observer : observers) {
            observer.monsterDefeated(monster);
        }
    }

    private void notifyHeroRewarded(Hero hero, int xp, int gold, boolean leveledUp) {
        for (BattleObserver observer : observers) {
            observer.heroRewarded(hero, xp, gold, leveledUp);
        }
    }

    private void notifyMonsterAttacked(Monster monster, Hero target, double damage) {
        for (BattleObserver observer : observers) {
            observer.monsterAttacked(monster, target, damage);
        }
    }

    private void notifyHeroFainted(Hero hero) {
        for (BattleObserver observer : observers) {
            observer.heroFainted(hero);
        }
    }
}
