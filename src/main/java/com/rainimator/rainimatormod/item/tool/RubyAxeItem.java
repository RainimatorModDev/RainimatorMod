package com.rainimator.rainimatormod.item.tool;

import com.iafenvoy.mcrconvertlib.item.ToolMaterialUtil;
import com.iafenvoy.mcrconvertlib.world.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class RubyAxeItem extends AxeItem {
    public RubyAxeItem() {
        super(ToolMaterialUtil.of(1500, 10.0F, 8.0F, 3, 20), 1.0F, -2.2F, new Settings());
    }

    @Override
    public boolean postHit(ItemStack itemtack, LivingEntity entity, LivingEntity sourceentity) {
        boolean ret_val = super.postHit(itemtack, entity, sourceentity);
        if (Math.random() < 0.5D) {
            entity.setOnFireFor(5);
            entity.damage(DamageUtil.build(entity, DamageTypes.IN_FIRE), 2.0F);
        }
        return ret_val;
    }
}

