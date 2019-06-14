package Managers;

import Entities.Portal;
import Main.KeyPressListener;
import Main.MainClass;
import Main.TimeScale;
import Renderers.PortalRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static Main.MainClass.HEIGHT;
import static Main.MainClass.WIDTH;

public class PortalsManager implements KeyPressListener {
    private boolean portalSet;
    private boolean portalEngaged;
    private Portal actualPortal;


    private static String[] types = {"classic", "item", "boss"};
    public static Map<String, Color> roomColor = Map.of(
            "classic", new Color(0x0094FF),     // blue
            "item", Color.yellow,                     // yellow
            "boss", new Color(0xf44336)         // red
    );
    private static Map<String, Float> cumulativeRoomProbability = Map.of(
            "classic", 0.20f,
            "item", 0.24f,
            "boss", 0.28f
    );

    private static ArrayList<Portal> portals = new ArrayList<>();

    private void setPortalEngaged(boolean portalEngaged) {
        this.portalEngaged = portalEngaged;
    }

    public PortalsManager(GameContainer gc) {
        gc.getInput().addKeyListener(this);
        this.portalSet = false;
        this.portalEngaged = false;
        this.actualPortal = new Portal(0,0,1,1,1);
        this.actualPortal.setType("classic");

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

    public Portal getActualPortal() { return this.actualPortal; }


    void setPortals() {
        if (!portalSet) {
            Random random = new Random();

            Portal p = portals.get(random.nextInt(portals.size()));
            p.setVisible(true);
            p.setType("classic");

            float chance;
            for (Portal portal: portals) {
                if (!portal.isVisible()) {
                    chance = random.nextFloat();

                    for (String type: cumulativeRoomProbability.keySet()) {
                        if (chance <= cumulativeRoomProbability.get(type)) {
                            portal.setVisible(true);
                            portal.setType(type);
                            break;
                        }
                    }
                }
            }
            portalSet = true;
        }

        if (this.portalEngaged) {
            for (Portal portal : portals) {
                if (portal.isVisible() && MainClass.getInstance().getPlayer().collidesWith(portal)) {
                    if(this.actualPortal.getType().equals("boss")){
                        MainClass.nextDifficulty();
                    }
                    this.actualPortal = portal;
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

    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_F) {
            this.setPortalEngaged(true);
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        if (key == Input.KEY_F) {
            this.setPortalEngaged(false);
        }
    }
}
