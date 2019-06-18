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

/**
 * Portal data manager
 */
public class PortalsManager implements KeyPressListener, LivingBeingMoveListener {
    private static final Map<String, Color> ROOM_COLOR = new HashMap<>();
    private static final Map<String, Float> CUMULATIVE_ROOM_PROBABILITY = new HashMap<>();

    /**
     *  Static code to add of the items for Java 1.8 (NEEDED)
     */
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
    private String latestPortalType;

    private ArrayList<PortalsManagerListener> portalsManagerListeners;

    /**
     * method to add a portal manager listener
     * @param listener the listener to add
     */
    private void addPortalsManagerListeners(PortalsManagerListener listener) {
        this.portalsManagerListeners.add(listener);
    }

    /**
     * room color map getter
     * @return the map
     */
    public static Map<String, Color> getRoomColor() {
        return ROOM_COLOR;
    }

    /**
     * Default constructor
     * @param gc the game container to add the key listener
     * @param player the player to add the move listener
     * @param fadeToBlack the fadeToblack listener added to the portalManager
     */
    public PortalsManager(GameContainer gc, Player player, FadeToBlack fadeToBlack) {
        gc.getInput().addKeyListener(this);
        this.portalSet = false;
        this.setLatestPortalType(null);
        this.portalHovered = null;

        int offsetFromWall = 60;

        // the 4 possible positions of the portals
        int[][] possiblePositions = {
                {MainClass.WIDTH / 2, offsetFromWall},
                {MainClass.WIDTH / 2, MainClass.HEIGHT - offsetFromWall},
                {offsetFromWall, MainClass.HEIGHT / 2},
                {MainClass.WIDTH - offsetFromWall, MainClass.HEIGHT / 2}
        };

        // create the next floor portal
        floorPortal = new Portal(MainClass.WIDTH / 2, MainClass.HEIGHT / 2,
                (int) PortalRenderer.getTileSize().getX(), (int) PortalRenderer.getTileSize().getY(), 20);
        floorPortal.setType("nextFloor");
        floorPortal.setShowDebugRect(true);

        // for each portal
        Portal portal;
        for (int p = 0; p < 4; p++) {
            portal = new Portal(possiblePositions[p][0], possiblePositions[p][1],
                    (int) PortalRenderer.getTileSize().getX(), (int) PortalRenderer.getTileSize().getY(), 20);
            portal.setShowDebugRect(true);
            portals.add(portal);
        }

        // add the move listener
        player.addMoveListener(this);

        // add the portal listeners
        this.portalsManagerListeners = new ArrayList<>();
        this.addPortalsManagerListeners(fadeToBlack);
        this.addPortalsManagerListeners(TimeScale.getInGameTimeScale());
    }

    /**
     * Latest portal type getter
     * @return the portal type
     */
    public String getLatestPortalType() {
        return this.latestPortalType;
    }

    /**
     * Latest portal setter
     * @param type the type of the portal contained in CUMULATIVE_ROOM_PROBABILITY keys
     */
    private void setLatestPortalType(String type) {
        this.latestPortalType = type;
    }

    /**
     * display all the portals depending the actual room
     */
    void setPortals() {
        if (!portalSet) { // if the portals are not set
            if (this.latestPortalType != null && this.latestPortalType.equals("boss")) { // if these not the first room and the room is a boss room
                floorPortal.setVisible(true); // set visible a next room portal
            } else {
                Random random = new Random(); // generate a random object

                Portal p = portals.get(random.nextInt(portals.size())); // take a random portal
                p.setVisible(true); // set it visible
                p.setType("classic"); // and make it classic

                float chance;
                for (Portal portal : portals) { // for each portal
                    if (!portal.isVisible()) { // select the not visible portals
                        chance = random.nextFloat(); // generate an random float between 0 and 1

                        for (String type : CUMULATIVE_ROOM_PROBABILITY.keySet()) { // for each type of portal
                            if (!(type.equals("item") && lastestPortalIsItem())) { // if the current room isn't an item room
                                if (chance <= CUMULATIVE_ROOM_PROBABILITY.get(type)) { // if the probability is lower or equal to chance
                                    portal.setVisible(true); // set the portal visible
                                    portal.setType(type); // set the portal type
                                    break; // break the for loop
                                }
                            }
                        }
                    }
                }
            }
            this.portalSet = true; // indicate that the portals are set
            this.latestPortalType = null; // set last portal type selected to null
        }
    }

    /**
     * In game update : update all the portals and the floor portal
     * @param deltaTime the duration in ms of the last frame
     * @see Portal#update(int)
     */
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

    /**
     * Hides all the portals
     */
    public void hidePortals() {
        for (Portal portalBis : portals) { // for each portals
            portalBis.setVisible(false); // hide portal
        }
        floorPortal.setVisible(false); // hide the floor portal too
        portalSet = false; // indicate that the portals aren't set
    }

    /**
     * Says wther the latest portal is an item portal
     * @return true if the latest portal is an item portal, false otherwise
     */
    private boolean lastestPortalIsItem() {
        return this.latestPortalType != null && this.latestPortalType.equals("item");
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     */
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

    /**
     * Portal key trigger : F to engage the portals
     * @param key the key pressed
     * @param c the key pressed
     */
    @Override
    public void keyPressed(int key, char c) {
        if (this.portalHovered != null && key == Input.KEY_F) {
            // if the latest room was a boss room

            // update the actual portal
            this.setLatestPortalType(this.portalHovered.getType());
            this.portalHovered = null;

            if (this.latestPortalType != null && this.latestPortalType.equals("nextFloor")) {
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

    /**
     * Portal hover trigger
     * @param being the being living which moved
     */
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
