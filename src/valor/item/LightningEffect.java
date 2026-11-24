package valor.item;

import valor.Characters.Monster;
import valor.Rules;

public class LightningEffect implements SpellEffect {
    @Override
    public String apply(Monster target) {
        target.reduceDodge(Rules.SPELL_EFFECT);
        return target.getName() + "'s dodge chance was reduced!";
    }
}
