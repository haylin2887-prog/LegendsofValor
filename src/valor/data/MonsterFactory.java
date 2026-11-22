package valor.data;

import valor.Characters.Monster;

// anything that can build a monster, such as (name, level, damage, defense, dodge) -> new Dragon(...)
public interface MonsterFactory {
    Monster create(String name, int level, double damage, double defense, double dodge);
}
