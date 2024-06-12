package dev.rainimator.mod.item.block;

import com.iafenvoy.mcrconvertlib.misc.Consumer5;
import com.iafenvoy.mcrconvertlib.misc.RandomHelper;
import com.iafenvoy.mcrconvertlib.misc.Timeout;
import com.iafenvoy.mcrconvertlib.world.EntityUtil;
import com.iafenvoy.mcrconvertlib.world.SoundUtil;
import dev.rainimator.mod.RainimatorMod;
import dev.rainimator.mod.item.block.entity.DarkObsidianBlockEntity;
import dev.rainimator.mod.registry.ModBlockEntities;
import dev.rainimator.mod.registry.ModEntities;
import dev.rainimator.mod.registry.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class DarkObsidianBlock extends BlockWithEntity {
    public static final HashMap<Item, Consumer5<PlayerEntity, ServerWorld, Integer, Integer, Integer>> consumers = new HashMap<>();
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final String NBT_KEY = "biome";

    public DarkObsidianBlock() {
        super(FabricBlockSettings.create().requiresTool().strength(50, 1200));
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    public static synchronized void initConsumers() {
        consumers.put(ModItems.LIGHT_HEART, (entity, world, x, y, z) -> {
            EntityUtil.summon(ModEntities.HEROBRINE, world, x, y, z);
            EntityUtil.summon(ModEntities.ZOMBIES, world, x, y, z - RandomHelper.nextInt(1, 3));
            EntityUtil.summon(ModEntities.DARK_ZOMBIE, world, x, y, z + RandomHelper.nextInt(1, 3));
            EntityUtil.summon(ModEntities.AZALEA, world, x + RandomHelper.nextInt(1, 3), y, z);
        });
        consumers.put(Blocks.WITHER_ROSE.asItem(), (entity, world, x, y, z) -> EntityUtil.summon(ModEntities.KRALOS, world, x, y, z));
        consumers.put(Items.TOTEM_OF_UNDYING, (entity, world, x, y, z) -> {
            EntityUtil.summon(ModEntities.KLAUS, world, x, y, z);
            EntityUtil.summon(EntityType.PILLAGER, world, x, y, z - RandomHelper.nextInt(1, 3));
            EntityUtil.summon(EntityType.PILLAGER, world, x, y, z + RandomHelper.nextInt(1, 3));
            EntityUtil.summon(EntityType.PILLAGER, world, x - RandomHelper.nextInt(1, 3), y, z);
            EntityUtil.summon(EntityType.PILLAGER, world, x + RandomHelper.nextInt(1, 3), y, z);
        });
        consumers.put(Blocks.WITHER_SKELETON_SKULL.asItem(), (entity, world, x, y, z) -> EntityUtil.summon(ModEntities.GIGABONE, world, x, y, z));
        consumers.put(ModItems.SOUL_PEOPLE, (entity, world, x, y, z) -> {
            EntityUtil.summon(ModEntities.NAMTAR, world, x, y, z);
            EntityUtil.summon(ModEntities.ZOMBIE_PIGLIN_ART, world, x, y, z - RandomHelper.nextInt(1, 3));
            EntityUtil.summon(ModEntities.MUTATED, world, x, y, z + RandomHelper.nextInt(1, 3));
            EntityUtil.summon(ModEntities.SKELETON_SNOW, world, x + RandomHelper.nextInt(1, 3), y, z);
            EntityUtil.summon(ModEntities.WITHER_SHIELD, world, x - RandomHelper.nextInt(1, 3), y, z);
        });
        consumers.put(ModItems.WITHER_BONE, (entity, world, x, y, z) -> EntityUtil.summon(ModEntities.BIG_UNDEAD_SKELETON, world, x, y, z));
        consumers.put(Items.GOLDEN_SWORD, (entity, world, x, y, z) -> EntityUtil.summon(ModEntities.PIGLIN_COMMANDER, world, x, y, z));
        consumers.put(Items.GOLD_INGOT, (entity, world, x, y, z) -> {
            EntityUtil.summon(ModEntities.ZOMBIE_PIGLIN_KING, world, x, y, z);
            EntityUtil.summon(EntityType.ZOMBIFIED_PIGLIN, world, x, y, z + RandomHelper.nextInt(1, 3));
        });
        consumers.put(Blocks.GOLD_BLOCK.asItem(), (entity, world, x, y, z) -> {
            EntityUtil.summon(ModEntities.PIGLIN_KING_ZOMBIES, world, x, y, z);
            EntityUtil.summon(ModEntities.ZOMBIE_PIGLIN_ART, world, x, y, z - RandomHelper.nextInt(1, 3));
            EntityUtil.summon(EntityType.ZOMBIFIED_PIGLIN, world, x, y, z + RandomHelper.nextInt(1, 3));
        });
        consumers.put(ModItems.BAO_ZHU, (entity, world, x, y, z) -> EntityUtil.summon(ModEntities.NULL_LIKE, world, x, y, z));
        consumers.put(ModItems.WARRIOR_HEART, (entity, world, x, y, z) -> {
            EntityUtil.summon(ModEntities.NAEUS, world, x, y, z);
            EntityUtil.summon(ModEntities.WITHERED_SKELETONS, world, x, y, z - RandomHelper.nextInt(1, 3));
            EntityUtil.summon(ModEntities.HOGSWORTH, world, x, y, z + RandomHelper.nextInt(1, 3));
        });
        consumers.put(ModItems.ICE_HEART, (entity, world, x, y, z) -> {
            EntityUtil.summon(ModEntities.RAIN, world, x, y, z);
            EntityUtil.summon(ModEntities.CIARA, world, x, y, z + RandomHelper.nextInt(1, 3));
            EntityUtil.summon(ModEntities.DARYLL, world, x, y, z - RandomHelper.nextInt(1, 3));
        });
        consumers.put(ModItems.ENDER_HEART, (entity, world, x, y, z) -> {
            EntityUtil.summon(ModEntities.CERIS, world, x, y, z);
            EntityUtil.summon(ModEntities.DARK_SHIELD, world, x, y, x + RandomHelper.nextInt(1, 3));
            EntityUtil.summon(ModEntities.DARK_SHIELD, world, x, y, x - RandomHelper.nextInt(1, 3));
        });
        consumers.put(ModItems.MAGIC_STAR, (entity, world, x, y, z) -> {
            EntityUtil.summon(ModEntities.PATRICK, world, x, y, z);
            EntityUtil.summon(ModEntities.HILDA, world, x, y, x - RandomHelper.nextInt(1, 3));
            EntityUtil.summon(ModEntities.SOLDIERS, world, x, y, x + RandomHelper.nextInt(1, 3));
        });
        consumers.put(ModItems.NETHERITE_WITHER_BONE, (entity, world, x, y, z) -> EntityUtil.summon(ModEntities.BLACKBONE, world, x, y, z));
        consumers.put(ModItems.UNDER_FLOWER, (entity, world, x, y, z) -> EntityUtil.summon(ModEntities.ABIGAIL, world, x, y, z));
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView reader, BlockPos pos) {
        return true;
    }

    @Override
    public int getOpacity(BlockState state, BlockView worldIn, BlockPos pos) {
        return 0;
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.get(FACING)));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        Optional<DarkObsidianBlockEntity> placed = world.getBlockEntity(pos, ModBlockEntities.DARK_OBSIDIAN_BLOCK);
        placed.ifPresent(x -> x.setBiome(itemStack, world.getBiome(pos)));
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        ItemStack dropStack = new ItemStack(ModItems.DARK_OBSIDIAN_BLOCK);
        Optional<DarkObsidianBlockEntity> placed = world.getBlockEntity(pos, ModBlockEntities.DARK_OBSIDIAN_BLOCK);
        placed.ifPresent(x -> {
            RegistryKey<Biome> key = placed.get().getBiome();
            Optional<RegistryKey<Biome>> current = world.getBiome(pos).getKey();
            if (key == null && current.isPresent())
                placed.get().setBiome(current.get());
            if (key != null)
                dropStack.getOrCreateNbt().putString(NBT_KEY, key.getValue().toString());
        });
        dropStack(world, pos, dropStack);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDroppedStacks(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.emptyList();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        NbtCompound compound = stack.getOrCreateNbt();
        String t = compound.getString(NBT_KEY);
        if (t.isBlank())
            t = RainimatorMod.MOD_ID + ":unknown";
        Identifier biome = new Identifier(t);
        tooltip.add(Text.literal(I18n.translate("block.rainimator.dark_obsidian_block.tooltip.biome") + I18n.translate(String.format("biome.%s.%s", biome.getNamespace(), biome.getPath()))));
    }

    @Override
    public ActionResult onUse(BlockState blockstate, World world, BlockPos pos, PlayerEntity entity, Hand hand, BlockHitResult hit) {
        super.onUse(blockstate, world, pos, entity, hand, hit);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (consumers.size() == 0) initConsumers();
        Item used = entity.getMainHandStack().getItem();
        if (consumers.containsKey(used)) {
            if (world instanceof ServerWorld _level) {
                _level.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 200, 0, 8, 0, 0.0002);
                _level.spawnParticles(ParticleTypes.FLAME, ((double) x + 3), y, z, 150, 0, 6, 0, 0.0002);
                _level.spawnParticles(ParticleTypes.FLAME, ((double) x - 3), y, z, 150, 0, 6, 0, 0.0002);
                _level.spawnParticles(ParticleTypes.FLAME, x, y, ((double) z + 3), 150, 0, 6, 0, 0.0002);
                _level.spawnParticles(ParticleTypes.FLAME, x, y, ((double) z - 3), 150, 0, 6, 0, 0.0002);
                _level.spawnParticles(ParticleTypes.FLAME, ((double) x + 2), y, ((double) z + 2), 150, 0, 6, 0, 0.0002);
                _level.spawnParticles(ParticleTypes.FLAME, ((double) x - 2), y, ((double) z - 2), 150, 0, 6, 0, 0.0002);
                _level.spawnParticles(ParticleTypes.FLAME, ((double) x + 2), y, ((double) z - 2), 150, 0, 6, 0, 0.0002);
                _level.spawnParticles(ParticleTypes.FLAME, ((double) x - 2), y, ((double) z + 2), 150, 0, 6, 0, 0.0002);
            }
            SoundUtil.playSound(world, x, y, z, new Identifier("block.portal.travel"), 1, 1);
            if (!world.isClient() && world.getServer() != null)
                world.getServer().getPlayerManager().broadcast(Text.translatable("block.rainimator.dark_obsidian_block.running"), false);
            entity.getMainHandStack().decrement(1);
            entity.getInventory().markDirty();
            if (world instanceof ServerWorld _level)
                Timeout.create(60, () -> {
                    _level.spawnParticles(ParticleTypes.END_ROD, x, y, z, 200, 1, 2, 1, 0.05);
                    consumers.get(used).accept(entity, _level, x, y, z);
                });
        }
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DarkObsidianBlockEntity(pos, state);
    }
}