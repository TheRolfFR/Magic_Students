package Entities;

import Entities.Attacks.MeleeAttack;

public class Melee extends Monster implements MeleeAttack {
    public boolean canMove() { return true; }
}
