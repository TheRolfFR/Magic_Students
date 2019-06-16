package Managers;

import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Player;
import Entities.Portal;
import HUD.FadeToBlack;
import Listeners.KeyPressListener;
import Listeners.LivingBeingMoveListener;
import Listeners.PortalsManagerListener;
import Main.GameStats;
import Main.MainClass;
import Main.TimeScale;
import Renderers.PortalRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static Main.MainClass.HEIGHT;
import static Main.MainClass.WIDTH;

public class PortalsManager implements KeyPressListener, LivingBeingMoveListener {
    private static final Map<String, Color> ROOM_COLOR = new HashMap<>();
    private static final Map<String, Float> CUMULATIVE_ROOM_PROBABILITY = new HashMap<>();

    static {
        ROOM_COLOR.put("classic", new Color(0x0094FF));
        ROOM_COLOR.put("item", Color.yellow);
        ROOM_COLOR.put("boss", new Color(0xf44336));
        ROOM_COLOR.put("nextFloor", new Color(0x32FF32));

        CUMULATIVE_ROOM_PROBABILITY.put("classic", 0.20f);
        CUMULATIVE_ROOM_PROBABILITY.put("item", 0.24f);
        CUMULATIVE_ROOM_PROBABILITY.put("boss", 0.27f);
    }

    private static ArrayList<Portal> portals = new ArrayList<>();
    private static Portal floorPortal;

    private boolean portalSet;
    private Portal portalHovered;
    private Portal latestPortal;

    private ArrayList<PortalsManagerListener> portalsManagerListeners;

    private void addPortalsManagerListeners(PortalsManagerListener listener) {
        this.portalsManagerListeners.add(listener);
    }

    public static Map<String, Color> getRoomColor() {
        return ROOM_COLOR;
    }

    public PortalsManager(GameContainer gc, Player player, FadeToBlack fadeToBlack) {
        gc.getInput().addKeyListener(this);
        this.portalSet = false;
        this.setLatestPortal(null);
        this.portalHovered = null;

        int offsetFromWall = 60;
        int[][] possiblePositions = {
                {WIDTH / 2, offsetFromWall},
                {WIDTH / 2, HEIGHT - offsetFromWall},
                {offsetFromWall, HEIGHT / 2},
                {WIDTH - offsetFromWall, HEIGHT / 2}
        };

        Portal portal = new Portal(WIDTH / 2, HEIGHT / 2,
                (int) PortalRenderer.getTILESIZE().getX(), (int) PortalRenderer.getTILESIZE().getY(), 20);
        portal.setType("nextFloor");
        portal.setShowDebugRect(true);
        floorPortal = portal;

        for (int p = 0; p < 4; p++) {
            portal = new Portal(possiblePositions[p][0], possiblePositions[p][1],
                    (int) PortalRenderer.getTILESIZE().getX(), (int) PortalRenderer.getTILESIZE().getY(), 20);
            portal.setShowDebugRect(true);
            portals.add(portal);
        }

        player.addMoveListener(this);

        this.portalsManagerListeners = new ArrayList<>();
        this.addPortalsManagerListeners(fadeToBlack);
        this.addPortalsManagerListeners(TimeScale.getInGameTimeScale());
    }

    public Portal getLatestPortal() { return this.latestPortal; }

    private void setLatestPortal(Portal latestPortal) {
        this.latestPortal = latestPortal;
    }

    void setPortals() {
        if (!portalSet) {
            if (this.latestPortal != null && this.latestPortal.getType().equals("boss")) {
                floorPortal.setVisible(true);
            } else {
                Random random = new Random();

                Portal p = portals.get(random.nextInt(portals.size()));
                p.setVisible(true);
                p.setType("classic");

                float chance;
                for (Portal portal : portals) {
                    if (!portal.isVisible()) {
                        chance = random.nextFloat();

                        for (String type : CUMULATIVE_ROOM_PROBABILITY.keySet()) {
                            if (!(type.equals("item") && lastestPortalIsItem())) {
                                if (chance <= CUMULATIVE_ROOM_PROBABILITY.get(type)) {
                                    portal.setVisible(true);
                                    portal.setType(type);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            this.portalSet = true;
            this.latestPortal = null;
        }
    }

    public void update(int deltaTime) {
        if (this.portalSet) {
            for (Portal portal : portals) {
                if (portal.isVisible()) {
                    portal.update(deltaTime);
                }
            }
            if (floorPortal.isVisible()) {
                floorPortal.update(deltaTime);
            }
        }
    }

    public void hidePortals() {
        for (Portal portalBis : portals) {
            portalBis.setVisible(false);
        }
        floorPortal.setVisible(false);
        portalSet = false;
    }

    private boolean lastestPortalIsItem() {
        return this.latestPortal != null && this.latestPortal.getType().equals("item");
    }

    public void render(Graphics g) {
        for (Portal portal: portals) {
            if (portal.isVisible()) {
                portal.render(g);
            }
        }
        if (floorPortal.isVisible()) {
            floorPortal.render(g);
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (this.portalHovered != null && key == Input.KEY_F) {
            // if the latest room was a boss room

            // update the actual portal
            this.setLatestPortal(this.portalHovered);
            this.portalHovered = null;

            if (this.latestPortal != null && this.latestPortal.getType().equals("nextFloor")) {
                GameStats.getInstance().nextDifficulty();
            }

            // trigger all listeners
            if (this.portalsManagerListeners.size() > 0) {
                for (PortalsManagerListener listener : this.portalsManagerListeners) {
                    listener.onEngage(this);
                }
            }
        }
    }

    @Override
    public void onMove(LivingBeing being) {
        // if the being is the player
        if (being instanceof Player) {
            this.portalHovered = null;
            // for each portal
            for (Portal portal : portals) {
                // if the player collides with the portal
                if (portal.isVisible() && being.collidesWith(portal)) {
                    // update the colliding portal
                    this.portalHovered = portal;
                }
            }
            if (floorPortal.isVisible() && being.collidesWith(floorPortal)) {
                // update the colliding portal
                this.portalHovered = floorPortal;
            }
        }
    }
}
