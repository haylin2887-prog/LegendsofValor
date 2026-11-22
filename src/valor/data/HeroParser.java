package valor.data;

import valor.Characters.Hero;

// Name/mana/strength/agility/dexterity/starting money/starting experience.
// the factory decides which kind of hero (Warrior, Sorcerer, Paladin) the row becomes
public class HeroParser implements RecordParser<Hero> {
    private final HeroFactory factory;

    public HeroParser(HeroFactory factory) {
        this.factory = factory;
    }

    @Override
    public Hero parse(String[] row) {
        String name = row[0];
        double mp = Double.parseDouble(row[1]);
        double strength = Double.parseDouble(row[2]);
        double agility = Double.parseDouble(row[3]);
        double dexterity = Double.parseDouble(row[4]);
        int gold = Integer.parseInt(row[5]);
        int xp = Integer.parseInt(row[6]);
        return factory.create(name, mp, strength, agility, dexterity, gold, xp);
    }
}
