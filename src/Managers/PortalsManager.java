package Managers;

import Entities.Portal;
import Main.MainClass;
import Main.TimeScale;
import Renderers.PortalRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static Main.MainClass.WIDTH;
import static Main.MainClass.HEIGHT;

public class PortalsManager {
    protected boolean portalSet;
    protected boolean portalEngaged;

    private static String[] types = {"classic", "item", "boss"};
    public static Map<String, Color> roomColor = Map.of(
            "classic", new Color(0x0094FF),     // blue
            "item", Color.yellow,                     // yellow
            "boss", new Color(0xf44336)         // red
    );
    protected static Map<String, Float> roomProbability = Map.of(
            "classic", 0.50f,
            "item", 0.30f,
            "boss", 0.30f
    );

    protected static ArrayList<Portal> portals = new ArrayList<>();

    public void setPortalEngaged(boolean portalEngaged) {
        this.portalEngaged = portalEngaged;
    }

    public PortalsManager() {
        this.portalSet = false;
        this.portalEngaged = false;

        int[][] possiblePositions = {{WIDTH / 2 - 20, 40}, {WIDTH / 2 - 20, HEIGHT - 40 - 40},
                {40, HEIGHT / 2 - 20}, {WIDTH - 40 - 40, HEIGHT / 2 - 20}};

        Portal portal;

        for (int p = 0; p < 4; p++) {
            portal = new Portal(possiblePositions[p][0], possiblePositions[p][1],
                    (int) PortalRenderer.getTILESIZE().getX(), (int) PortalRenderer.getTILESIZE().getY(), 20);
            portal.setShowDebugRect(true);
            portals.add(portal);
        }
    }

    public void setPortals() {
        if (!portalSet) {
            Random random = new Random();
            int nbClassicRoom = 0;

            for (Portal portal : portals) {
                String type = types[random.nextInt(types.length)];
                if (random.nextFloat() < roomProbability.get(type)) {
                    portal.setVisible(true);
                    portal.setType(type);

                    if (type.equals("classic")) {
                        nbClassicRoom++;
                    }
                }
            }
            if (nbClassicRoom == 0) {
                Portal portal = portals.get(random.nextInt(portals.size()));
                portal.setVisible(true);
                portal.setType("classic");
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
