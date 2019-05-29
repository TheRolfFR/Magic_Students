package Entities;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class SpriteRenderer {
    private Entity entity;

    private int numberOfViews;
    private float speed;

    private Animation actualView;
    private Animation topView;
    private Animation topLeftView;
    private Animation topRightView;
    private Animation leftView;
    private Animation rightView;
    private Animation bottomView;
    private Animation bottomLeftView;
    private Animation bottomRightView;

    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;

        this.actualView.setSpeed(this.speed);
        this.topView.setSpeed(this.speed);
        this.topLeftView.setSpeed(this.speed);
        this.topRightView.setSpeed(this.speed);
        this.leftView.setSpeed(this.speed);
        this.rightView.setSpeed(this.speed);
        this.bottomLeftView.setSpeed(this.speed);
        this.bottomRightView.setSpeed(this.speed);
    }

    private static final Vector2f zero = new Vector2f(0, 0);

    public SpriteRenderer(Entity entity, Vector2f tileSize, Image image, int[] viewsFrames, float speed) {
        // throws error if number of views incorrect
        if ( (viewsFrames.length <= 0 || viewsFrames.length >2) &&
                (viewsFrames.length != 4 && viewsFrames.length != 8) ) {
            System.err.println("invalid number of views");
            System.exit(1);
        }

        this.width = (int) tileSize.getX();
        this.height = (int) tileSize.getY();

        this.entity = entity;
        this.numberOfViews = viewsFrames.length;
        this.speed = speed;

        // defines the offset
        Vector2f offset = SpriteRenderer.zero.copy();

        this.actualView = loadAnimation(image, offset, tileSize, 1); // idle is the first image

        if (this.numberOfViews == 1) {
            this.bottomView = loadAnimation(image, offset, tileSize, viewsFrames[0]);
        }
        else if (this.numberOfViews == 2) {
            this.leftView = loadAnimation(image, offset, tileSize, viewsFrames[0]);

            // offsetting
            offset.add(new Vector2f(tileSize.getX()*viewsFrames[0], 0));

            this.rightView = loadAnimation(image, offset, tileSize, viewsFrames[1]);
        }
        else {

            if (this.numberOfViews == 4 || this.numberOfViews == 8) {
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

            if (this.numberOfViews == 8) {

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

    private Animation loadAnimation(Image image, Vector2f offset, Vector2f tileSize, int nbOfFrames) {
        SpriteSheet sp = new SpriteSheet(image.getSubImage((int) offset.getX(), (int) offset.getY(), nbOfFrames * (int) tileSize.getX(), (int) tileSize.getY()), (int) tileSize.getX(), (int) tileSize.getY());

        return new Animation(sp,(int) this.speed);
    }

    public void render(Vector2f facedDirection) {
        Vector2f position = entity.getPosition();

        if (!facedDirection.equals(SpriteRenderer.zero)) {
            if (numberOfViews == 1) {
                this.actualView = this.bottomView;
            }
            else {
                // 2 views or more

                // looking right
                if (facedDirection.getX() > 0f) {
                    if (this.numberOfViews == 2 || this.numberOfViews == 4) {
                        this.actualView = rightView;
                    }
                    else {
                        // 8 views
                        if (facedDirection.getY() > 0) {
                            // looking bottom right
                            this.actualView = bottomRightView;
                        } else if (facedDirection.getY() < 0) {
                            // looking top right
                            this.actualView = topRightView;
                        } else {
                            // looking just right
                            this.actualView = rightView;
                        }
                    }
                }
                else if(facedDirection.getX() < 0) {
                    // looking left
                    if (this.numberOfViews == 2 || this.numberOfViews == 4) {
                        this.actualView = leftView;
                    }
                    else {
                        // 8 views
                        if (facedDirection.getY() > 0) {
                            // looking bottom left
                            this.actualView = bottomLeftView;
                        } else if (facedDirection.getY() < 0) {
                            // looking top left
                            this.actualView = topLeftView;
                        } else {
                            // looking just left
                            this.actualView = leftView;
                        }
                    }
                }
                else {
                    // looking top or bottom
                    if (facedDirection.getY() > 0) {
                        // looking bottom
                        this.actualView = bottomView;
                    } else {
                        // looking top
                        this.actualView = topView;
                    }
                }
            }
            this.actualView.start();
        } else {
            this.actualView.stop();
        }

        // draw sprite
        this.actualView.draw((int) position.getX(), (int) position.getY());
    }
}
