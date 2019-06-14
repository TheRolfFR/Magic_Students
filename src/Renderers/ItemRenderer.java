package Renderers;

import Entities.Entity;
import Main.TimeScale;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class ItemRenderer extends SpriteRenderer {
    private Animation animation;
    private Image lastImage;

    private Vector2f tileSizeOffset;

    public ItemRenderer(Entity entity, Image image, Vector2f tileSize, int frameDuration) {
        super(entity, tileSize);
        this.tileSizeOffset = tileSize.copy().scale(0.5f);
        this.animation = new Animation(new SpriteSheet(image, (int) tileSize.getX(), (int) tileSize.getY()), frameDuration);
    }

    public final void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public void render(Graphics g, int x, int y) {
        // if game not paused
        if(TimeScale.getInGameTimeScale().getTimeScale() != 0f) {
            this.lastImage = animation.getCurrentFrame().copy();
        }

        if(lastImage != null) {
            g.drawImage(this.lastImage, x - (int) tileSizeOffset.getX(), y - (int) tileSizeOffset.getY());
        }
    }
}
