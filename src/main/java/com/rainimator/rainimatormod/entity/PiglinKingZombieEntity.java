package com.rainimator.rainimatormod.entity;

import com.rainimator.rainimatormod.RainimatorMod;
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
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.*;

public class PiglinKingZombieEntity extends MonsterEntityBase {
    public static final Stage.StagedEntityTextureProvider texture = Stage.ofProvider("piglin_king_zombie");
    private final ServerBossBar bossInfo = new ServerBossBar(this.getDisplayName(), BossBar.Color.YELLOW, BossBar.Style.PROGRESS);

    public PiglinKingZombieEntity(EntityType<PiglinKingZombieEntity> type, World world) {
        super(type, world, EntityGroup.UNDEAD);
        this.experiencePoints = 0;
        this.setPersistent();
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GLUTTON_SLEDGEHAMMER));
        this.equipStack(EquipmentSlot.HEAD, new ItemStack(ModItems.GLUTTON_HELMET));
        this.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
        this.equipStack(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        DefaultAttributeContainer.Builder builder = MobEntity.createMobAttributes();
        builder = builder.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D);
        builder = builder.add(EntityAttributes.GENERIC_MAX_HEALTH, 150.0D);
        builder = builder.add(EntityAttributes.GENERIC_ARMOR, 30.0D);
        builder = builder.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D);
        builder = builder.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D);
        builder = builder.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 5.0D);
        builder = builder.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D);
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
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, false, false));
        this.targetSelector.add(4, new RevengeGoal(this));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.goalSelector.add(6, new SwimGoal(this));
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return Registry.SOUND_EVENT.get(new Identifier("entity.zombified_piglin.hurt"));
    }

    @Override
    public SoundEvent getDeathSound() {
        return Registry.SOUND_EVENT.get(new Identifier("entity.zombified_piglin.death"));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source == DamageSource.FALL)
            return false;
        if (source == DamageSource.CACTUS)
            return false;
        if (source == DamageSource.LIGHTNING_BOLT)
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
        if ((WorldAccess) world instanceof World _level)
            SoundUtil.playSound(_level, x, y, z, new Identifier(RainimatorMod.MOD_ID, "blackbone_skill"), 5, 1);
        if ((WorldAccess) world instanceof ServerWorld _level)
            _level.spawnParticles((DefaultParticleType) (ModParticles.YELLOW_STARS), x, y, z, 100, 1, 2, 1, 1);
        if (world.getDifficulty() != Difficulty.PEACEFUL) {
            if (!this.world.isClient() && this.getServer() != null)
                SoundUtil.playSound(this.world, x, y, z, new Identifier(RainimatorMod.MOD_ID, "glutton_boss_music"), 1, 1);
            Runnable callback = () -> {
                if (this.isAlive())
                    if (!this.world.isClient() && this.getServer() != null)
                        SoundUtil.playSound(this.world, x, y, z, new Identifier(RainimatorMod.MOD_ID, "glutton_boss_music"), 1, 1);
            };
            Timeout.create(5520, callback);
            Timeout.create(11040, callback);
            Timeout.create(16560, callback);
            Timeout.create(22080, callback);
            Timeout.create(27600, callback);
            Timeout.create(33120, callback);
        }
        return retval;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.isAlive())
            SoundUtil.stopSound(this.world, new Identifier(RainimatorMod.MOD_ID, "glutton_boss_music"));
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