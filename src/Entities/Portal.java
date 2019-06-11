package Entities;

import Managers.PortalsManager;
import Renderers.PortalRenderer;
import org.newdawn.slick.Graphics;

public class Portal extends Entity {
    private int width;
    private int height;
    private boolean visible;

    protected String roomType;

    private PortalRenderer renderer;

    public Portal(float x, float y, int width, int height, int radius) {
        super(x, y, width, height, radius);
        this.width = width;
        this.height = height;
        this.visible = false;
        this.roomType = null;

        this.renderer = new PortalRenderer(this,this.getCenter().getX(), this.getCenter().getY());
    }

    public boolean isVisible() { return this.visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public void setType(String type) { this.roomType = type; }

    @Override
    public void move() {}

    public void update(int deltaTime) {
        this.renderer.update(deltaTime);
    }

    @Override
    public void render(Graphics g) {
        if (this.renderer != null) {
            this.renderer.Render(PortalsManager.roomColor.get(this.roomType));
            //this.renderer.render(g, (int) this.getPosition().getX(), (int) this.getPosition().getY());
        }
        super.render(g);
    }
}
