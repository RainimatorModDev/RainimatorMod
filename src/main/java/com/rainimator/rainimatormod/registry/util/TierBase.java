package com.rainimator.rainimatormod.registry.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class TierBase {
    public static ToolMaterial of(int uses, float speed, float attackDamageBonus, int level, int enchantmentLevel) {
        return of(uses, speed, attackDamageBonus, level, enchantmentLevel, new Item[0]);
    }

    public static ToolMaterial of(int uses, float speed, float attackDamageBonus, int level, int enchantmentLevel, Block... repairIngredient) {
        Item[] items = new Item[repairIngredient.length];
        for (int i = 0; i < repairIngredient.length; i++)
            items[i] = repairIngredient[i].asItem();
        return of(uses, speed, attackDamageBonus, level, enchantmentLevel, items);
    }

    public static ToolMaterial of(int uses, float speed, float attackDamageBonus, int level, int enchantmentLevel, Item... repairIngredients) {
        return new ToolMaterial() {
            @Override
            public int getDurability() {
                return uses;
            }

            @Override
            public float getMiningSpeedMultiplier() {
                return speed;
            }

            @Override
            public float getAttackDamage() {
                return attackDamageBonus;
            }

            @Override
            public int getMiningLevel() {
                return level;
            }

            @Override
            public int getEnchantability() {
                return enchantmentLevel;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return Ingredient.ofItems(repairIngredients);
            }
        };
    }
}