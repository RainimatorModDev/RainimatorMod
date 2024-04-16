package com.rainimator.rainimatormod.item.tool;

import com.iafenvoy.mcrconvertlib.item.ToolMaterialUtil;
import com.iafenvoy.mcrconvertlib.world.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;

public class RubyShovelItem extends ShovelItem {
    public RubyShovelItem() {
        super(ToolMaterialUtil.of(1000, 10.0F, 3.0F, 3, 20), 1.0F, -2.2F, new Settings());
    }

    @Override
    public boolean postHit(ItemStack itemtack, LivingEntity entity, LivingEntity sourceentity) {
        boolean retval = super.postHit(itemtack, entity, sourceentity);
        if (Math.random() < 0.5D) {
            entity.setOnFireFor(5);
            entity.damage(DamageUtil.build(entity, DamageTypes.IN_FIRE), 2.0F);
        }
        return retval;
    }
}
