package Entities;

import Entities.Attacks.RangedAttack;

public class Ranged extends Monster implements RangedAttack {
    public boolean can_move() { return true; }
}
