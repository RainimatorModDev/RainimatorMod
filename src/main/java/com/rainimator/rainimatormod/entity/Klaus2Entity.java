package com.rainimator.rainimatormod.entity;

import com.iafenvoy.annotationlib.annotation.registration.AttributeBuilder;
import com.iafenvoy.mcrconvertlib.item.MonsterEntityBase;
import com.iafenvoy.mcrconvertlib.misc.Timeout;
import com.iafenvoy.mcrconvertlib.render.Stage;
import com.iafenvoy.mcrconvertlib.world.SoundUtil;
import com.rainimator.rainimatormod.RainimatorMod;
import com.rainimator.rainimatormod.registry.ModEffects;
import com.rainimator.rainimatormod.registry.ModItems;
import com.rainimator.rainimatormod.registry.ModParticles;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
public class Klaus2Entity extends MonsterEntityBase {
    public static final Stage.StagedEntityTextureProvider texture = Stage.ofProvider(RainimatorMod.MOD_ID,"klaus_2");
    private final ServerBossBar bossInfo = new ServerBossBar(this.getDisplayName(), BossBar.Color.BLUE, BossBar.Style.PROGRESS);

    public Klaus2Entity(EntityType<Klaus2Entity> type, World world) {
        super(type, world, EntityGroup.DEFAULT);
        this.experiencePoints = 0;
        this.setPersistent();
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.SOUL_RAIDING_HAMMER));
        this.equipStack(EquipmentSlot.HEAD, new ItemStack(ModItems.KING_NORMAL_CROWN));
    }

    @AttributeBuilder
    public static DefaultAttributeContainer.Builder createAttributes() {
        DefaultAttributeContainer.Builder builder = MobEntity.createMobAttributes();
        builder = builder.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D);
        builder = builder.add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0D);
        builder = builder.add(EntityAttributes.GENERIC_ARMOR, 20.0D);
        builder = builder.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0D);
        builder = builder.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D);
        builder = builder.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 6.0D);
        builder = builder.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2.0D);
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
        this.goalSelector.add(5, new LongDoorInteractGoal(this, false));
        this.goalSelector.add(6, new LongDoorInteractGoal(this, true));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.goalSelector.add(8, new SwimGoal(this));
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return Registries.SOUND_EVENT.get(new Identifier("entity.vindicator.hurt"));
    }

    @Override
    public SoundEvent getDeathSound() {
        return Registries.SOUND_EVENT.get(new Identifier("entity.vindicator.death"));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        Entity sourceentity = source.getAttacker();
        if (sourceentity != null) {
            if (sourceentity instanceof LivingEntity _ent)
                this.setTarget(_ent);
            if (this.getMainHandStack().getItem() == ModItems.DEEP_WAR_HAMMER || this.getMainHandStack().getItem() == ModItems.LASER_SWORD) {
                if (Math.random() < 0.3) {
                    ItemStack _setstack = new ItemStack(ModItems.LASER_SWORD);
                    _setstack.setCount(1);
                    this.setStackInHand(Hand.MAIN_HAND, _setstack);
                } else if (Math.random() < 0.4) {
                    ItemStack _setstack = new ItemStack(ModItems.DEEP_WAR_HAMMER);
                    _setstack.setCount(1);
                    this.setStackInHand(Hand.MAIN_HAND, _setstack);
                }
            }
            if (EnchantmentHelper.getLevel(Enchantments.PROTECTION, this.getEquippedStack(EquipmentSlot.HEAD)) != 0) {
                if (!this.getWorld().isClient()) {
                    this.addStatusEffect(new StatusEffectInstance(ModEffects.PURIFICATION, 200, 0));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 2));
                }
                if (Math.random() < 0.1) {
                    if (!this.getWorld().isClient())
                        this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 0));
                    SoundUtil.playSound(this.getWorld(), x, y, z, new Identifier("block.anvil.land"), 1, 1);
                    if (this.getWorld() instanceof ServerWorld _level)
                        _level.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, x, y, z, 200, 0, 10, 0, 0.002);
                }
            }
            if (EnchantmentHelper.getLevel(Enchantments.SHARPNESS, this.getMainHandStack()) != 0) {
                if (!this.getWorld().isClient())
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 0));
                if (Math.random() < 0.1) {
                    if (sourceentity instanceof LivingEntity _entity && !_entity.getWorld().isClient())
                        _entity.addStatusEffect(new StatusEffectInstance(ModEffects.STUNNED, 100, 0));
                    SoundUtil.playSound(this.getWorld(), x, y, z, new Identifier(RainimatorMod.MOD_ID, "stunned"), 5, 1);
                    if (this.getWorld() instanceof ServerWorld _level)
                        _level.spawnParticles((DefaultParticleType) (ModParticles.YELLOW_STARS), x, y, z, 50, 1, 2, 1, 1);
                }
            }
            if (this.getMainHandStack().getItem() == ModItems.SOUL_RAIDING_HAMMER) {
                if (!this.getWorld().isClient())
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 1));
                if (Math.random() < 0.1) {
                    SoundUtil.playSound(this.getWorld(), x, y, z, new Identifier("block.anvil.land"), 1, 1);
                    if (this.getWorld() instanceof ServerWorld _level)
                        _level.spawnParticles(ParticleTypes.END_ROD, x, y, z, 100, 2, 3, 2, 0.002);
                    if (sourceentity instanceof LivingEntity _entity && !_entity.getWorld().isClient())
                        _entity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 100, 1));
                }
            }
            if (this.getMainHandStack().getItem() == ModItems.SEIZING_SHADOW_HALBERD) {
                if (!this.getWorld().isClient())
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 2));
                if (Math.random() < 0.1) {
                    SoundUtil.playSound(this.getWorld(), x, y, z, new Identifier("block.anvil.land"), 1, 1);
                    if (this.getWorld() instanceof ServerWorld _level)
                        _level.spawnParticles(ParticleTypes.END_ROD, x, y, z, 100, 2, 3, 2, 0.002);
                    if (sourceentity instanceof LivingEntity _entity && !_entity.getWorld().isClient()) {
                        _entity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 100, 1));
                        _entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 1));
                        _entity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 1));
                    }
                }
            }
        }
        return super.damage(source, amount);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (damageSource.getSource() instanceof PersistentProjectileEntity)
            return true;
        if (damageSource.isOf(DamageTypes.FALL))
            return true;
        if (damageSource.isOf(DamageTypes.CACTUS))
            return true;
        if (damageSource.isOf(DamageTypes.LIGHTNING_BOLT))
            return true;
        if (damageSource.isOf(DamageTypes.EXPLOSION))
            return true;
        if (damageSource.isOf(DamageTypes.TRIDENT))
            return true;
        if (damageSource.isOf(DamageTypes.FALLING_ANVIL))
            return true;
        if (damageSource.isOf(DamageTypes.DRAGON_BREATH))
            return true;
        if (damageSource.isOf(DamageTypes.WITHER))
            return true;
        if (damageSource.isOf(DamageTypes.WITHER_SKULL))
            return true;
        return super.isInvulnerableTo(damageSource);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason reason, EntityData livingdata, NbtCompound tag) {
        EntityData retval = super.initialize(world, difficulty, reason, livingdata, tag);
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        if (EnchantmentHelper.getLevel(Enchantments.SHARPNESS, this.getMainHandStack()) == 0) {
            if (EnchantmentHelper.getLevel(Enchantments.PROTECTION, this.getEquippedStack(EquipmentSlot.HEAD)) == 0) {
                if (world instanceof World _level)
                    SoundUtil.playSound(_level, this.getX(), this.getY(), this.getZ(), new Identifier(RainimatorMod.MOD_ID, "block.beacon.activate"), 5.0F, 1.0F);
                if (world instanceof ServerWorld _level)
                    _level.spawnParticles((ParticleEffect) ModParticles.LIGHTENING_ARC, x, y, z, 100, 2.0D, 3.0D, 2.0D, 0.001D);
                this.getMainHandStack().addEnchantment(Enchantments.SHARPNESS, 4);
                this.getEquippedStack(EquipmentSlot.HEAD).addEnchantment(Enchantments.PROTECTION, 4);
            }
        }
        if (world.getDifficulty() != Difficulty.PEACEFUL) {
            Runnable callback = () -> {
                if (this.isAlive())
                    SoundUtil.playSound(this.getWorld(), this.getX(), this.getY(), this.getZ(), new Identifier(RainimatorMod.MOD_ID, "klaus_boss_music"), 1, 1F);
            };
            Timeout.create(0, callback);
            Timeout.create(6060, callback);
            Timeout.create(12120, callback);
            Timeout.create(18180, callback);
            Timeout.create(24240, callback);
            Timeout.create(30300, callback);
            Timeout.create(36360, callback);
        }
        return retval;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.isAlive())
            SoundUtil.stopSound(this.getWorld(), new Identifier(RainimatorMod.MOD_ID, "klaus_boss_music"));
        if (this.getOffHandStack().getItem() == Blocks.AIR.asItem())
            if (this.getMainHandStack().getItem() == Blocks.AIR.asItem()) {
                if (!this.getWorld().isClient()) {
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 2));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.UNLUCK, 100, 2));
                }
                return;
            }
        if (this.getOffHandStack().getItem() == Items.EMERALD)
            if (this.getMainHandStack().getItem() == ModItems.SEIZING_SHADOW_HALBERD) {
                if (!this.getWorld().isClient()) {
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 1));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 1));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100, 1));
                    this.addStatusEffect(new StatusEffectInstance(ModEffects.PURIFICATION, 100, 0));
                }
                return;
            }
        if (this.getOffHandStack().getItem() == Blocks.AIR.asItem())
            if (this.getMainHandStack().getItem() == ModItems.SEIZING_SHADOW_HALBERD) {
                if (!this.getWorld().isClient()) {
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 0));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 0));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100, 0));
                    this.addStatusEffect(new StatusEffectInstance(ModEffects.PURIFICATION, 100, 0));
                }
            }
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