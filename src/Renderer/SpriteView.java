package Renderer;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class SpriteView {
    private Animation animation;

    public SpriteView(String path, Vector2f tileSize) {
    }

    private void init(String path ,Vector2f tileSize, int duration) {
        init(path, tileSize, duration,null);
    }
    private void init(String path, Vector2f tileSize, int duration, Color filter) {
        try {
            Image original;
            if(filter.equals(null)) {
                original = new Image(path, false, Image.FILTER_NEAREST);
            } else {
                original = new Image(path, false, Image.FILTER_NEAREST, filter);
            }

            float scale = tileSize.getY()/ ((float) original.getHeight());

            Image copy = original.getScaledCopy(scale);

            SpriteSheet sp = new SpriteSheet(copy, (int) tileSize.getX(), (int) tileSize.getY());

            this.animation = new Animation(sp, duration);
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void render(int x, int y, Color filter) {
        if(this.animation != null) {
            this.animation.draw(x, y, filter);
        }
    }
}
