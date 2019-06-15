package Listeners;

import Entities.LivingBeings.LivingBeing;

public interface LivingBeingHealthListener {
    default void onHurt(LivingBeing being) {

    }
    default void onHeal(LivingBeing being) {

    }
    default void onUpdate(LivingBeing being) {

    }
}
