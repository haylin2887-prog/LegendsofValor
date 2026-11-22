package valor.data;

import valor.Characters.Monster;

// Name/level/damage/defense/dodge chance.
// the factory decides which kind of monster (Dragon, Exoskeleton, Spirit) the row becomes
public class MonsterParser implements RecordParser<Monster> {
    private final MonsterFactory factory;

    public MonsterParser(MonsterFactory factory) {
        this.factory = factory;
    }

    @Override
    public Monster parse(String[] row) {
        String name = row[0];
        int level = Integer.parseInt(row[1]);
        double damage = Double.parseDouble(row[2]);
        double defense = Double.parseDouble(row[3]);
        double dodge = Double.parseDouble(row[4]);
        return factory.create(name, level, damage, defense, dodge);
    }
}
