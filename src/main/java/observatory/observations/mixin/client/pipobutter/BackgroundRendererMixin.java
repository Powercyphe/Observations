package observatory.observations.mixin.client.pipobutter;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

    @Unique
    private static final float FOG_START = -8.0F;
    @Unique
    private static final float FOG_END = 140.0F;


    @Inject(method = "applyFog", at = @At("RETURN"))
    private static void applyFogModifyDistance(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo info) {
        Entity entity = camera.getFocusedEntity();

        if (entity instanceof PlayerEntity player) {
            if (TraitComponent.get(player).hasTrait(Trait.MAGMA_COVERED) && entity.isInLava() ) {
                RenderSystem.setShaderFogStart(FOG_START);
                RenderSystem.setShaderFogEnd(FOG_END);
                RenderSystem.setShaderFogShape(FogShape.CYLINDER);
            }
        }
    }
}
