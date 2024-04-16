package com.rainimator.rainimatormod.item.sword;

import com.iafenvoy.mcrconvertlib.item.SwordItemBase;
import com.iafenvoy.mcrconvertlib.item.ToolMaterialUtil;
import com.rainimator.rainimatormod.registry.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class BlackBoneTheBladeSingleHandItem extends SwordItemBase {
    public BlackBoneTheBladeSingleHandItem() {
        super(ToolMaterialUtil.of(1500, 0.0F, 10.0F, 0, 0, ModItems.RUBY), 3, -2.3F, new Settings().fireproof());
    }

    @Override
    public boolean postHit(ItemStack itemtack, LivingEntity entity, LivingEntity sourceentity) {
        boolean retval = super.postHit(itemtack, entity, sourceentity);
        entity.setOnFireFor(10);
        return retval;
    }
}

