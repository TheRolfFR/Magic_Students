package Listeners;

import Entities.LivingBeings.LivingBeing;

/**
 * Listener triggered when the being moves
 */
public interface LivingBeingMoveListener {
    /**
     * the being just moved
     * @param being the being which moved
     */
    void onMove(LivingBeing being);
}
