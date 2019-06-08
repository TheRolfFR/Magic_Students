package Renderer;

import Entities.Entity;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class PlayerMarkerRenderer extends SpriteRenderer {

    private Image playerMarkerImage;
    private Vector2f playerMarkerImageCenter;
    private Vector2f playerMarkerDebugRectCenter;

    private float angle;

    private int markerRadius;

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getMarkerRadius() {
        return markerRadius;
    }

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

    public void Render(Graphics g, double angleFaced) {
        Vector2f location = entity.getCenter().sub(playerMarkerImageCenter);
        Image copy = playerMarkerImage.getScaledCopy(1);
        copy.rotate((float) angleFaced);
        g.drawImage(copy, (int) location.getX(), (int) location.getY());

        if(showDebugRect) {
            Color tmp = g.getColor();
            g.setColor(Color.blue);

            location = entity.getCenter().sub(playerMarkerDebugRectCenter);

            g.drawOval((int) location.getX(), (int) location.getY(), this.markerRadius*2, this.markerRadius*2);

            g.setColor(tmp);
        }
    }
}
