package Listeners;

/**
 * Listener triggered when a living being attack status changes
 */
public interface AttackListener {

    /**
     * the being just attacked
     * @param amount the amount of damage sent to the other {@link Entities.LivingBeings.LivingBeing libving being}
     */
    default void onAttack(int amount) {

    }

    /**
     * the being finished attacking, its attack cooldown starts
     */
    default void onCooldownStart() {

    }

    /**
     * the cooldown is updated
     * @param currentTime current time
     * @param duration duration of the cooldown
     */
    default void onCooldownUpdate(float currentTime, float duration) {

    }

    /**
     * the cooldown just ended
     */
    default void onCooldownEnd() {

    }
}
