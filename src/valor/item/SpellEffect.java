package valor.item;

import valor.Characters.Monster;

// Strategy pattern: the side effect a spell has on the monster it hits.
// A Spell does not care which effect it has, it just asks the strategy to apply itself.
public interface SpellEffect {
    // changes the monster and returns a message to show
    String apply(Monster target);

    // picks the effect that goes with a spell type
    static SpellEffect forType(SpellType type) {
        switch (type) {
            case ICE:
                return new IceEffect();
            case FIRE:
                return new FireEffect();
            default:
                return new LightningEffect();
        }
    }
}
