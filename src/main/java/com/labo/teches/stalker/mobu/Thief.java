package com.labo.teches.stalker.mobu;

import net.minecraft.server.v1_16_R2.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;

import java.lang.reflect.Field;
import java.util.Map;

public class Thief extends EntityVillager {
    private static Field attributeField;

    public Thief(Location loc) {
        super(EntityTypes.VILLAGER, ((CraftWorld) loc.getWorld()).getHandle());

        this.setPosition(loc.getX(), loc.getY(), loc.getZ());

        this.setCustomName(new ChatComponentText(ChatColor.LIGHT_PURPLE + "盗賊"));
        this.setCustomNameVisible(true);
        this.setHealth(5);
        this.setInvulnerable(false);

        try {
            registerGenericAttribute(this.getBukkitEntity(), Attribute.GENERIC_ATTACK_DAMAGE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initPathfinder() {
        this.getAttributeMap().b().add(new AttributeModifiable(GenericAttributes.ATTACK_DAMAGE, (a) -> {a.setValue(10.0);}));
        this.getAttributeMap().b().add(new AttributeModifiable(GenericAttributes.FOLLOW_RANGE, (a) -> {a.setValue(1.0);}));

        this.goalSelector.a(3, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, false));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
        this.goalSelector.a(2, new PathfinderGoalRandomLookaround(this));
        this.goalSelector.a(0, new PathfinderGoalPanic(this, 1.25D));
//        this.goalSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));

    }


    static {
        try {
            attributeField = net.minecraft.server.v1_16_R2.AttributeMapBase.class.getDeclaredField("b");
            attributeField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void registerGenericAttribute(org.bukkit.entity.Entity entity, Attribute attribute) throws IllegalAccessException {
        net.minecraft.server.v1_16_R2.AttributeMapBase attributeMapBase = ((org.bukkit.craftbukkit.v1_16_R2.entity.CraftLivingEntity)entity).getHandle().getAttributeMap();
        Map<AttributeBase, AttributeModifiable> map = (Map<AttributeBase, AttributeModifiable>) attributeField.get(attributeMapBase);
        net.minecraft.server.v1_16_R2.AttributeBase attributeBase = org.bukkit.craftbukkit.v1_16_R2.attribute.CraftAttributeMap.toMinecraft(attribute);
        net.minecraft.server.v1_16_R2.AttributeModifiable attributeModifiable = new net.minecraft.server.v1_16_R2.AttributeModifiable(attributeBase, net.minecraft.server.v1_16_R2.AttributeModifiable::getAttribute);
        map.put(attributeBase, attributeModifiable);
    }
}

