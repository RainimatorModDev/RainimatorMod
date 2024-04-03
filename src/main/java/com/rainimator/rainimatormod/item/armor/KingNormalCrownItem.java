package com.rainimator.rainimatormod.item.armor;

import com.rainimator.rainimatormod.RainimatorMod;
import com.rainimator.rainimatormod.registry.util.ItemBase;
import com.rainimator.rainimatormod.registry.util.ModCreativeTab;
import com.rainimator.rainimatormod.renderer.model.ModelKingNormalCrown;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.Map;

public class KingNormalCrownItem extends ItemBase {
    public KingNormalCrownItem(Item.Settings properties) {
        super(p -> properties);
    }

    public static class Helmet extends KingNormalCrownItem implements ArmorRenderer {
        public Helmet() {
            super(ModCreativeTab.createProperty());
            ArmorRenderer.register(this, this);
        }

        private BipedEntityModel<LivingEntity> getArmorModel(LivingEntity living) {
            BipedEntityModel<LivingEntity> armorModel = new BipedEntityModel<>(new ModelPart(Collections.emptyList(), Map.of("head", (new ModelKingNormalCrown<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(ModelKingNormalCrown.LAYER_LOCATION))).Head, "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body", new ModelPart(
                    Collections.emptyList(), Collections.emptyMap()), "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_arm", new ModelPart(
                    Collections.emptyList(), Collections.emptyMap()), "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_leg", new ModelPart(
                    Collections.emptyList(), Collections.emptyMap()))));
            armorModel.sneaking = living.isSneaking();
            armorModel.riding = living.hasVehicle();
            armorModel.child = living.isBaby();
            return armorModel;
        }

        private Identifier getTexture() {
            return new Identifier(RainimatorMod.MOD_ID, "textures/models/armor/king_normal_crown.png");
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
            ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, this.getArmorModel(entity), this.getTexture());
        }
    }
}
