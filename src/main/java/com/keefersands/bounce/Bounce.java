package com.keefersands.bounce;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
public final class Bounce extends JavaPlugin implements Listener {
    int MaxHeight;
    double initialV;
    Material plate;
    Material uBlock;
    @Override
    public void onEnable() { //When the Server Has been Enabled
        this.saveDefaultConfig();
        MaxHeight = this.getConfig().getInt("maxHeight");
        initialV = this.getConfig().getDouble("initialV");
        plate = Material.valueOf(this.getConfig().getString("TypeOfPad"));
        uBlock = Material.valueOf(this.getConfig().getString("BlockUnder"));
        getLogger().info("\u001B[1;33m" + "Bounce has been Enabled! \u001B[0m"); //Send to the Console
        //This Registers this class/plugin as an event listener
        getServer().getPluginManager().registerEvents(this, this);

    }
    @Override
    //When disabled just getLogger Saying its disabled
    public void onDisable() {
        getLogger().info("\u001B[1;34m" +"Bounce has been Disabled! \u001B[0m");
    }

    //This is just the syntax of onEntityInteract
    @EventHandler(ignoreCancelled = true)
    public void onEntityInteract(EntityInteractEvent event) {
        Block block = event.getBlock(); //use event to get block
        Entity entity = event.getEntity();//use event to get entity

        //This if statement first checks that the block is not equal to null
        //because i was getting errors and then checks if the activated block is
        //a stone plate
        if((!(block.getType()==null)) && block.getType().equals(plate)){
            //if it is we set location down one
            Location blockL = block.getLocation();
            blockL.setY(blockL.getY()-1);
            //then check to see if the block below it is gold
            if (blockL.getBlock().getType().equals(uBlock)) {
                //this grabs the scheduler so we can send tasks to it
                BukkitScheduler scheduler = getServer().getScheduler();
                Runnable entityBounce = Runnables.entityBounce(entity,initialV);
                //set delayed task of r to 0 delay to start it as soon as its activated
                scheduler.scheduleSyncDelayedTask(this, entityBounce, 0L);
                int x = 0; //also scheduling a setvelocity every 1 tick for 30 ticks
                while(x<MaxHeight) { //this is actually pretty smooth
                    scheduler.scheduleSyncDelayedTask(this, entityBounce, x*5L);
                    x++;
                }
            }
        }
    }

    @EventHandler
    //This is basically the same thing except with a player intiating the event
    public void PlayerInteractEvent(PlayerInteractEvent evt) {
        Player player = evt.getPlayer();
        Block b = evt.getClickedBlock(); //for players must be getClickedBlock();
        try {
            if (b.getType().equals(plate)) {
                Location blockL = b.getLocation();
                blockL.setY(blockL.getY() - 1);
                if (blockL.getBlock().getType().equals(uBlock)) {
                    BukkitScheduler scheduler = getServer().getScheduler();
                    Runnable playerBounce = Runnables.playerBounce(player,initialV); //grabs custom runnable
                    scheduler.scheduleSyncDelayedTask(this, playerBounce, 0L);
                    int x = 0;
                    while (x < MaxHeight) {
                        scheduler.scheduleSyncDelayedTask(this, playerBounce, x * 5L);
                        x++;
                    }
                }
            }
        }catch(NullPointerException e){
            //This just means an item is on the pad
        }
    }
}

