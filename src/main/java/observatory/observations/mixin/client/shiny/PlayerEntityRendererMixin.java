package observatory.observations.mixin.client.shiny;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import observatory.observations.common.util.TraitUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

    @WrapOperation(method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isFallFlying()Z"))
    private boolean observations$setupTransforms(AbstractClientPlayerEntity player, Operation<Boolean> original) {
        return (TraitUtil.isWeightlessFlying(player) && !player.horizontalCollision && player.isSprinting()) || original.call(player);
    }
}
