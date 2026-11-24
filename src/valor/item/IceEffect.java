package valor.item;

import valor.Characters.Monster;
import valor.Rules;

public class IceEffect implements SpellEffect {
    @Override
    public String apply(Monster target) {
        target.reduceDamage(Rules.SPELL_EFFECT);
        return target.getName() + "'s damage was reduced!";
    }
}
