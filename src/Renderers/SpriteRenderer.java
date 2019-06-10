package Renderers;

import Entities.Entity;
import org.newdawn.slick.geom.Vector2f;

public abstract class SpriteRenderer {
    protected boolean showDebugRect;
    protected float opacity;
    protected Entity entity;
    protected Vector2f tileSize;

    public boolean isShowDebugRect() {
        return showDebugRect;
    }

    public void setShowDebugRect(boolean showDebugRect) {
        this.showDebugRect = showDebugRect;
    }

    public SpriteRenderer(Entity entity, Vector2f tileSize) {
        this.entity = entity;
        this.tileSize = tileSize;
        this.entity.setTileSize(tileSize.copy());
    }

    public void setOpacity(float opacity) {

    }
}
