package Managers;

import Entities.Portal;
import Main.MainClass;
import Main.TimeScale;
import Renderers.PortalRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.Random;

import static Main.MainClass.WIDTH;
import static Main.MainClass.HEIGHT;

public class PortalsManager {
    protected boolean portalSet = false;
    protected boolean portalEngaged = false;

    public static final int PORTAL_BOSS_COUNT = 40;
    public static final int PORTAL_BOSS_CHANCE = 25;
    public static final int PORTAL_ITEM_COUNT = 25;
    public static final int PORTAL_ITEM_CHANCE = 15;

    protected static ArrayList<Portal> portals = new ArrayList<>();

    public void setPortalEngaged(boolean portalEngaged) {
        this.portalEngaged = portalEngaged;
    }

    public PortalsManager() {
        this.portalSet = false;
        this.portalEngaged = false;

        int[][] possiblePositions = {{WIDTH / 2 - 20, 40}, {WIDTH / 2 - 20, HEIGHT - 40 - 40},
                {40, HEIGHT / 2 - 20}, {WIDTH - 40 - 40, HEIGHT / 2 - 20}};

        Color[] possibleColors = {
                new Color(0x0094FF), // blue
                new Color(0xf44336), // red
                Color.yellow,
                new Color(0x4CAF50) // green
        };
        Portal portal;

        for (int p = 0; p < 4; p++) {
            portal = new Portal(possiblePositions[p][0], possiblePositions[p][1],
                    (int) PortalRenderer.getTILESIZE().getX(), (int) PortalRenderer.getTILESIZE().getY(), 20, possibleColors[p]);
            portal.setShowDebugRect(true);
            portals.add(portal);
        }
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
                    TimeScale.getInGameTimeScale().setTimeScale(0f);
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
        for (Portal portalBis : portals) {
            portalBis.setVisible(false);
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
