package observatory.observations.mixin.everest;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    private void observations$handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;

        if (livingEntity instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.GRAVITY_CORE)) {
            cir.setReturnValue(false);
        }
    }
}
