package com.rainimator.rainimatormod.entity;

import com.rainimator.rainimatormod.registry.ModEntities;
import com.rainimator.rainimatormod.registry.ModItems;
import com.rainimator.rainimatormod.registry.ModParticles;
import com.rainimator.rainimatormod.registry.util.MonsterEntityBase;
import com.rainimator.rainimatormod.util.Stage;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import java.util.Random;

public class PatrickEntity extends MonsterEntityBase implements RangedAttackMob {
    public static final Stage.StagedEntityTextureProvider texture = Stage.ofProvider("patrick");

    public PatrickEntity(EntityType<PatrickEntity> type, World world) {
        super(type, world, EntityGroup.DEFAULT);
        this.experiencePoints = 0;
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GENERAL_PATRICK_LONG_KNIVES));
        this.equipStack(EquipmentSlot.OFFHAND, new ItemStack(ModItems.GENERAL_PATRICK_LONG_KNIVES));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        DefaultAttributeContainer.Builder builder = MobEntity.createMobAttributes();
        builder = builder.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D);
        builder = builder.add(EntityAttributes.GENERIC_MAX_HEALTH, 120.0D);
        builder = builder.add(EntityAttributes.GENERIC_ARMOR, 30.0D);
        builder = builder.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0D);
        builder = builder.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D);
        builder = builder.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 5.0D);
        builder = builder.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D);
        return builder;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, false, false));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, false) {
            protected double getSquaredMaxAttackDistance(LivingEntity entity) {
                return (this.mob.getWidth() * this.mob.getWidth() + entity.getWidth());
            }
        });
        this.goalSelector.add(3, new WanderAroundGoal(this, 1.0D));
        this.targetSelector.add(4, new RevengeGoal(this));
        this.goalSelector.add(5, new LongDoorInteractGoal(this, true));
        this.goalSelector.add(6, new LongDoorInteractGoal(this, false));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.goalSelector.add(8, new SwimGoal(this));
        this.goalSelector.add(1, new ProjectileAttackGoal(this, 1.25D, 20, 10.0F) {
            public boolean shouldContinue() {
                return this.canStart();
            }
        });
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return Registry.SOUND_EVENT.get(new Identifier("entity.generic.hurt"));
    }

    @Override
    public SoundEvent getDeathSound() {
        return Registry.SOUND_EVENT.get(new Identifier("entity.generic.death"));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        if (Math.random() < 0.2D) {
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x, y + 0.5D, z + 0.5D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x + 0.5D, y + 1.5D, z, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x, y + 1.0D, z - 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x - 1.0D, y + 2.0D, z, 0.0D, 0.0D, 0.0D);
        } else if (Math.random() < 0.2D) {
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x + 1.0D, y + 0.5D, z + 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x - 1.0D, y + 1.5D, z - 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x - 0.5D, y + 1.0D, z + 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x + 1.0D, y + 2.0D, z - 0.5D, 0.0D, 0.0D, 0.0D);
        } else if (Math.random() < 0.2D) {
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x + 1.0D, y + 1.0D, z - 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x - 1.0D, y + 2.0D, z + 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x + 1.0D, y + 1.5D, z + 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x - 1.0D, y + 0.5D, z - 1.0D, 0.0D, 0.0D, 0.0D);
        } else if (Math.random() < 0.2D) {
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x, y + 1.0D, z - 0.5D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x - 0.5D, y + 2.0D, z, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x, y + 0.5D, z + 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.YELLOW_LIGHTENING, x + 1.0D, y + 1.5D, z, 0.0D, 0.0D, 0.0D);
        }
        if (Math.random() < 0.7D)
            if (!this.world.isClient()) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 1));
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 1));
            }
        return super.damage(source, amount);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason reason, EntityData livingdata, NbtCompound tag) {
        EntityData retval = super.initialize(world, difficulty, reason, livingdata, tag);
        if (world instanceof ServerWorld _level) {
            HildaEntity hildaEntity = new HildaEntity(ModEntities.HILDA, _level);
            hildaEntity.refreshPositionAndAngles(this.getX() + MathHelper.nextInt(new Random(), 1, 4), this.getY(), this.getZ() + MathHelper.nextInt(new Random(), 1, 4), world.getRandom().nextFloat() * 360.0F, 0.0F);
            hildaEntity.initialize(_level, world.getLocalDifficulty(hildaEntity.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
            world.spawnEntity(hildaEntity);
        }
        return retval;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.world.isClient())
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 80, 0));
    }

    @Override
    public void attack(LivingEntity target, float flval) {
        PatrickEntityProjectile entityarrow = new PatrickEntityProjectile(ModEntities.PATRICK_PROJECTILE, this, this.world);
        double d0 = target.getY() + target.getStandingEyeHeight() - 1.1D;
        double d1 = target.getX() - this.getX();
        double d3 = target.getZ() - this.getZ();
        entityarrow.setVelocity(d1, d0 - entityarrow.getY() + Math.sqrt(d1 * d1 + d3 * d3) * 0.20000000298023224D, d3, 1.6F, 12.0F);
        this.world.spawnEntity(entityarrow);
    }
}