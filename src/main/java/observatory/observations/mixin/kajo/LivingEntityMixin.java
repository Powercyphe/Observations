package observatory.observations.mixin.kajo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyReturnValue(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At(value = "RETURN"))
    private boolean observations$preventBeingTargeted(boolean original, LivingEntity target) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof HostileEntity && target instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.SOULLESS_CREATURE)) {
            return false;
        }
        return original;
    }
}
