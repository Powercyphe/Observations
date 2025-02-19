package observatory.observations.mixin.vann1e_;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import observatory.observations.component.TraitComponent;
import observatory.observations.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void observations$no_longer_flesh(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity player) {
            if (TraitComponent.get(player).hasTrait(Trait.NO_LONGER_FLESH) && player.isTouchingWater() && player.isOnGround() && player.jumping && player.jumpingCooldown == 0) {
                player.jump();
                player.jumpingCooldown = 10;
            }
        }
    }


}
