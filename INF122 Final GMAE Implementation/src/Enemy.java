import java.util.List;

public interface Enemy extends Mob{
    public void hit(List<Mob> victims);
}
