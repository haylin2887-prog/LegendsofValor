package valor.data;

import valor.Characters.Hero;

// anything that can build a hero, such as (name, mp, str, agi, dex, gold, xp) -> new Warrior(...)
public interface HeroFactory {
    Hero create(String name, double mp, double strength, double agility, double dexterity, int gold, int xp);
}
