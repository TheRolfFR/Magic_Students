package Entities;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class SpriteRenderer {
    private Entity entity;

    int numberOfViews;
    float speed;

    private Animation idleView;
    private Animation topView;
    private Animation topLeftView;
    private Animation topRightView;
    private Animation leftView;
    private Animation rightView;
    private Animation bottomView;
    private Animation bottomLeftView;
    private Animation bottomRightView;

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    private static final Vector2f zero = new Vector2f(0, 0);

    public SpriteRenderer(Entity entity, Vector2f tileSize, Image image, int[] viewsFrames, float speed) throws Exception {
        // throws error if number of views incorrect
        if( (viewsFrames.length <= 0 || viewsFrames.length >2) && viewsFrames.length != 4 && viewsFrames.length != 8 ) {
            throw new Exception("invalid number of views");
        }

        this.entity = entity;
        this.numberOfViews = viewsFrames.length;
        this.speed = speed;

        // defines the offset
        Vector2f offset = SpriteRenderer.zero.copy();

        this.idleView = loadAnimation(image, offset, tileSize, 1); // idle is the first image

        if(this.numberOfViews == 1) {
            this.bottomView = loadAnimation(image, offset, tileSize, viewsFrames[0]);
        } else if(this.numberOfViews == 2) {
            this.leftView = loadAnimation(image, offset, tileSize, viewsFrames[0]);

            // offsetting
            offset.add(new Vector2f(tileSize.getX()*viewsFrames[0], 0));

            this.rightView = loadAnimation(image, offset, tileSize, viewsFrames[1]);
        } else {

            if(this.numberOfViews == 4 || this.numberOfViews == 8) {
                this.bottomView = loadAnimation(image, offset, tileSize, viewsFrames[0]);

                // offsetting
                offset.add(new Vector2f(tileSize.getX()*viewsFrames[0], 0));

                this.topView = loadAnimation(image, offset, tileSize, viewsFrames[1]);

                // offsetting
                offset.add(new Vector2f(tileSize.getX()*viewsFrames[1], 0));

                this.leftView = loadAnimation(image, offset, tileSize, viewsFrames[2]);

                // offsetting
                offset.add(new Vector2f(tileSize.getX()*viewsFrames[2], 0));

                this.rightView = loadAnimation(image, offset, tileSize, viewsFrames[3]);
            }

            if(this.numberOfViews == 8) {

                // offsetting
                offset.add(new Vector2f(tileSize.getX()*viewsFrames[3], 0));

                this.bottomLeftView = loadAnimation(image, offset, tileSize, viewsFrames[4]);

                // offsetting
                offset.add(new Vector2f(tileSize.getX()*viewsFrames[4], 0));

                this.bottomRightView = loadAnimation(image, offset, tileSize, viewsFrames[5]);

                // offsetting
                offset.add(new Vector2f(tileSize.getX()*viewsFrames[5], 0));

                this.topLeftView = loadAnimation(image, offset, tileSize, viewsFrames[6]);

                // offsetting
                offset.add(new Vector2f(tileSize.getX()*viewsFrames[6], 0));

                this.topRightView = loadAnimation(image, offset, tileSize, viewsFrames[7]);
            }
        }
    }

    private final Animation loadAnimation(Image image, Vector2f offset, Vector2f tileSize, int nbOfFrames) {
        SpriteSheet sp = new SpriteSheet(image.getSubImage((int) offset.getX(), (int) offset.getY(), nbOfFrames * (int) tileSize.getX(), (int) tileSize.getY()), (int) tileSize.getX(), (int) tileSize.getY());

        return new Animation(sp,(int) this.speed);
    }

    public void render() {
        Vector2f speed = entity.getSpeed();
        Vector2f position = entity.getPosition();

        Animation animation = this.idleView;

        if(!speed.equals(SpriteRenderer.zero)) {
            if(numberOfViews == 1) {
                animation = this.bottomView;
            } else {
                // 2 views or more

                // looking right
                if(speed.getX() > 0f) {
                    if(this.numberOfViews == 2 || this.numberOfViews == 4) {
                        animation = rightView;
                    } else {
                        // 8 views
                        if(speed.getY() > 0) {
                            // looking bottom right
                            animation = bottomRightView;
                        } else if(speed.getY() < 0) {
                            // looking top right
                            animation = topRightView;
                        } else {
                            // looking just right
                            animation = rightView;
                        }
                    }
                } else if(speed.getX() < 0) {
                    // looking left
                    if(this.numberOfViews == 2 || this.numberOfViews == 4) {
                        animation = leftView;
                    } else {
                        // 8 views
                        if(speed.getY() > 0) {
                            // looking bottom left
                            animation = bottomLeftView;
                        } else if(speed.getY() < 0) {
                            // looking top left
                            animation = topLeftView;
                        } else {
                            // looking just left
                            animation = leftView;
                        }
                    }
                } else {
                    // looking top or bottom
                    if(speed.getY() > 0) {
                        // looking bottom
                        animation = bottomView;
                    } else {
                        // looking top
                        animation = topView;
                    }
                }
            }
        }

        // draw sprite
        animation.draw((int) position.getX(), (int) position.getY());
    }
}
