package Entities;

import Managers.PortalsManager;
import Renderers.PortalRenderer;
import org.newdawn.slick.Graphics;

public class Portal extends Entity {
    //A portal is an object that the player can interact with to go in another room

    private boolean visible; //Indicate wether or not the portal is visible

    private String roomType; //Indicate which type of room the portal lead into

    private PortalRenderer renderer;

    /**
     * Constructor for a portal
     * @param x first coordonate of the position
     * @param y second coordonate of the position
     * @param width width of the image
     * @param height height of the image
     * @param radius hitbox radius of the portal
     */
    public Portal(float x, float y, int width, int height, int radius) {
        super(x, y, width, height, radius); //Create an entity
        this.visible = false;
        this.roomType = null;

        this.renderer = new PortalRenderer(this, this.getCenter().getX(), this.getCenter().getY());
    }

    /**
     * Indicate wether or not the portal is visible
     * @return true if the portal is visible, false otherwise
     */
    public boolean isVisible() { return this.visible; }

    /**
     * Setter for visible
     * @param visible wether or not the portal is visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
        if (this.visible) { //If the portal is visible
            this.renderer.restart(); //reload the renderer
        }
    }

    /**
     * Setter for the type of portal
     * @param type the type of the portal
     */
    public void setType(String type) { this.roomType = type; }

    /**
     * Getter for the type of the portal
     * @return the type of the portal
     */
    public String getType() {
        return this.roomType;
    }

    /**
     * Update the renderer of the portal
     * @param deltaTime time passed since the last frame
     */
    public void update(int deltaTime) {
        this.renderer.update(deltaTime);
    }

    @Override
    public void render(Graphics g) {
        if (this.renderer != null) {
            this.renderer.Render(PortalsManager.getRoomColor().get(this.roomType));
        }
        super.render(g);
    }
}
