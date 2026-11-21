package valor.Characters;

import java.util.ArrayList;

// The player's team of heroes

public class Party {
    private ArrayList<Hero> heroes = new ArrayList<Hero>();

    public void addHero(Hero hero) {
        heroes.add(hero);
    }

    public ArrayList<Hero> getHeroes() {
        return heroes;
    }

    public int size() {
        return heroes.size();
    }

    public int highestLevel() {
        int highest = 1;
        for (Hero hero : heroes) {
            if (hero.getLevel() > highest) {
                highest = hero.getLevel();
            }
        }
        return highest;
    }
}
