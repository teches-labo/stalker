package com.labo.teches.stalker.event;

import com.labo.teches.stalker.mobu.Thief;
import net.minecraft.server.v1_16_R2.EntityVillager;
import net.minecraft.server.v1_16_R2.PathfinderGoalPanic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftVillager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Event implements Listener {
    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        if(e.getDamager().getType() == EntityType.VILLAGER){
            // 村人ゾンビの攻撃をキャンセル
            e.setCancelled(true);

            // 攻撃されたプレイヤーの名前を取得
            Player player = Bukkit.getPlayer(e.getEntity().getName());
            // プレイヤーの所持アイテム取得
            ArrayList<ItemStack> list = new ArrayList<ItemStack>(Arrays.asList(player.getInventory().getContents()));
            ArrayList<ItemStack> hasItem = new ArrayList<ItemStack>();
            for(ItemStack item : list){
                if(item == null){
                    continue;
                }
                hasItem.add(item);
            }
            if(hasItem.size() == 0){
                return;
            }

            // 手持ちのアイテムをランダムで取得
            Random rnd = new Random();
            int selectItem = rnd.nextInt(hasItem.size());

            player.getInventory().setItem(
                    list.indexOf(hasItem.get(selectItem)),
                    new ItemStack(Material.AIR)
            );

            Location loc = e.getEntity().getLocation();
            loc.getWorld().playSound(loc, Sound.BLOCK_GRASS_BREAK,0.8F,0);

            int i = rnd.nextInt(10);
            if(i % 2  == 0) {
                loc.getWorld().playSound(loc, Sound.ENTITY_WITCH_AMBIENT, 0.5F, 4);
            }else{
                loc.getWorld().playSound(loc, Sound.ENTITY_WITCH_AMBIENT, 0.5F, -2);
            }

            Villager villager = (Villager) e.getDamager();
            villager.damage(0, e.getEntity());
        };
    }

    @EventHandler
    public void EntityCombustEvent(EntityCombustEvent e){
        // 村人ゾンビのみ炎上しない
        if(e.getEntity().getType() == EntityType.ZOMBIE_VILLAGER) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void EntitySpawnEvent(EntitySpawnEvent e){
        if(e.getEntity().getType() == EntityType.ZOMBIE) {
            Thief thief = new Thief(e.getLocation());
            ((CraftWorld) e.getLocation().getWorld()).getHandle().addEntity(thief);
            e.setCancelled(true);
//            Location loc = e.getEntity().getLocation();
//            loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE_VILLAGER);
        }
    }
}
