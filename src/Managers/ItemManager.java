package Managers;

import Entities.Item;
import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Player;
import Listeners.LivingBeingMoveListener;
import org.newdawn.slick.Graphics;

public class ItemManager implements LivingBeingMoveListener {
    private Item item;

    public ItemManager(Player player) {
        player.addMoveListener(this);

        this.item = null;
    }

    public void newItem() {
        this.item = new Item();
    }

    public void render(Graphics g) {
        if (item != null) {
            item.render(g);
        }
    }

    @Override
    public void onMove(LivingBeing being) {
        if (item != null && being instanceof Player) {
            item = item.update((Player) being);
        }
    }
}
