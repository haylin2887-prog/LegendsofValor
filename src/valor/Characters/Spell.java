package valor.Characters;

import valor.Rules;

public class Spell {
    // strategy pattern

    // A single-use magic attack. The extra effect (ice, fire, lightning) is a Strategy object.
    // The spell does not care which effect it has; it just asks the strategy.

    // The spell has three other types.
/* class IceEffect implements SpellEffect {
    public String apply(Monster target) {
        target.reduceDamage(Rules.SPELL_EFFECT);
        return target.getName() + "'s damage was reduced!";
    }

    public String getName() {
        return "Ice";
    }
}

class LightningEffect implements SpellEffect {
    public String apply(Monster target) {
        target.reduceDodge(Rules.SPELL_EFFECT);
        return target.getName() + "'s dodge chance was reduced!";
    }

    public String getName() {
        return "Lightning";
    }
}

class FireEffect implements SpellEffect {
    public String apply(Monster target) {
        target.reduceDefense(Rules.SPELL_EFFECT);
        return target.getName() + "'s defense was reduced!";
    }

    public String getName() {
        return "Fire";
    }*/
}
