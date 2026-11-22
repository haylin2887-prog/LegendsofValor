package valor.world;

// the top and bottom row of each lane. heroes and monsters spawn on their own side's Nexus
public class NexusTile extends Tile {
    public enum Owner { HEROES, MONSTERS }

    private final Owner owner;

    public NexusTile(int row, int col, Owner owner) {
        super(row, col, 'N', true);
        this.owner = owner;
    }

    public Owner getOwner() {
        return owner;
    }

    // heroes standing on their own Nexus can visit the market
    public boolean isHeroNexus() {
        return owner == Owner.HEROES;
    }
}
