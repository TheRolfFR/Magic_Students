package Entities.LivingBeings.Monsters.Melee;

import org.newdawn.slick.geom.Vector2f;

public interface KnightConstants {
    float ATTACK_LOADING_DURATION = 0.5f;
    float STUN_AFTER_ATTACK_DURATION = 0.2f;
    float MAX_SPEED = 4.25f;
    float ACCELERATION_RATE = 1;
    float KNIGHT_TILEZSIZE_SCALE = 2f;
    Vector2f KNIGHT_TILESIZE = new Vector2f(48, 48).scale(KNIGHT_TILEZSIZE_SCALE);
}
