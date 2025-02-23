package observatory.observations.mixin.client.shiny;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.ElytraSoundInstance;
import observatory.observations.common.util.TraitUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ElytraSoundInstance.class)
public abstract class ElytraSoundInstanceMixin {

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isFallFlying()Z"))
    private boolean observations$playWeightlessFlyingSound(ClientPlayerEntity player, Operation<Boolean> original) {
        return (TraitUtil.isWeightlessFlying(player) && player.isSprinting()) || original.call(player);
    }
}
