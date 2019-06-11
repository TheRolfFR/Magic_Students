package Renderers;

import Entities.Entity;
import Main.TimeScale;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class LivingBeingRenderer extends SpriteRenderer {

    private Color colorFilter;

    private Vector2f lastFacedDirection;
    private SpriteView lastView;

    private SpriteView topView;
    private SpriteView leftView;
    private SpriteView rightView;
    private SpriteView bottomView;

    private SpriteView leftIdleView;
    private SpriteView topIdleView;
    private SpriteView rightIdleView;
    private SpriteView bottomIdleView;

    private SpriteView topDashView;
    private SpriteView leftDashView;
    private SpriteView rightDashView;
    private SpriteView bottomDashView;

    private static final Vector2f zero = new Vector2f(0f, 0f);

    public void setTopView(SpriteView topView) { this.topView = topView; }
    public void setLeftView(SpriteView leftView) {
        this.leftView = leftView;
    }
    public void setRightView(SpriteView rightView) {
        this.rightView = rightView;
    }
    public void setBottomView(SpriteView bottomView) {
        this.bottomView = bottomView;
        setLastView(this.bottomIdleView);
    }

    public void setLeftIdleView(SpriteView leftIdleView) {
        this.leftIdleView = leftIdleView;
    }
    public void setTopIdleView(SpriteView topIdleView) {
        this.topIdleView = topIdleView;
    }
    public void setRightIdleView(SpriteView rightIdleView) {
        this.rightIdleView = rightIdleView;
    }
    public void setBottomIdleView(SpriteView bottomIdleView) {
        this.bottomIdleView = bottomIdleView;
    }
    public LivingBeingRenderer(Entity entity, Vector2f tileSize) {
        super(entity, tileSize);
        init(Color.white);
    }

    public void setTopDashView(SpriteView topDashView) { this.topDashView = topDashView; }
    public void setLeftDashView(SpriteView leftDashView) { this.leftDashView = leftDashView; }
    public void setRightDashView(SpriteView rightDashView) { this.rightDashView = rightDashView; }
    public void setBottomDashView(SpriteView bottomDashView) { this.bottomDashView = bottomDashView; }

    public LivingBeingRenderer(Entity entity, Vector2f tileSize, Color colorFilter) {
        super(entity, tileSize);
        init(colorFilter);
    }

    private void init(Color colorFilter) {
        this.colorFilter = colorFilter;
        this.lastView = null;
        this.lastFacedDirection = zero.copy();
    }

    private void setLastView(SpriteView v) {
        if(v != null) {
            this.lastView = v;
        }
    }
    public void render(Graphics g, Vector2f facedDirection) {
        // update render if not paused
        if(TimeScale.getInGameTimeScale().getTimeScale() != 0f) {
            // update last faced direction
            if(!facedDirection.equals(zero)) {
                this.lastFacedDirection = facedDirection;
            }

            // standing still
            if(this.entity.getSpeed().length() == 0f) {
                // facing down
                if(this.lastFacedDirection.getY() > 0) {
                    setLastView(this.bottomIdleView);
                }
                // facing right
                if(this.lastFacedDirection.getX() > 0) {
                    this.setLastView(rightIdleView);
                }
                // facing left
                else if(this.lastFacedDirection.getX() < 0) {
                    this.setLastView(leftIdleView);
                }
                // facing up
                else if(this.lastFacedDirection.getY() < 0) {
                    this.setLastView(topIdleView);
                }
            } else {
                // moving
                // facing right
                if(this.lastFacedDirection.getX() > 0) {
                    this.setLastView(rightView);
                }
                // facing left
                else if(this.lastFacedDirection.getX() < 0) {
                    this.setLastView(leftView);
                }
                // facing up
                else if(this.lastFacedDirection.getY() < 0) {
                    this.setLastView(topView);
                }
                // facing down
                else if(this.lastFacedDirection.getY() > 0) {
                    this.setLastView(bottomView);
                }
            }
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
