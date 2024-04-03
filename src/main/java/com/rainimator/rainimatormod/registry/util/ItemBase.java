package com.rainimator.rainimatormod.registry.util;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Function;

public class ItemBase extends Item {
    public ItemBase(Function<Settings, Settings> properties) {
        super(properties.apply(ModCreativeTab.createProperty().rarity(Rarity.COMMON).maxCount(64)));
    }

    @Override
    public void appendTooltip(ItemStack itemstack, World world, List<Text> list, TooltipContext flag) {
        super.appendTooltip(itemstack, world, list, flag);
        if (this instanceof IRainimatorInfo)
            list.add(new LiteralText(RainimatorInfoManager.getHoverText()));
    }
}