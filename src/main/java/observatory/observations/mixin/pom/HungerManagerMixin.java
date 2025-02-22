package observatory.observations.mixin.pom;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {

    @Shadow private int foodTickTimer;

    @ModifyExpressionValue(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private Difficulty observations$noEnzymes(Difficulty original, PlayerEntity player) {
        if (TraitComponent.get(player).hasTrait(Trait.NO_ENZYMES)) {
            return Difficulty.PEACEFUL;
        }
        return original;
    }

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z", shift = At.Shift.AFTER))
    private void observations$noEnzymes(PlayerEntity player, CallbackInfo ci) {
        if (TraitComponent.get(player).hasTrait(Trait.NO_ENZYMES)) {
            ++this.foodTickTimer;
        }
    }
}
