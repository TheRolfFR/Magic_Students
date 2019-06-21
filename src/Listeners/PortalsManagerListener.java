package Listeners;

import Managers.PortalsManager;

/**
 * Listener triggered when a portal is engaged
 */
public interface PortalsManagerListener {

    /**
     * a portal is engaged
     * @param portalsManager the portal manager handling the portals data
     */
    void onEngage(PortalsManager portalsManager);
}
