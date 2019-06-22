package Renderers;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

/**
 * Sprite view for {@link LivingBeingRenderer} class
 */
public class SpriteView {
    /**
     * the sprite view animation
     */
    protected Animation animation;
    private Vector2f animationCenter;

    /**
     * Constructor without transparent color
     * @param spriteSheetPath the image spritesheet path
     * @param tileSize the size of a tile of the spritesheet
     * @param frameDuration the duration in ms of a tile
     */
    public SpriteView(String spriteSheetPath, Vector2f tileSize, int frameDuration) {
        init(spriteSheetPath, tileSize, frameDuration, null);
    }

    /**
     * Constructor with transparent color
     * @param spriteSheetPath the image spritesheet path
     * @param tileSize the size of a tile of the spritesheet
     * @param frameDuration the duration in ms of a tile
     * @param transparentColor the background color to put transparent
     */
    public SpriteView(String spriteSheetPath, Vector2f tileSize, int frameDuration, Color transparentColor) {
        init(spriteSheetPath, tileSize, frameDuration, transparentColor);
    }

    /**
     * initialization function for constructors
     * @param spriteSheetPath the image spritesheet path
     * @param tileSize the size of a tile of the spritesheet
     * @param frameDuration the duration in ms of a tile
     * @param transparentColor the background color to put transparent
     */
    private void init(String spriteSheetPath, Vector2f tileSize, int frameDuration, Color transparentColor) {
        Image original = null;

        try {
            if (transparentColor == null) {
                original = new Image(spriteSheetPath, false, Image.FILTER_NEAREST);
            } else {
                original = new Image(spriteSheetPath, false, Image.FILTER_NEAREST, transparentColor);
            }
        } catch (SlickException e) {
            System.exit(1);
        }

        float scale = tileSize.getY()/ ((float) original.getHeight());

        Image copy = original.getScaledCopy(scale);

        SpriteSheet sp = new SpriteSheet(copy, (int) tileSize.getX(), (int) tileSize.getY());

        animationCenter = new Vector2f(tileSize.getX()/2f, tileSize.getY()/2f);

        this.animation = new Animation(sp, frameDuration);
    }

    /**
     * Stops the animation
     */
    public void stop() {
        this.animation.stop();
    }

    /**
     * Resume the animation
     */
    public void start() {
        this.animation.start();
    }

    /**
     * In game rendering
     * @param center spriteview center position
     * @param filter color filter to "color" the image
     * @see LivingBeingRenderer#render(Graphics, Vector2f)
     */
    public void render(Vector2f center, Color filter) {
        if (this.animation != null) {
            Vector2f location = center.copy().sub(animationCenter);
            this.animation.draw((int) location.getX(), (int) location.getY(), filter);
        }
    }
}
