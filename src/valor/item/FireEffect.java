package valor.item;

import valor.Characters.Monster;
import valor.Rules;

public class FireEffect implements SpellEffect {
    @Override
    public String apply(Monster target) {
        target.reduceDefense(Rules.SPELL_EFFECT);
        return target.getName() + "'s defense was reduced!";
    }
}
