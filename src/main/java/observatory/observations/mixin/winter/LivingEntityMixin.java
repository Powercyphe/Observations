package observatory.observations.mixin.winter;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "getNextAirUnderwater", at = @At("HEAD"), cancellable = true)
    protected void getNextAirUnderwater(int air, CallbackInfoReturnable<Integer> cir) {
        LivingEntity living = (LivingEntity) (Object) this;
        int i = EnchantmentHelper.getRespiration(living);
        if (living.getEyeY() >= 200 && living instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.SMOKED_LUNGS)) {
            cir.setReturnValue(i > 0 && living.getRandom().nextInt(i + 1) > 0 ? air : air - 2);
        }
    }
}
