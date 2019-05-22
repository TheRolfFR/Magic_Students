package Entities;

import Entities.Attacks.MeleeAttack;
import Entities.Attacks.RangedAttack;

public class Player extends Entity implements MeleeAttack, RangedAttack {
    private float life;

    public Player(float x, float y, float maxSpeed, float accelerationRate) {
        super(x, y, maxSpeed, accelerationRate);
        this.life = 100;
    }

    public boolean canMove() { return true; }
}
