package Renderers;

import Entities.Entity;
import Main.TimeScale;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;

public class LivingBeingRenderer extends SpriteRenderer {

    public static final String ACCEPTED_VISION_DIRECTIONS[] = {"left", "right", "top", "bottom"};

    private static boolean acceptedDirectionsContains(String direction) {
        boolean found = false;

        int i = 0;
        while (i < ACCEPTED_VISION_DIRECTIONS.length && !found) {
            found = ACCEPTED_VISION_DIRECTIONS[i].equals(direction);

            i++;
        }
        return found;
    }

    private Color colorFilter;

    protected ArrayList<String> activities;

    protected String lastActivity;
    protected String lastVisionDirection;
    protected Vector2f lastFacedDirection;
    protected SpriteView lastView;

    protected HashMap<String, SpriteView> views;

    protected static final Vector2f zero = new Vector2f(0f, 0f);
    /**
     * Sets last activity if the value exists
     * @param lastActivity the wanted activity
     */
    public void setLastActivity(String lastActivity) {
        if(this.activities.contains(lastActivity)) {
            this.lastActivity = lastActivity;
        }
    }

    /**
     * Checks if the name of the view is correct and returns the activity
     * @param viewName the view name to check
     * @return String - the corresponding activity or null
     */
    protected String hasCorrectName(String viewName) {
        String[] arr = viewName.split("(?=\\p{Lu})");

        if(arr.length == 2 && acceptedDirectionsContains(arr[0]))
            return arr[1];

        System.err.println("not correct view name : " + viewName);
        return null;
    }

    /**
     * Tries to add a view and put it in the hashmap
     * @param viewName the name of the view
     * @param view the view corresponding
     */
    public void addView(String viewName, SpriteView view) {
        // if this view as a correct name and doesn't exists yet
        String activity = hasCorrectName(viewName);
        if (activity != null && !views.containsKey(viewName)) {
            // if there is no default view set it
            if (lastView == null) {
                lastView = view;
            } else if (viewName.equals("bottomIdle")) { // else if this is the bottom idle view set it
                this.lastView = view;
            }

            // finally put data in the list
            if(!this.activities.contains(activity))
                this.activities.add(activity);
            this.views.put(viewName, view);
        }
    }

    /**
     * Default constructor with white color filter
     * @param entity the entity linked
     * @param tileSize the tilesize
     */
    public LivingBeingRenderer(Entity entity, Vector2f tileSize) {
        super(entity, tileSize);
        init(Color.white);
    }

    /**
     * Constructor with a color filter to color the views
     * @param entity the entity linked
     * @param tileSize the tilesize
     * @param colorFilter the color filter
     */
    public LivingBeingRenderer(Entity entity, Vector2f tileSize, Color colorFilter) {
        super(entity, tileSize);
        init(colorFilter);
    }

    /**
     * Init method for the constructors
     * mush faster than rewriting each time a complicated constructor
     * @param colorFilter the color filter
     */
    private void init(Color colorFilter) {
        this.colorFilter = colorFilter;
        this.lastView = null;
        this.lastFacedDirection = zero.copy();
        this.views = new HashMap<>();
        this.activities = new ArrayList<>();
    }

    /**
     * Sets a non null last view
     * @param v the view wanted
     */
    private void setLastView(SpriteView v) {
        if(v != null) {
            this.lastView = v;
        }
    }

    /**
     * Method made to get the wanted view or one of the other activities if not included
     * @param visionDirection vision direction string
     * @param activity activity string
     * @return the view wanted or almost or null
     */
    private SpriteView getView(String visionDirection, String activity) {
        SpriteView v;
        v = this.views.get(visionDirection + activity);
        int i = 0;
        while(i < activities.size() && v == null) {
            if(!activities.get(i).equals(activity)) {
                v = this.views.get(visionDirection + activities.get(i));
            }
            i++;
        }

        return v;
    }

    /** supposed update **/
    public void update(Vector2f facedDirection) {
        // update last faced direction
        if (!facedDirection.equals(zero)) {
            this.lastFacedDirection = facedDirection;

            // Identify the direction of his vision
            if (this.lastFacedDirection.getY() > 0) this.lastVisionDirection = "bottom";
            else if (this.lastFacedDirection.getX() > 0) this.lastVisionDirection = "right";
            else if (this.lastFacedDirection.getX() < 0) this.lastVisionDirection = "left";
            else this.lastVisionDirection = "top";
        }

        this.setLastView(this.getView(this.lastVisionDirection, this.lastActivity));
    }

    /**
     * Method used when the developer wants an automatic choice of the vision direction
     * @param g the graphics to draw on
     * @param facedDirection the direction faced by the living being
     */
    public void render(Graphics g, Vector2f facedDirection) {
        // update render if not paused
        if(TimeScale.getInGameTimeScale().getTimeScale() != 0f) {
            update(facedDirection);
        }

        if(this.lastView != null) {
            // if game is not paused (OBLIGATORY because I need to pause the animations)
            if(TimeScale.getInGameTimeScale().getTimeScale() != 0f) {
                this.lastView.start();
            } else {
                this.lastView.stop();
            }

            this.lastView.render(entity.getCenter(), colorFilter);
        }
    }
}
