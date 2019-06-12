package Entities;

import Entities.LivingBeings.Player;
import Entities.Projectiles.MeleeAttack;
import Entities.Projectiles.Snowball;
import Main.MainClass;

import java.util.ArrayList;
import java.util.Random;

public class Item extends Entity {

    public static ArrayList<Item> items = new ArrayList<>();

    private int typeOfItem;
    private static int HEALBUFFAMOUNT = 50;
    private static int MAXHPBUFFAMOUNT = 10;
    private static int MELEEBUFFAMOUNT = 5;
    private static int RANGEDBUFFAMOUNT = 5;
    private static int ARMORBUFFAMOUNT = 5;
    private static float SPEEDBUFFAMOUNT = Math.round(15/ MainClass.MAX_FPS);

    public Item(){
        super(MainClass.WIDTH/2, MainClass.HEIGHT/2, 25,25,13);
        Random random = new Random();
        this.typeOfItem = random.nextInt(6);
        this.setShowDebugRect(true);
        //loadImage();
    }

    public Item(int typeOfItem){
        super(MainClass.WIDTH/2, MainClass.HEIGHT/2, 25,25,13);
        this.typeOfItem = typeOfItem;
        //loadImage();
    }

    private void loadImage(){
        switch (this.typeOfItem){
            case 0 : /*Load image of heal*/ break;
            case 1 : /*Load image of max hp up*/break;
            case 2 : /*load image of melee damage up*/break;
            case 3 : /*Load image of ranged damage up*/break;
            case 4 : /*Load image of armor up*/break;
            case 5 : /*Load image of speed up*/break;
        }
    }

    @Override
    public void move() {}

    public Item update(Player player){
        if(collidesWith(player)){
            collidingType(player);
            return null;
        }
        return this;
    }

    private void collidingType(Player player){
        System.out.println("acc" + player.getAccelerationRate());
        System.out.println("armor" + player.getArmorPoints());
        System.out.println("chp" + player.getCurrentHealthPoints());
        System.out.println("mHP" + player.getMaxHealthPoints());
        System.out.println("Melee" + MeleeAttack.damage);
        System.out.println("ranged" + Snowball.damage);
        switch (this.typeOfItem){
            case 0 : player.heal((int) Math.round(HEALBUFFAMOUNT*MainClass.getDifficulty()));
                break;
            case 1 : player.buffHP((int) Math.round(MAXHPBUFFAMOUNT*MainClass.getDifficulty()));
                break;
            case 2 : MeleeAttack.damage = MeleeAttack.damage + (int) Math.round(MELEEBUFFAMOUNT*MainClass.getDifficulty());
                break;
            case 3 : Snowball.damage = Snowball.damage + (int) Math.round(RANGEDBUFFAMOUNT*MainClass.getDifficulty());
                break;
            case 4 : player.buffArmor((int) Math.round(ARMORBUFFAMOUNT*MainClass.getDifficulty()));
                break;
            case 5 : player.buffSpeed((int) Math.round(SPEEDBUFFAMOUNT*MainClass.getDifficulty()));
                break;
        }
        System.out.println("acc" + player.ACCELERATION_RATE);
        System.out.println("armor" + player.getArmorPoints());
        System.out.println("chp" + player.getCurrentHealthPoints());
        System.out.println("mHP" + player.getMaxHealthPoints());
        System.out.println("Melee" + MeleeAttack.damage);
        System.out.println("ranged" + Snowball.damage);
    }
}