package Renderers;

import Entities.Entity;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

/**
 * Render for items in item rooms (yellow portals)
 */
public class ItemRenderer extends SpriteRenderer {
    private Image itemImage; // the item image

    private Vector2f imageOffset; // offset for display

    /**
     * Default rennder inititializing the entity, the item image and the tilesize offset
     * @param entity the entity it relies on
     * @param itemImage the item image
     * @param imageSize the size of the item image
     */
    public ItemRenderer(Entity entity, Image itemImage, Vector2f imageSize) {
        super(entity, imageSize);
        this.imageOffset = imageSize.copy().scale(0.5f);
        this.itemImage = itemImage;
    }

    /**
     * Opecity setter
     * @param opacity the opacity between 0 and 1
     */
    public final void setOpacity(float opacity) {
        this.opacity = Math.min(1, Math.max(0, opacity));
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     * @param x the x center position
     * @param y the y center position
     */
    public void render(Graphics g, int x, int y) {
        if (itemImage != null) { // avoid NullPointerException
            g.drawImage(this.itemImage, x - (int) imageOffset.getX(), y - (int) imageOffset.getY());
        }
    }
}
