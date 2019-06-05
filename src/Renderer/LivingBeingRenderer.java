package Renderer;

import Entities.Entity;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class LivingBeingRenderer extends SpriteRenderer {

    private Color colorFilter;

    private SpriteView topView;
    private SpriteView leftView;
    private SpriteView rightView;
    private SpriteView bottomView;

    private SpriteView leftIdleView;
    private SpriteView topIdleView;
    private SpriteView rightIdleView;
    private SpriteView bottomIdleView;

    private static final Vector2f zero = new Vector2f(0f, 0f);

    public void setTopView(SpriteView topView) {
        this.topView = topView;
    }

    public void setLeftView(SpriteView leftView) {
        this.leftView = leftView;
    }

    public void setRightView(SpriteView rightView) {
        this.rightView = rightView;
    }

    public void setBottomView(SpriteView bottomView) {
        this.bottomView = bottomView;
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

    public LivingBeingRenderer(Entity entity, Vector2f tileSize, Color colorFilter) {
        super(entity, tileSize);
        init(colorFilter);
    }

    private void init(Color colorFilter) {
        this.colorFilter = colorFilter;
    }

    private SpriteView setTmp(SpriteView tmp, SpriteView v) {
        if(v != null) {
            return v;
        }
        return tmp;
    }

    public void render(Graphics g, Vector2f facedDirection) {
        Vector2f position = entity.getPosition();

        // default view null or bottom idle
        SpriteView tmp = setTmp(null, bottomIdleView);

        // standing still
        if(this.entity.getSpeed().length() < 0.5f) {
            // facing down
            /* if(facedDirection.getY() > 0) {
                // already done by default
            } */
            // facing right
            if(facedDirection.getX() > 0) {
                tmp = setTmp(tmp, rightIdleView);
            }
            // facing left
            else if(facedDirection.getX() < 0) {
                tmp = setTmp(tmp, leftIdleView);
            }
            // facing up
            else if(facedDirection.getY() < 0) {
                tmp = setTmp(tmp, topIdleView);
            }
        } else {
            // moving
            // facing right
            if(facedDirection.getX() > 0) {
                tmp = setTmp(tmp, rightView);
            }
            // facing left
            else if(facedDirection.getX() < 0) {
                tmp = setTmp(tmp, leftView);
            }
            // facing up
            else if(facedDirection.getY() < 0) {
                tmp = setTmp(tmp, topView);
            }
            // facing down
            if(facedDirection.getY() > 0) {
                tmp = setTmp(tmp, bottomView);
            }
        }

        if(tmp != null) {
            tmp.render((int) entity.getPosition().getX(), (int) entity.getPosition().getY(), colorFilter);
        }
    }
}
