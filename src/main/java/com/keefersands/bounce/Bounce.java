package com.keefersands.bounce;

import org.apache.commons.lang.ObjectUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import static org.bukkit.Material.GOLD_BLOCK;
import static org.bukkit.Material.STONE_PLATE;
public final class Bounce extends JavaPlugin implements Listener {
    int MaxHeight = 0;
    double initalV = .2;
    @Override
    public void onEnable() { //When the Server Has been Enabled
        this.saveDefaultConfig();
        MaxHeight = this.getConfig().getInt("maxHeight");
        initalV = this.getConfig().getDouble("initialV");
        getLogger().info("\u001B[1;33m" + "Bounce has been Enabled! \u001B[0m"); //Send to the Console
        //This Registers this class/plugin as an event listener
        getServer().getPluginManager().registerEvents(this, this);

    }
    public void onReload(){

    }
    @Override
    //When disabled just getLogger Saying its disabled
    public void onDisable() {
        getLogger().info("\u001B[1;34m" +"Bounce has been Disabled! \u001B[0m");
    }
    //Okay so this is how commands are set up, player is sender so we can cast
    //We can get location from th eplayer and set it up 30
    //Must remember to add a command to the yml if you want it to work
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("up")) {
            Player player = (Player) sender;
            Location loc = player.getLocation();
            loc.setY(loc.getY() + 5);
            player.teleport(loc);
        }
        return false;
    }
    //This is just the syntax of onEntityInteract
    @EventHandler(ignoreCancelled = true)
    public void onEntityInteract(EntityInteractEvent event) {
        Block block = event.getBlock(); //use event to get block
        Entity entity = event.getEntity();//use event to get entity

        //This if statement first checks that the block is not equal to null
        //because i was getting errors and then checks if the activated block is
        //a stone plate
        if((!(block.getType()==null)) && block.getType().equals(STONE_PLATE)){
            //if it is we set location down one
            Location blockL = block.getLocation();
            blockL.setY(blockL.getY()-1);
            //then check to see if the block below it is gold
            if (blockL.getBlock().getType().equals(GOLD_BLOCK)) {
                //we create a new runnable (Which might be a good 2nd class idea)
                Runnable r = new Runnable() {
                    @Override
                    public void run() { //runable just sets velocity
                        entity.setVelocity(new Vector(0.0, initalV+.9, 0.0));
                    }
                };
                //this grabs the scheduler so we can send tasks to it
                BukkitScheduler scheduler = getServer().getScheduler();
                //set delayed task of r to 0 delay to start it as soon as its activated
                scheduler.scheduleSyncDelayedTask(this, r, 0L);
                int x = 0; //also scheduling a setvelocity every 1 tick for 30 ticks
                while(x<MaxHeight) { //this is actually pretty smooth
                    scheduler.scheduleSyncDelayedTask(this, r, x*1L);
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
            if (b.getType().equals(STONE_PLATE)) {
                Location blockL = b.getLocation();
                blockL.setY(blockL.getY() - 1);
                if (blockL.getBlock().getType().equals(GOLD_BLOCK)) {
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            player.setVelocity(new Vector(0.0, initalV, 0.0));
                            player.setVelocity(player.getVelocity().multiply(5));
                        }
                    };
                    BukkitScheduler scheduler = getServer().getScheduler();
                    scheduler.scheduleSyncDelayedTask(this, r, 0L);
                    int x = 0;
                    while (x < MaxHeight) {
                        scheduler.scheduleSyncDelayedTask(this, r, x * 1L);
                        x++;
                    }
                }
            }
        }catch(NullPointerException e){
            //This just means an item is on the pad
        }
    }
}

