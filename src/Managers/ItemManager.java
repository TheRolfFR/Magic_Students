package Managers;

import Entities.Item;
import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Player;
import Listeners.LivingBeingMoveListener;
import org.newdawn.slick.Graphics;

/**
 * Item manager for the item
 */
public class ItemManager implements LivingBeingMoveListener {
    private Item item;

    /**
     * Default constructor
     * @param player the player to add its {@link LivingBeingMoveListener}
     */
    public ItemManager(Player player) {
        player.addMoveListener(this);

        this.item = null;
    }

    /**
     * Regenerate a new item
     * @see Item#Item()
     */
    public void newItem() {
        this.item = new Item();
    }

    /**
     * In game rendering for the item
     * @param g the graphics to draw on
     * @see Item#render(Graphics)
     */
    public void render(Graphics g) {
        if (item != null) {
            item.render(g);
        }
    }

    /**
     * listner implemenation to upate the item if the player move
     * @param being the being moving
     * @see Item#update(Player)
     */
    @Override
    public void onMove(LivingBeing being) {
        if (item != null && being instanceof Player) {
            item = item.update((Player) being);
        }
    }
}
