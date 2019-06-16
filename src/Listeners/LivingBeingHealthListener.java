package Listeners;

import Entities.LivingBeings.LivingBeing;

public interface LivingBeingHealthListener {
    default void onHurt(LivingBeing being, int amount) {

    }
    default void onHeal(LivingBeing being, int amount) {

    }
    default void onUpdate(LivingBeing being, int amount) {

    }
    default void onDeath(LivingBeing being) {

    }
}
