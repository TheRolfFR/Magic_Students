package Renderers;

import Entities.Entity;
import Main.TimeScale;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

/**
 * Portal renderer with 2 animations, the opening animation and the opened animation
 */
public class PortalRenderer extends SpriteRenderer {
    private Animation openingAnimation;
    private Animation openedAnimation;

    private Vector2f topLeftAngle;

    private int timeCounter;

    private static final float PORTAL_SCALE = 2f;

    private static final Vector2f TILESIZE = new Vector2f(32, 32).scale(PORTAL_SCALE);
    private static final Vector2f TILESIZE_OFFSET = TILESIZE.copy().scale(0.5f);

    private static final String PREPATH = "img/portal/";

    private static final int OPENING_DURATION = 250;
    private static final String PORTAL_OPENING_PATH = PREPATH + "portalOpening_32x32.png";

    private static final int OPENED_DURATION = 750;
    private static final String PORTAL_OPENED_PATH = PREPATH + "portalOpened_32x32.png";

    /**
     * restarts the animation
     */
    public void restart() {
        this.timeCounter = 0;
        this.openingAnimation.restart();
    }

    /**
     * tilesize getter
     * @return the tile size getter
     */
    public static Vector2f getTileSize() {
        return TILESIZE.copy();
    }

    /**
     * Default constructor : tries to load the images, crash otherwise
     * @param entity the entity related
     * @param x the x center of the portal
     * @param y the y center of the portal
     */
    public PortalRenderer(Entity entity, float x, float y) {
        super(entity, TILESIZE);

        try {
            SpriteSheet openingSpriteSheet = new SpriteSheet(new Image(PORTAL_OPENING_PATH, false, Image.FILTER_NEAREST).getScaledCopy(PORTAL_SCALE), (int) TILESIZE.getX(), (int)TILESIZE.getY());
            this.openingAnimation = new Animation(openingSpriteSheet, OPENING_DURATION/openingSpriteSheet.getVerticalCount()/openingSpriteSheet.getHorizontalCount());

            SpriteSheet openedSpriteSheet = new SpriteSheet(new Image(PORTAL_OPENED_PATH, false, Image.FILTER_NEAREST).getScaledCopy(PORTAL_SCALE), (int) TILESIZE.getX(), (int)TILESIZE.getY());
            this.openedAnimation = new Animation(openedSpriteSheet, OPENED_DURATION/openedSpriteSheet.getVerticalCount()/openedSpriteSheet.getHorizontalCount());

            this.topLeftAngle = new Vector2f(x, y).sub(TILESIZE_OFFSET);

            this.timeCounter = 0;
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * In game update : updates the time counter
     * @param deltaTime the duration in ms of the last frame
     */
    public void update(int deltaTime) {
        if (this.timeCounter < OPENING_DURATION) {
            this.timeCounter += deltaTime;
        }
    }

    /**
     * In game rendering
     * @param color the color of the portal
     */
    public void Render(Color color) {
        Animation animationToShow;

        // animation selection based on the OPENING_DURATION
        if (this.timeCounter < OPENING_DURATION) {
            animationToShow = this.openingAnimation;
        } else {
            animationToShow = this.openedAnimation;
        }

        // if game not paused
        if (TimeScale.getInGameTimeScale().getTimeScale() != 0f) {
            animationToShow.start();
        } else {
            animationToShow.stop();
        }

        // draw the animation
        animationToShow.draw(topLeftAngle.getX(), topLeftAngle.getY(), color);
    }
}
