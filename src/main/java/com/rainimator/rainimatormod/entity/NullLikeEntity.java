package com.rainimator.rainimatormod.entity;

import com.rainimator.rainimatormod.RainimatorMod;
import com.rainimator.rainimatormod.registry.ModEffects;
import com.rainimator.rainimatormod.registry.ModItems;
import com.rainimator.rainimatormod.registry.ModParticles;
import com.rainimator.rainimatormod.registry.util.MonsterEntityBase;
import com.rainimator.rainimatormod.util.SoundUtil;
import com.rainimator.rainimatormod.util.Stage;
import com.rainimator.rainimatormod.util.Timeout;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class NullLikeEntity extends MonsterEntityBase {
    public static final Stage.StagedEntityTextureProvider texture = Stage.ofProvider("null_like").setEyeTextureId("null_like_eye");
    private final ServerBossBar bossInfo = new ServerBossBar(this.getDisplayName(), BossBar.Color.WHITE, BossBar.Style.PROGRESS);

    public NullLikeEntity(EntityType<NullLikeEntity> type, World world) {
        super(type, world, EntityGroup.UNDEAD);
        this.experiencePoints = 0;
        this.setPersistent();
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.BLACK_DEATH_SWORD));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        DefaultAttributeContainer.Builder builder = MobEntity.createMobAttributes();
        builder = builder.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D);
        builder = builder.add(EntityAttributes.GENERIC_MAX_HEALTH, 120.0D);
        builder = builder.add(EntityAttributes.GENERIC_ARMOR, 30.0D);
        builder = builder.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0D);
        builder = builder.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D);
        builder = builder.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 5.0D);
        builder = builder.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2.0D);
        return builder;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2D, false) {
            protected double getSquaredMaxAttackDistance(LivingEntity entity) {
                return (this.mob.getWidth() * this.mob.getWidth() + entity.getWidth());
            }
        });
        this.goalSelector.add(2, new WanderAroundGoal(this, 1.0D));
        this.targetSelector.add(3, new RevengeGoal(this));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, PlayerEntity.class, false, false));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.goalSelector.add(6, new SwimGoal(this));
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceToClosestPlayer) {
        return false;
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
        if (this.hasStatusEffect(ModEffects.FEAR_DARK))
            this.clearStatusEffects();
        else if (this.hasStatusEffect(ModEffects.SOUL_DEATH))
            this.clearStatusEffects();
        else if (this.hasStatusEffect(StatusEffects.POISON))
            this.clearStatusEffects();
        else if (this.hasStatusEffect(StatusEffects.WEAKNESS))
            this.clearStatusEffects();
        else if (Math.random() < 0.3D) {
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x, y + 0.5D, z + 0.5D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x + 0.5D, y + 1.5D, z, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x, y + 1.0D, z - 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x - 1.0D, y + 2.0D, z, 0.0D, 0.0D, 0.0D);
        } else if (Math.random() < 0.3D) {
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x + 1.0D, y + 0.5D, z + 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x - 1.0D, y + 1.5D, z - 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x - 0.5D, y + 1.0D, z + 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x + 1.0D, y + 2.0D, z - 0.5D, 0.0D, 0.0D, 0.0D);
        } else if (Math.random() < 0.3D) {
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x + 1.0D, y + 1.0D, z - 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x - 1.0D, y + 2.0D, z + 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x + 1.0D, y + 1.5D, z + 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x - 1.0D, y + 0.5D, z - 1.0D, 0.0D, 0.0D, 0.0D);
        } else if (Math.random() < 0.3D) {
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x, y + 1.0D, z - 0.5D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x - 0.5D, y + 2.0D, z, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x, y + 0.5D, z + 1.0D, 0.0D, 0.0D, 0.0D);
            this.world.addParticle((ParticleEffect) ModParticles.SWEATER_SNOW, x + 1.0D, y + 1.5D, z, 0.0D, 0.0D, 0.0D);
        }
        if (Math.random() < 0.7D)
            if (!this.world.isClient()) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 1));
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 1));
            }
        if (source == DamageSource.FALL)
            return false;
        if (source == DamageSource.DROWN)
            return false;
        if (source == DamageSource.LIGHTNING_BOLT)
            return false;
        if (source.isExplosive())
            return false;
        if (source == DamageSource.ANVIL)
            return false;
        if (source == DamageSource.WITHER)
            return false;
        if (source.getName().equals("witherSkull"))
            return false;
        return super.damage(source, amount);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason reason, EntityData livingdata, NbtCompound tag) {
        EntityData retval = super.initialize(world, difficulty, reason, livingdata, tag);
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        if (world instanceof World _level)
            SoundUtil.playSound(_level, x, y, z, new Identifier(RainimatorMod.MOD_ID, "blued_diamond_skill_1"), 5.0F, 1.0F);
        if (world instanceof ServerWorld _level)
            _level.spawnParticles((ParticleEffect) ModParticles.FLOWER_WHITE, x, y, z, 300, 2.0D, 3.0D, 2.0D, 0.3D);
        if (world.getDifficulty() != Difficulty.PEACEFUL) {
            if (!this.world.isClient() && this.getServer() != null)
                this.getServer().getCommandManager().execute(this.getCommandSource().withSilent().withLevel(4), "playsound rainimator:null_boss_music neutral @a ~ ~ ~");
            Runnable callback = () -> {
                if (this.isAlive())
                    if (!this.world.isClient() && this.getServer() != null)
                        this.getServer().getCommandManager().execute(this.getCommandSource().withSilent().withLevel(4), "playsound rainimator:null_boss_music neutral @a ~ ~ ~");
            };
            Timeout.create(6420, callback);
            Timeout.create(12840, callback);
            Timeout.create(19260, callback);
            Timeout.create(25680, callback);
            Timeout.create(32100, callback);
            Timeout.create(38520, callback);
        }

        return retval;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.world.isClient())
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 1));
        if (!this.isAlive())
            SoundUtil.stopSound(this.world, new Identifier(RainimatorMod.MOD_ID, "null_boss_music"));
    }

    @Override
    public boolean canUsePortals() {
        return false;
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void mobTick() {
        super.mobTick();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }
}