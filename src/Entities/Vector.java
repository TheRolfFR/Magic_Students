package Entities;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

class Vector {
    private float x;
    private float y;
    private float norm;

    Vector(float x, float y) {
        this.x = x;
        this.y = y;
        computeNorm();
    }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }

    public float getX() { return this.x; }
    public float getY() { return this.y; }
    float getNorm() { return this.norm; }

    void computeNorm() {
        this.norm = (float) sqrt(pow(this.x, 2) + pow(this.y, 2));
    }

    public void add(Vector v) {
        this.x += v.getX();
        this.y += v.getY();
    }
    public void multiply(float a) {
        this.x *= a;
        this.y *= a;
    }
}
