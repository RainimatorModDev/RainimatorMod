package com.rainimator.rainimatormod.item;

import com.rainimator.rainimatormod.registry.util.FoilItemBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

public class DiamondAppleSupperItem extends FoilItemBase {
    public DiamondAppleSupperItem() {
        super(p -> p.maxCount(16).rarity(Rarity.EPIC).food((new FoodComponent.Builder()).hunger(5).saturationModifier(5.0F).alwaysEdible().build()));
    }

    @Override
    public ItemStack finishUsing(ItemStack itemstack, World world, LivingEntity entity) {
        ItemStack retval = super.finishUsing(itemstack, world, entity);
        if (entity instanceof LivingEntity)
            if (!entity.world.isClient()) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 2));
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600, 2));
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 600, 4));
            }
        return retval;
    }
}