package Renderers;

import Entities.Entity;
import Main.TimeScale;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.HashMap;

public class LivingBeingRenderer extends SpriteRenderer {

    protected static final String ACCEPTED_ACTIVITIES[] = {"Move", "Idle", "Dashing"};
    protected static final String ACCEPTED_VISION_DIRECTIONS[] = {"left", "right", "top", "bottom"};

    protected static boolean acceptedActivitiesContains(String activity) {
        boolean found = false;

        int i = 0;
        while (i < ACCEPTED_ACTIVITIES.length && !found) {
            found = ACCEPTED_ACTIVITIES[i].equals(activity);

            i++;
        }

        return found;
    }

    protected static boolean acceptedDirectionsContains(String direction) {
        boolean found = false;

        int i = 0;
        while (i < ACCEPTED_VISION_DIRECTIONS.length && !found) {
            found = ACCEPTED_VISION_DIRECTIONS[i].equals(direction);

            i++;
        }

        return found;
    }

    protected Color colorFilter;

    protected Vector2f lastFacedDirection;
    protected SpriteView lastView;

    protected HashMap<String, SpriteView> views;

    protected static final Vector2f zero = new Vector2f(0f, 0f);

    protected boolean hasCorrectName(String viewName) {
        String[] arr = viewName.split("(?=\\p{Lu})");

        if(arr.length == 2 && acceptedActivitiesContains(arr[0]) && acceptedDirectionsContains(arr[1]))
            return true;

        return false;
    }

    public void addView(String viewName, SpriteView view) {
        // if this view as a correct name and doesn't exists yet
        if (hasCorrectName(viewName) && !views.containsKey(viewName)) {
            // if there is no default view set it
            if (lastView == null) {
                lastView = view;
            } else if (viewName.equals("bottomIdle")) { // else if this is the bottom idle view set it
                this.lastView = view;
            }

            // finally put it in the list
            this.views.put(viewName, view);
        }
    }

    public LivingBeingRenderer(Entity entity, Vector2f tileSize, Color colorFilter) {
        super(entity, tileSize);
        init(colorFilter);
    }

    private void init(Color colorFilter) {
        this.colorFilter = colorFilter;
        this.lastView = null;
        this.lastFacedDirection = zero.copy();
        this.views = new HashMap<>();
    }

    private void setLastView(SpriteView v) {
        if(v != null) {
            this.lastView = v;
        }
    }

    /**
     * Method used when the developer wants an automatic choice of the view
     * @param g the graphics to draw on
     * @param facedDirection the direction faced by the living being
     */
    public void render(Graphics g, Vector2f facedDirection) {
        // update render if not paused
        if(TimeScale.getInGameTimeScale().getTimeScale() != 0f) {
            // Identify the activity of the living being (moving or not)
            String activity = "";
            if(this.entity.getSpeed().length() == 0f) activity = "Move";
            else activity = "Idle";

            render(g, facedDirection, activity);
        }
    }

    public void render(Graphics g, Vector2f facedDirection, String activity) {
        // update render if not paused
        if(TimeScale.getInGameTimeScale().getTimeScale() != 0f && acceptedActivitiesContains(activity)) {
            // update last faced direction
            if (!facedDirection.equals(zero)) {
                this.lastFacedDirection = facedDirection;
            }

            // Identify the direction of his vision
            String visionDirection = "";
            if (this.lastFacedDirection.getX() > 0) visionDirection = "bottom";
            else if (this.lastFacedDirection.getX() > 0) visionDirection = "right";
            else if (this.lastFacedDirection.getX() < 0) visionDirection = "left";
            else visionDirection = "top";

            this.setLastView(this.views.get(visionDirection + activity));
        }

        if(this.lastView != null) {

            // if game is not paused
            if(TimeScale.getInGameTimeScale().getTimeScale() != 0f) {
                this.lastView.start();
            } else {
                this.lastView.stop();
            }

            this.lastView.render(entity.getCenter(), colorFilter);
        }
    }
}
