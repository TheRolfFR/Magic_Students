package Entities;

import Entities.Attacks.MeleeAttack;
import Entities.Attacks.RangedAttack;

public class Player extends Entity implements MeleeAttack, RangedAttack {
    private float life;

    public Player(float x, float y, float maxSpeed) {
        super(x, y, maxSpeed);
        this.life = 100;
    }
}
