package Managers;

import Entities.Portal;
import Main.MainClass;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.Random;

import static Main.MainClass.WIDTH;
import static Main.MainClass.HEIGHT;

public class PortalManager {
    protected boolean portalSet = false;
    protected boolean portalEngaged = false;

    public static final int PORTAL_BOSS_COUNT = 40;
    public static final int PORTAL_BOSS_CHANCE = 25;
    public static final int PORTAL_ITEM_COUNT = 25;
    public static final int PORTAL_ITEM_CHANCE = 15;

    protected static ArrayList<Portal> portals = new ArrayList<>();

    public void setPortalSet(boolean portalSet) {
        this.portalSet = portalSet;
    }

    public void setPortalEngaged(boolean portalEngaged) {
        this.portalEngaged = portalEngaged;
    }

    public PortalManager() {
        this.portalSet = false;
        this.portalEngaged = false;

        int[][] possible_positions = {{WIDTH / 2 - 20, 40}, {WIDTH / 2 - 20, HEIGHT - 40 - 40},
                {40, HEIGHT / 2 - 20}, {WIDTH - 40 - 40, HEIGHT / 2 - 20}};
        Portal portal;

        for (int p = 0; p < 4; p++) {
            portal = new Portal(possible_positions[p][0], possible_positions[p][1],
                    40, 40, 20);
            portal.setShowDebugRect(true);
            portals.add(portal);
        }

        this.setPortals();
    }

    public void setPortals() {
        if (!portalSet) {
            Random random = new Random();
            int nbVisiblePortal = 0;

            for (Portal portal : portals) {
                if (random.nextInt(3) == 0) {
                    portal.setVisible(true);
                    nbVisiblePortal++;
                }
            }
            if (nbVisiblePortal == 0) {
                portals.get(random.nextInt(4)).setVisible(true);
            }
            portalSet = true;
        }

        if (this.portalEngaged) {
            for (Portal portal : portals) {
                if (portal.isVisible() && MainClass.getInstance().getPlayer().collidesWith(portal)) {
                    MainClass.getInGameTimeScale().setTimeScale(0f);
                    MainClass.getInstance().getFadeToBlack().setActive(true);
                    this.portalEngaged = false;
                }
            }
        }
    }

    public void update(int deltaTime) {
        if(portalSet){
            for(Portal portal : portals){
                if(portal.isVisible()){
                    portal.update(deltaTime);
                }
            }
        }
    }

    public void hidePortals() {
        for (Portal portal_bis : portals) {
            portal_bis.setVisible(false);
            portalSet = false;
        }
    }

    public void render(Graphics g) {
        for (Portal portal: portals) {
            if (portal.isVisible()) {
                portal.render(g);
            }
        }
    }
}
