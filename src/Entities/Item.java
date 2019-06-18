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

/**
 * An item is an object that increase the statistics of the player
 */
public class Item extends Entity {

    private int typeOfItem; //an integer between 0 and 5 that indicate which item it is

    private ItemRenderer renderer;

    private static int HEALBUFFAMOUNT = 50; //Base heal provide by an item
    private static int MAXHPBUFFAMOUNT = 10; //Base increase of HP provide by an item
    private static int MELEEBUFFAMOUNT = 5; // Base increase of damage provide by an item
    private static int ARMORBUFFAMOUNT = 5;  //Base increase of armor provide by an item
    private static float SPEEDBUFFAMOUNT = Math.round(15/ MainClass.MAX_FPS); //Base increase od speed provide by an item

    private static final float ITEM_IMAGE_SCALE = 2f;
    private static SpriteSheet ITEMS_SPRITESHEET = null;
    private static final String ITEMS_SPRITESHEET_PATH = "img/items/items.png";
    private static final Vector2f ITEM_TILESIZE = new Vector2f(16, 16).scale(ITEM_IMAGE_SCALE);

    /**
     * Default constructor that creates a random item
     */
    public Item() {
        super(MainClass.WIDTH / 2, MainClass.HEIGHT / 2, 25, 25, 13); //Create an entity located in the middle of the screen
        Random random = new Random();
        this.typeOfItem = random.nextInt(6); //Pick randomly of type of item
        this.setShowDebugRect(true);
        loadImage(); //Add the image to the renderer
    }

    /**
     * Constructor that creates a specific item
     * @param typeOfItem the type of the item
     */
    public Item(int typeOfItem) {
        super(MainClass.WIDTH / 2, MainClass.HEIGHT / 2, 25, 25, 13);
        this.typeOfItem = typeOfItem;
        loadImage();
    }

    /**
     * returns the correct image from the items spritesheet, but crashes if the image creation fails
     * @param index the index of the wanted image
     * @return the image of the index wanted, null else
     */
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

    /**
     * Create a renderer for the image
     */
    private void loadImage() {
        this.renderer = new ItemRenderer(this, getItemImageFromSpriteSheet(this.typeOfItem), ITEM_TILESIZE);
    }

    /**
     * Indicate if the player pick the item
     * @param player the player
     * @return the item if the player doesn't pick it, null otherwise
     */
    public Item update(Player player) {
        if (collidesWith(player)) { //If the player is on the item
            collidingType(player); //Apply the buff provide by the item
            return null;
        }
        return this;
    }

    /**
     * Apply the buff provide by the item to the player
     * @param player the player
     */
    private void collidingType(Player player) {
        switch (this.typeOfItem) { //the buff is proportional to the current difficulty
            case 0 : player.heal( Math.round(HEALBUFFAMOUNT* GameStats.getInstance().getDifficulty()));
                break;
            case 1 : player.buffHP( Math.round(MAXHPBUFFAMOUNT*GameStats.getInstance().getDifficulty()));
                break;
            case 2 : MeleeAttack.increaseDamage( Math.round(MELEEBUFFAMOUNT*GameStats.getInstance().getDifficulty()));
                break;
            case 3 : Fireball.increaseDamage( Math.round(MELEEBUFFAMOUNT*GameStats.getInstance().getDifficulty()));
                break;
            case 4 : player.buffArmor( Math.round(ARMORBUFFAMOUNT*GameStats.getInstance().getDifficulty()));
                break;
            case 5 : player.buffSpeed( Math.round(SPEEDBUFFAMOUNT*GameStats.getInstance().getDifficulty()));
                break;
        }
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     */
    @Override
    public void render(Graphics g) {
        this.renderer.render(g, (int) this.getCenter().getX(), (int) this.getCenter().getY());
        super.render(g);
    }
}