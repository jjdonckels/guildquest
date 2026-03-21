public enum Difficulty {
    EASY(1.0, 1.0, 3, 2, 4),
    MEDIUM(1.25, 1.25, 5, 4, 2),
    HARD(1.5, 1.5, 8, 6, 1);

    private final double healthMultiplier, damageMultiplier;
    private final int enemyCount, hazardCount, powerUpCount;

    Difficulty(double healthMultiplier, double damageMultiplier, int enemyCount, int hazardCount, int powerUpCount) {
        this.healthMultiplier = healthMultiplier;
        this.damageMultiplier = damageMultiplier;
        this.enemyCount = enemyCount;
        this.hazardCount = hazardCount;
        this.powerUpCount = powerUpCount;
    }

    public double getHealthMultiplier() {return healthMultiplier;}

    public double getDamageMultiplier() {return damageMultiplier;}

    public int getEnemyCount() {return enemyCount;}

    public int getHazardCount() {return hazardCount;}

    public int getPowerUpCount() {return powerUpCount;}
}
