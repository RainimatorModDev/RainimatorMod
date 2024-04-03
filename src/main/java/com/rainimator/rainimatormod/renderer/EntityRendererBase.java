package com.rainimator.rainimatormod.renderer;

import com.rainimator.rainimatormod.RainimatorMod;
import com.rainimator.rainimatormod.util.Stage;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

public class EntityRendererBase<T extends MobEntity> extends BipedEntityRenderer<T, BipedEntityModel<T>> {
    private final Stage.StagedEntityTextureProvider textureId;

    public EntityRendererBase(EntityRendererFactory.Context context, Stage.StagedEntityTextureProvider textureId, String eyeTextureId) {
        super(context, new BipedEntityModel<>(context.getPart(EntityModelLayers.PLAYER)), 0.5F);
        this.textureId = textureId;
        this.addFeature(new ArmorFeatureRenderer<>(this, new BipedEntityModel<>(context.getPart(EntityModelLayers.PLAYER_INNER_ARMOR)), new BipedEntityModel<>(context.getPart(EntityModelLayers.PLAYER_OUTER_ARMOR))));
        if (eyeTextureId != null)
            this.addFeature(new EyesFeatureRenderer<>(this) {
                public RenderLayer getEyesTexture() {
                    return RenderLayer.getEyes(new Identifier(RainimatorMod.MOD_ID, "textures/entities/" + eyeTextureId + ".png"));
                }
            });
    }

    @Override
    public Identifier getTexture(T entity) {
        if (entity instanceof Stage.StagedEntity stagedEntity)
            return this.textureId.getTexture(stagedEntity.getStage());
        return this.textureId.getTexture();
    }
}