package Renderers;

import Main.TimeScale;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

/**
 * Visual effect renderer : attack slashes and bow charge and release
 */
public class EffectRenderer{

    private Animation animation; // animation of the image
    private Vector2f tileSize; // tile size

    private Image lastImage; // last image displayed

    /**
     * Default constructor
     * @param imgPath the path of the image
     * @param tileSize the size of one tile of the spritesheet
     * @param frameDuration how long a frame lasts (in ms)
     */
    public EffectRenderer(String imgPath, Vector2f tileSize, int frameDuration) {
        this.tileSize = tileSize; // affecting the tileSize

        Image image = null; // tring to load an image
        try {
            image = new Image(imgPath, false, Image.FILTER_NEAREST); // try to load the image with a nearest filter (important)
        } catch (SlickException e) { // else crash
            e.printStackTrace();
            System.exit(1);
        }

        float scale = tileSize.getY() / (float) image.getHeight(); // determinating how much I have to scale

        image = image.getScaledCopy(scale); // getitng a scaled copy of the image

        // creation and start of the aniamtion
        this.animation = new Animation(new SpriteSheet(image, (int) tileSize.getX(), (int) tileSize.getY()), frameDuration);
        animation.start();
    }

    /**
     * tileSize getter
     * @return the tile size
     */
    public Vector2f getTileSize() {
        return this.tileSize;
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     * @param centerX the X center of the entity
     * @param centerY the Y center of the entity
     * @param angle the angle to rotate the image if needed
     */
    public void render(Graphics g, int centerX, int centerY, float angle) {
        // if game not paused
        if (TimeScale.getInGameTimeScale().getTimeScale() != 0f) {
            animation.draw(-10000, -10000); // draw and updates the animation in oustide of the window
            this.lastImage = animation.getCurrentFrame().copy(); // get the current frame
            this.lastImage.rotate(angle); // and rotate it
        }

        if (this.lastImage != null) { // avoid NullException errors
            g.drawImage(this.lastImage, centerX - this.tileSize.getX()/2, centerY - this.tileSize.getY()/2); // render the image (top left corner)
        }
    }

    /**
     * Animation looping disabler
     */
    public void noLoop(){
        this.animation.setLooping(false);
    }
}
