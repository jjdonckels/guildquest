import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PowerUpItem extends Item {
    private final int buffValue;
    private final int duration;
    private boolean active;

    public PowerUpItem(String name, int buffValue, int duration) {
        super(name, ItemType.POWERUP);
        this.buffValue = buffValue;
        this.duration = duration;
        this.active = false;
    }

    public int getBuffValue() {
        return buffValue;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isActive() {
        return active;
    }

    public String activate() {
        this.active = true;
        return String.format("You activate %s: +%d power for %d turns.", getName(), buffValue, duration);
    }

    public String deactivate() {
        this.active = false;
        return String.format("%s effect ends.", getName());
    }

    @Override
    public String use() {
        return activate();
    }

    @Override
    public String getDescription() {
        return String.format("Power-up: +%d for %d turns", buffValue, duration);
    }
}
