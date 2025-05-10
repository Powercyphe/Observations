package observatory.observations.mixin.pom;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class JumpMixin {
    @ModifyReturnValue(method = "getJumpVelocityMultiplier", at = @At("RETURN"))
    private float increacedJumpHeight(float original) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.ACROBATICS)) {
            return original * 1.5f;
        }
        return original;
    }
}