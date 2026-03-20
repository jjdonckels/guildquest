public class Relic extends PowerUp {
    private PlayableCharacter holder;

    public Relic(Position position) {
        super(position, "🏆");
        this.holder = null;
    }

    public PlayableCharacter getHolder() {return holder;}

    public boolean isCollected() {return holder != null;}

    @Override
    public void applyEffect(PlayableCharacter character) {
        if (character == null)
            throw new IllegalArgumentException("Character cannot be null.");
        if (!isCollected())
            this.holder = character;
    }

    @Override
    public void reset() {
        super.reset();
        this.holder = null;
    }

    @Override
    public String toString() {
        return "{Relic"
                + "id: " + getId()
                + ", position=" + getPosition()
                + ", holder=" + (holder == null ? "none" : holder.getName())
                + ", symbol='" + getSymbol() + "'}";
    }
}