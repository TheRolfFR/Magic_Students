package Renderer;

import Entities.Entity;
import Main.MainClass;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class PortalRenderer extends SpriteRenderer {
    Animation openingAnimation;
    Animation openedAnimation;

    Vector2f topLeftAngle;

    Color portalColor;

    private int timeCounter;

    private static final float PORTAL_SCALE = 2f;

    private static final Vector2f TILESIZE = new Vector2f(32, 32).scale(PORTAL_SCALE);
    private static final Vector2f TILESIZE_OFFSET = TILESIZE.copy().scale(0.5f);

    private static final String PREPATH = "img/portal/";

    private static final int OPENING_DURATION = 250;
    private static final String PORTAL_OPENING_PATH = PREPATH + "portalOpening_32x32.png";

    private static final int OPENED_DURATION = 750;
    private static final String PORTAL_OPENED_PATH = PREPATH + "portalOpened_32x32.png";

    public PortalRenderer(Entity entity, float x, float y, Color portalColor) {
        super(entity, TILESIZE);

        try {
            SpriteSheet openingSpriteSheet = new SpriteSheet(new Image(PORTAL_OPENING_PATH, false, Image.FILTER_NEAREST).getScaledCopy(PORTAL_SCALE), (int) TILESIZE.getX(), (int)TILESIZE.getY());
            this.openingAnimation = new Animation(openingSpriteSheet, OPENING_DURATION/openingSpriteSheet.getVerticalCount()/openingSpriteSheet.getHorizontalCount());

            SpriteSheet openedSpriteSheet = new SpriteSheet(new Image(PORTAL_OPENED_PATH, false, Image.FILTER_NEAREST).getScaledCopy(PORTAL_SCALE), (int) TILESIZE.getX(), (int)TILESIZE.getY());
            this.openedAnimation = new Animation(openedSpriteSheet, OPENED_DURATION/openedSpriteSheet.getVerticalCount()/openedSpriteSheet.getHorizontalCount());

            this.topLeftAngle = new Vector2f(x, y).sub(TILESIZE_OFFSET);

            this.portalColor = portalColor;

            this.timeCounter = 0;
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void update(int deltaTime) {
        if(this.timeCounter < OPENING_DURATION) {
            this.timeCounter += deltaTime;
        }
    }

    public void Render() {
        Animation animationToShow;

        if(this.timeCounter < OPENING_DURATION) {
            animationToShow = this.openingAnimation;
        } else {
            animationToShow = this.openedAnimation;
        }

        // if game not paused
        if (MainClass.getInGameTimeScale().getTimeScale() != 0f) {
            animationToShow.start();
        } else {
            animationToShow.stop();
        }

        animationToShow.draw(topLeftAngle.getX(), topLeftAngle.getY(), portalColor);
    }
}
