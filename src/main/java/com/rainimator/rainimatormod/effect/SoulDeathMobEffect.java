package com.rainimator.rainimatormod.effect;

import com.rainimator.rainimatormod.RainimatorMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class SoulDeathMobEffect extends StatusEffect {
    public SoulDeathMobEffect() {
        super(StatusEffectCategory.HARMFUL, -16764058);
    }

    @Override
    public String getTranslationKey() {
        return "effect." + RainimatorMod.MOD_ID + ".soul_death";
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.damage(DamageSource.GENERIC, 1.0F);
        if (!entity.world.isClient()) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 4));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60, 1));
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}