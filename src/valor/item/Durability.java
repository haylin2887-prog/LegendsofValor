package valor.item;

// how many more times an item can be used before it breaks (or is used up)
public class Durability {
    private final int maxUses;
    private int usesLeft;

    public Durability(int maxUses) {
        this.maxUses = maxUses;
        this.usesLeft = maxUses;
    }

    // copy constructor, so each copied item wears out separately
    public Durability(Durability other) {
        this.maxUses = other.maxUses;
        this.usesLeft = other.usesLeft;
    }

    public int getUsesLeft() {
        return usesLeft;
    }

    public boolean isUsable() {
        return usesLeft > 0;
    }

    public void consume() {
        if (usesLeft > 0) {
            usesLeft--;
        }
    }

    public void restore() {
        usesLeft = maxUses;
    }
}
