package Renderers;

import Entities.Entity;
import org.newdawn.slick.geom.Vector2f;

/**
 * Basic abstract renderer
 */
public abstract class SpriteRenderer {
    /**
     * Says wether the debug rect will be displayed at render
     */
    protected boolean showDebugRect;
    /**
     * Opacity of the sprite renderer between 0 and 1
     */
    protected float opacity;
    /**
     * The entity related
     */
    protected Entity entity;
    /**
     * the renderer tile size
     */
    protected Vector2f tileSize;

    /**
     * Says wether the debug rect will be displayed at render
     * @return true if the debug rect is activated, false otherwise
     */
    public boolean isShowDebugRect() {
        return showDebugRect;
    }

    /**
     * debug rect setter
     * @param showDebugRect if you want to show the debug rect
     */
    public void setShowDebugRect(boolean showDebugRect) {
        this.showDebugRect = showDebugRect;
    }

    /**
     * Default constructor
     * @param entity the entity related
     * @param tileSize the tile size
     */
    public SpriteRenderer(Entity entity, Vector2f tileSize) {
        this.entity = entity;
        this.tileSize = tileSize;
        this.entity.setTileSize(tileSize.copy());
    }
}
