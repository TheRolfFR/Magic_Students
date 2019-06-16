package Entities;

import Entities.LivingBeings.Player;
import Entities.Projectiles.Fireball;
import Entities.Projectiles.MeleeAttack;
import Main.GameStats;
import Main.MainClass;
import Renderers.ItemRenderer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;
import java.util.Random;

public class Item extends Entity {

    public static ArrayList<Item> items = new ArrayList<>();

    private int typeOfItem;

    private ItemRenderer renderer;

    private static int HEALBUFFAMOUNT = 50;
    private static int MAXHPBUFFAMOUNT = 10;
    private static int MELEEBUFFAMOUNT = 5;
    private static int RANGEDBUFFAMOUNT = 5;
    private static int ARMORBUFFAMOUNT = 5;
    private static float SPEEDBUFFAMOUNT = Math.round(15/ MainClass.MAX_FPS);

    private static final float ITEM_IMAGE_SCALE = 2f;
    private static SpriteSheet ITEMS_SPRITESHEET = null;
    private static final String ITEMS_SPRITESHEET_PATH = "img/items/items.png";
    private static final Vector2f ITEM_TILESIZE = new Vector2f(16, 16).scale(ITEM_IMAGE_SCALE);
    private static final int ITEM_FRAME_DURATION = 100000;

    private static Image getItemImageFromSpriteSheet(int index) {
        if (ITEMS_SPRITESHEET == null) {
            try {
                Image tmp = new Image(ITEMS_SPRITESHEET_PATH, false, Image.FILTER_NEAREST).getScaledCopy(ITEM_IMAGE_SCALE);
                ITEMS_SPRITESHEET = new SpriteSheet(tmp, (int) ITEM_TILESIZE.getX(), (int) ITEM_TILESIZE.getY());
            } catch (SlickException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        return ITEMS_SPRITESHEET.getSprite(index, 0);
    }

    public Item() {
        super(MainClass.WIDTH / 2, MainClass.HEIGHT / 2, 25, 25, 13);
        Random random = new Random();
        this.typeOfItem = random.nextInt(6);
        this.setShowDebugRect(true);
        loadImage();
    }

    public Item(int typeOfItem) {
        super(MainClass.WIDTH / 2, MainClass.HEIGHT / 2, 25, 25, 13);
        this.typeOfItem = typeOfItem;
        loadImage();
    }

    private void loadImage() {
        this.renderer = new ItemRenderer(this, getItemImageFromSpriteSheet(this.typeOfItem), ITEM_TILESIZE, ITEM_FRAME_DURATION);
    }

    @Override
    public void move() {}

    public Item update(Player player) {
        if (collidesWith(player)) {
            collidingType(player);
            return null;
        }
        return this;
    }

    private void collidingType(Player player) {
        switch (this.typeOfItem) {
            case 0 : player.heal((int) Math.round(HEALBUFFAMOUNT* GameStats.getInstance().getDifficulty()));
                break;
            case 1 : player.buffHP((int) Math.round(MAXHPBUFFAMOUNT*GameStats.getInstance().getDifficulty()));
                break;
            case 2 : MeleeAttack.increaseDamage((int) Math.round(MELEEBUFFAMOUNT*GameStats.getInstance().getDifficulty()));
                break;
            case 3 : Fireball.increaseDamage((int) Math.round(MELEEBUFFAMOUNT*GameStats.getInstance().getDifficulty()));
                break;
            case 4 : player.buffArmor((int) Math.round(ARMORBUFFAMOUNT*GameStats.getInstance().getDifficulty()));
                break;
            case 5 : player.buffSpeed((int) Math.round(SPEEDBUFFAMOUNT*GameStats.getInstance().getDifficulty()));
                break;
        }
    }

    @Override
    public void render(Graphics g) {
        this.renderer.render(g, (int) this.getCenter().getX(), (int) this.getCenter().getY());
        super.render(g);
    }
}