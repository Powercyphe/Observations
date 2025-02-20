package observatory.observations.mixin.client.vann1e_;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import observatory.observations.client.render.feature.BuddingFeatureRenderer;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void applyFeatures(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        this.addFeature(new BuddingFeatureRenderer<>(this, ctx.getItemRenderer(), ctx.getBlockRenderManager()));
    }

    @Inject(method = "setModelPose", at = @At("TAIL"))
    private void onRenderLeftArm(AbstractClientPlayerEntity player, CallbackInfo ci, @Local PlayerEntityModel<?> playerEntityModel) {
        if (TraitComponent.get(player).hasTrait(Trait.DISARMED)) {
            playerEntityModel.leftArm.visible = false;
            playerEntityModel.leftSleeve.visible = false;
        }
    }
}
