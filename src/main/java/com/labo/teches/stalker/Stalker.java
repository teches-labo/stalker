package com.labo.teches.stalker;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.labo.teches.stalker.event.Event;

import java.util.List;

public final class Stalker extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new Event(), this);

        // Plugin startup logic
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(Player player : getServer().getOnlinePlayers()){
                List<Entity> entityList = player.getNearbyEntities(30,30,30);
                for(Entity entity : entityList) {
                    if (entity.getType() == EntityType.VILLAGER) {
                        Villager villager = (Villager) entity;
                        villager.setTarget(player);
                       //villager.zombify();
                    }
                    if(entity.getType() == EntityType.ZOMBIE_VILLAGER){
                        ZombieVillager zombieVillager = (ZombieVillager) entity;
                        zombieVillager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000,4, false,false));
                        zombieVillager.setSilent(true);
                    }
                }

            }
        }, 0, 5L);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
