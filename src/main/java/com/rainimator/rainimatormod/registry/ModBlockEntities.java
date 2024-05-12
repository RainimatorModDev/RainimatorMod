package com.rainimator.rainimatormod.registry;

import com.iafenvoy.annotationlib.annotation.ModId;
import com.iafenvoy.annotationlib.annotation.registration.RegisterAll;
import com.iafenvoy.annotationlib.api.IAnnotatedRegistryEntry;
import com.rainimator.rainimatormod.RainimatorMod;
import com.rainimator.rainimatormod.item.block.entity.DarkObsidianBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

@ModId(RainimatorMod.MOD_ID)
@RegisterAll
public class ModBlockEntities implements IAnnotatedRegistryEntry {
    public static final BlockEntityType<DarkObsidianBlockEntity> DARK_OBSIDIAN_BLOCK = FabricBlockEntityTypeBuilder.create(DarkObsidianBlockEntity::new, ModBlocks.DARK_OBSIDIAN_BLOCK).build();
}
