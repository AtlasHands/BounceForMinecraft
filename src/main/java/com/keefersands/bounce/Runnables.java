package com.keefersands.bounce;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Created by Alpine Tree on 5/1/2017.
 */
public class Runnables {
    public static Runnable playerBounce(Player player, double initialV) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                player.setVelocity(new Vector(0.0, initialV, 0.0));
                player.setVelocity(player.getVelocity().multiply(5));
            }
        };
        return r;
    }
    public static Runnable entityBounce(Entity entity, double initialV){
        Runnable r = new Runnable() {
            @Override
            public void run() { //runable just sets velocity
                entity.setVelocity(new Vector(0.0, initialV +.9, 0.0));
            }
        };
        return r;
    }
}
