package observatory.observations.mixin.nopeable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {

    //Trait: Divine Intervention
    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;heal(F)V"))
    private void observations$increaseRegen(PlayerEntity player, float amount, Operation<Void> original) {
        if (TraitComponent.get(player).hasTrait(Trait.DIVINE_INTERVENTION)) {
            amount *= 1.15f;
        }
        original.call(player, amount);
    }
}
