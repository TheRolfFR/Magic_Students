package Renderer;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class SpriteView {
    protected Animation animation;
    protected Vector2f animationCenter;

    public SpriteView(String path, Vector2f tileSize, int duration) {
        init(path, tileSize ,duration, null);
    }

    public SpriteView(String path, Vector2f tileSize, int duration, Color filter) {
        init(path, tileSize, duration, filter);
    }

    private void init(String path ,Vector2f tileSize, int duration) {
        init(path, tileSize, duration,null);
    }
    private void init(String path, Vector2f tileSize, int duration, Color filter) {
        try {
            Image original;
            if(filter == null) {
                original = new Image(path, false, Image.FILTER_NEAREST);
            } else {
                original = new Image(path, false, Image.FILTER_NEAREST, filter);
            }

            float scale = tileSize.getY()/ ((float) original.getHeight());

            Image copy = original.getScaledCopy(scale);

            SpriteSheet sp = new SpriteSheet(copy, (int) tileSize.getX(), (int) tileSize.getY());

            animationCenter = new Vector2f(tileSize.getX()/2f, tileSize.getY()/2f);

            this.animation = new Animation(sp, duration);
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.animation.stop();
    }

    public void start() {
        this.animation.start();
    }

    public void render(Vector2f center, Color filter) {
        if(this.animation != null) {
            Vector2f location = center.copy().sub(animationCenter);
            this.animation.draw((int) location.getX(), (int) location.getY(), filter);
        }
    }
}
