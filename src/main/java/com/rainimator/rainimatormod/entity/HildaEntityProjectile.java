package com.rainimator.rainimatormod.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class HildaEntityProjectile extends PersistentProjectileEntity implements FlyingItemEntity {

    public HildaEntityProjectile(EntityType<? extends HildaEntityProjectile> type, World world) {
        super(type, world);
    }

    public HildaEntityProjectile(EntityType<? extends HildaEntityProjectile> type, LivingEntity entity, World world) {
        super(type, entity, world);
    }

    @Override
    protected void onHit(LivingEntity livingEntity) {
        super.onHit(livingEntity);
        livingEntity.setStuckArrowCount(livingEntity.getStuckArrowCount() - 1);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack getStack() {
        return new ItemStack(Items.ENDER_PEARL);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(Items.ENDER_PEARL);
    }
}