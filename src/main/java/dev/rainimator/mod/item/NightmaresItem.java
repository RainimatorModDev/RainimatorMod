package dev.rainimator.mod.item;

import dev.rainimator.mod.item.util.ItemBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class NightmaresItem extends ItemBase {
    public NightmaresItem() {
        super(p -> p.maxDamage(32).fireproof());
    }

    @Override
    public void inventoryTick(ItemStack itemtack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemtack, world, entity, slot, selected);
        if (entity instanceof LivingEntity _entity)
            if (!_entity.getWorld().isClient())
                _entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 80, 0));
    }
}