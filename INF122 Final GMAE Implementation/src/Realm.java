public abstract class Realm {
    private static int idCounter = 1;
    private final int id;
    private final String name;
    private final String description;
//    private LocalTimeRule localTimeRule;

//    public  Realm(String name, String description, LocalTimeRule localTimeRule)
//    {
//        super(idCounter++, name);
//        this.description = description;
//        this.localTimeRule = localTimeRule;
//    }

    public Realm(String name, String description) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name cannot be null or blank.");
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Description cannot be null or blank.");
        this.id = idCounter++;
        this.name = name;
        this.description = description;
    }

    public abstract Enemy createEnemy(Position position, Difficulty difficulty);

    public abstract Hazard createHazard(Position position);

    @Override
    public String toString() {
        return "{Realm id: " + id +
                ", name: " + name +
                ", description: " + description + "}";
//                "\nLocal Time: " + toLocalTime(GuildQuest.getWorldClock().getCurrentTime());
    }

    // converts world time to local time
//    public LocalTime toLocalTime(WorldTime worldTime)
//    {
//        return localTimeRule.worldToLocal(worldTime);
//    }

    // Getters
    public int getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public String getDescription()
    {
        return description;
    }
//    public LocalTimeRule getLocalTimeRule()
//    {
//        return localTimeRule;
//    }
//    public void setLocalTimeRule(LocalTimeRule localTimeRule)
//    {
//        this.localTimeRule = localTimeRule;
//    }
}

class FireRealm extends Realm {
    private static final int BASE_ENEMY_HEALTH = 30;
    private static final int BASE_ENEMY_ATTACK = 15;
    private static final int BASE_ENEMY_DEFENSE = 12;
    private static final int HAZARD_DAMAGE = 20;
    private static String defaultHazard = "\ud83d\udd25";
    private static String defaultEnemy = "\ud83d\udc09";
    private String hazardStr;
    private String enemyStr;

    public FireRealm() {
        super("Volcano", "Lava, fire, and a whole lot of heat.");
        hazardStr = defaultHazard;
        enemyStr = defaultEnemy;
        if (Main.debug) {
            hazardStr = "H";
            enemyStr = "E";
        }
    }
    public FireRealm(String name, String description) {
        super(name, description);
        hazardStr = defaultHazard;
        enemyStr = defaultEnemy;
        if (Main.debug) {
            hazardStr = "H";
            enemyStr = "E";
        }
    }
    public FireRealm(String name, String description, String hazard, String enemy) {
        super(name, description);
        hazardStr = hazard;
        enemyStr = enemy;
        if (Main.debug) {
            hazardStr = "H";
            enemyStr = "E";
        }
    }

    @Override
    public Enemy createEnemy(Position position, Difficulty difficulty) {
        if (position == null || difficulty == null)
            throw new IllegalArgumentException("Position and difficulty cannot be null.");
        int scaledHealth = (int) (BASE_ENEMY_HEALTH * difficulty.getHealthMultiplier());
        int scaledAttack = (int) (BASE_ENEMY_ATTACK * difficulty.getDamageMultiplier());
        int scaledDefense = (int) (BASE_ENEMY_DEFENSE * difficulty.getDamageMultiplier());
        return new Enemy(position, enemyStr, scaledHealth, scaledAttack, scaledDefense);
    }

    @Override
    public Hazard createHazard(Position position) {
        if (position == null)
            throw new IllegalArgumentException("Position cannot be null.");
        return new Hazard(position, hazardStr, HAZARD_DAMAGE);
    }
}

class WaterRealm extends Realm {
    private static final int BASE_ENEMY_HEALTH = 30;
    private static final int BASE_ENEMY_ATTACK = 15;
    private static final int BASE_ENEMY_DEFENSE = 8;
    private static final int HAZARD_DAMAGE = 20;
    private static String defaultHazard = "\ud83c\udf0a";
    private static String defaultEnemy = "\ud83e\udd88";
    private String hazardStr;
    private String enemyStr;

    public WaterRealm() {
        super("Ocean", "Waves, currents, and plenty of salt.");
        hazardStr = defaultHazard;
        enemyStr = defaultEnemy;
        if (Main.debug) {
            hazardStr = "H";
            enemyStr = "E";
        }
    }
    public WaterRealm(String name, String description) {
        super(name, description);
        hazardStr = defaultHazard;
        enemyStr = defaultEnemy;
        if (Main.debug) {
            hazardStr = "H";
            enemyStr = "E";
        }
    }
    public WaterRealm(String name, String description, String hazard, String enemy) {
        super(name, description);
        hazardStr = hazard;
        enemyStr = enemy;
        if (Main.debug) {
            hazardStr = "H";
            enemyStr = "E";
        }
    }

    @Override
    public Enemy createEnemy(Position position, Difficulty difficulty) {
        if (position == null || difficulty == null)
            throw new IllegalArgumentException("Position and difficulty cannot be null.");
        int scaledHealth = (int) (BASE_ENEMY_HEALTH * difficulty.getHealthMultiplier());
        int scaledAttack = (int) (BASE_ENEMY_ATTACK * difficulty.getDamageMultiplier());
        int scaledDefense = (int) (BASE_ENEMY_DEFENSE * difficulty.getDamageMultiplier());
        return new Enemy(position, enemyStr, scaledHealth, scaledAttack, scaledDefense);
    }

    @Override
    public Hazard createHazard(Position position) {
        if (position == null)
            throw new IllegalArgumentException("Position cannot be null.");
        return new Hazard(position, hazardStr, HAZARD_DAMAGE);
    }
}

class EarthRealm extends Realm {
    private static final int BASE_ENEMY_HEALTH = 30;
    private static final int BASE_ENEMY_ATTACK = 15;
    private static final int BASE_ENEMY_DEFENSE = 20;
    private static final int HAZARD_DAMAGE = 20;
    private static String defaultHazard = "\ud83c\udf2a";
    private static String defaultEnemy = "\ud83e\udd81";
    private String hazardStr;
    private String enemyStr;

    public EarthRealm() {
        super("Land", "Lions, tornadoes, and a whole lot of rock.");
        hazardStr = defaultHazard;
        enemyStr = defaultEnemy;
        if (Main.debug) {
            hazardStr = "H";
            enemyStr = "E";
        }
    }
    public EarthRealm(String name, String description) {
        super(name, description);
        hazardStr = defaultHazard;
        enemyStr = defaultEnemy;
        if (Main.debug) {
            hazardStr = "H";
            enemyStr = "E";
        }
    }
    public EarthRealm(String name, String description, String hazard, String enemy) {
        super(name, description);
        hazardStr = hazard;
        enemyStr = enemy;
        if (Main.debug) {
            hazardStr = "H";
            enemyStr = "E";
        }
    }

    @Override
    public Enemy createEnemy(Position position, Difficulty difficulty) {
        if (position == null || difficulty == null)
            throw new IllegalArgumentException("Position and difficulty cannot be null.");
        int scaledHealth = (int) (BASE_ENEMY_HEALTH * difficulty.getHealthMultiplier());
        int scaledAttack = (int) (BASE_ENEMY_ATTACK * difficulty.getDamageMultiplier());
        int scaledDefense = (int) (BASE_ENEMY_DEFENSE * difficulty.getDamageMultiplier());
        return new Enemy(position, enemyStr, scaledHealth, scaledAttack, scaledDefense);
    }

    @Override
    public Hazard createHazard(Position position) {
        if (position == null)
            throw new IllegalArgumentException("Position cannot be null.");
        return new Hazard(position, hazardStr, HAZARD_DAMAGE);
    }
}
