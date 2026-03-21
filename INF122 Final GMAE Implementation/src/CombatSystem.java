public class CombatSystem {

    public void resolveAttack(Entity attacker, Entity defender) {
        if (attacker == null)
            throw new IllegalArgumentException("Attacker cannot be null.");
        if (defender == null)
            throw new IllegalArgumentException("Defender cannot be null.");
        if (!attacker.isAlive())
            throw new IllegalStateException("Defeated attacker cannot attack.");
        if (!defender.isAlive())
            throw new IllegalStateException("Cannot attack a defeated defender.");
        int damage = Math.toIntExact(Math.round(calculateDamage(attacker) - (0.25 * calculateDefense(defender))));
        defender.takeDamage(damage);
        if (attacker instanceof PlayableCharacter) {
            PlayableCharacter pc = (PlayableCharacter) attacker;
            if (pc.getStrengthBoostTurns() > 0)
                pc.consumeStrengthBoostTurn();
        }
    }

    private int calculateDamage(Entity attacker) {
        if (attacker instanceof PlayableCharacter) {
            PlayableCharacter pc = (PlayableCharacter) attacker;
            return pc.getAttackPower();
        }
        if (attacker instanceof Enemy) {
            Enemy e = (Enemy) attacker;
            return e.getAttackPower();
        }
        throw new IllegalArgumentException("Unsupported attacker type: " + attacker);
    }

    private int calculateDefense(Entity defender) {
        if (defender instanceof PlayableCharacter) {
            PlayableCharacter pc = (PlayableCharacter) defender;
            return pc.getDefense();
        }
        if (defender instanceof Enemy) {
            Enemy e = (Enemy) defender;
            return e.getDefense();
        }
        throw new IllegalArgumentException("Unsupported attacker type: " + defender);
    }
}
