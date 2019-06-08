package Renderer;

import Entities.Entity;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class PortalRenderer extends SpriteRenderer {
    Animation openingAnimation;
    Animation openedAnimation;

    Vector2f topLeftAngle;

    Color portalColor;

    private int timeCounter;

    private static final Vector2f TILESIZE = new Vector2f(32, 32);
    private static final Vector2f TILESIZE_CENTER = TILESIZE.copy().scale(0.5f);

    private static final String PREPATH = "img/portal/";

    private static final int OPENING_DURATION = 1200;
    private static final String PORTAL_OPENING_PATH = PREPATH + "portalOpening_32x32.png";

    private static final int OPENED_DURATION = 500;
    private static final String PORTAL_OPENED_PATH = PREPATH + "portalOpened_32x32.png";

    public PortalRenderer(Entity entity, float x, float y, Color portalColor) {
        super(entity, TILESIZE);

        try {
            SpriteSheet openingSpriteSheet = new SpriteSheet(new Image(PORTAL_OPENING_PATH, false, Image.FILTER_NEAREST), (int) TILESIZE.getX(), (int)TILESIZE.getY());
            this.openingAnimation = new Animation(openingSpriteSheet, OPENING_DURATION);

            SpriteSheet openedSpriteSheet = new SpriteSheet(new Image(PORTAL_OPENED_PATH, false, Image.FILTER_NEAREST), (int) TILESIZE.getX(), (int)TILESIZE.getY());
            this.openedAnimation = new Animation(openedSpriteSheet, OPENED_DURATION);

            this.topLeftAngle = new Vector2f(x, y).sub(TILESIZE_CENTER);

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

        animationToShow.draw((int) topLeftAngle.getX(), topLeftAngle.getY(), portalColor);
    }
}
