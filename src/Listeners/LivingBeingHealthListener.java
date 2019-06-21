package Listeners;

import Entities.LivingBeings.LivingBeing;

/**
 * Listener triggered when the being health is updated. It is mainly used for health bars in order to update its data.
 */
public interface LivingBeingHealthListener {
    /**
     * the being <being> has been hurt with <amount> damages. Be careful, this amount may be different than  the one that could be triggered by the onAttack method of the AttackListener listener because the beings may have armor.
     * @param being the being hurt
     * @param amount the amount of damages caused
     */
    default void onHurt(LivingBeing being, int amount) {

    }

    /**
     * the being <being> is healed of <amount> points.
     * @param being the being healed
     * @param amount the amount of health restored
     */
    default void onHeal(LivingBeing being, int amount) {

    }

    /**
     * the being <being> is hurt or healed of <amount> points
     * @param being the being hurt or healed
     * @param amount amount of change (amount is signed)
     */
    default void onUpdate(LivingBeing being, int amount) {

    }

    /**
     * the being <being> just died
     * @param being the being which just died
     */
    default void onDeath(LivingBeing being) {

    }
}
