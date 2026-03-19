public interface EnemyHazard extends Hazard {
    int getDamage();
    void applyToPlayer(Player player);
}