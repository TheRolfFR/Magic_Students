package Renderers;

import Entities.Entity;
import Entities.LivingBeings.LivingBeing;
import Listeners.LivingBeingMoveListener;
import Main.TimeScale;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * The player spell direction indicator (white circle and arrow)
 */
public class PlayerMarkerRenderer extends SpriteRenderer implements LivingBeingMoveListener {

    private Image playerMarkerImage;
    private Vector2f playerMarkerImageCenter;
    private Vector2f playerMarkerDebugRectCenter;

    private Image lastImage; // last image displayed

    private int markerRadius; // marker radius use dfor the debug rect

    /**
     * Default renderer : tries to load the image and crah otherwise
     * @param entity the entity related to this renderer
     * @param scale the scale of this renderer
     */
    public PlayerMarkerRenderer(Entity entity, float scale) {
        super(entity, new Vector2f(0, 0));

        try {
            this.playerMarkerImage = new Image("img/playerMarker/playerMarker.png", false, Image.FILTER_NEAREST).getScaledCopy(scale);

            this.playerMarkerImageCenter = new Vector2f(this.playerMarkerImage.getWidth()/2, this.playerMarkerImage.getHeight()/2);

            this.showDebugRect = false;
            this.markerRadius = (int) (28f/46f*this.playerMarkerImage.getWidth())/2;
            this.playerMarkerDebugRectCenter = new Vector2f(this.markerRadius, this.markerRadius);
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     * @param angleFaced the angle between the player and the mouse cursor
     */
    public void Render(Graphics g, double angleFaced) {
        // if game not paused
        if (TimeScale.getInGameTimeScale().getTimeScale() != 0f) {
            this.lastImage = playerMarkerImage.getScaledCopy(1);
            this.lastImage.rotate((float) angleFaced);
        }

        // determine the top left center of the image
        Vector2f location = entity.getCenter().sub(playerMarkerImageCenter);

        if (this.lastImage != null) {
            g.drawImage(this.lastImage, (int) location.getX(), (int) location.getY());
        }

        // show debug rect if activated
        if (this.showDebugRect) {
            g.setColor(Color.blue);

            location = entity.getCenter().sub(playerMarkerDebugRectCenter);

            g.drawOval((int) location.getX(), (int) location.getY(), this.markerRadius*2, this.markerRadius*2);
        }
    }

    @Override
    public void onMove(LivingBeing being) {

    }
}
