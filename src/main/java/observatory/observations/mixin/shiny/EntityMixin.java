package observatory.observations.mixin.shiny;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @ModifyReturnValue(method = "hasNoGravity", at = @At(value = "RETURN"))
    private boolean observations$shouldHaveNoGravity(boolean original) {
        Entity entity = (Entity) (Object) this;

        if (entity instanceof PlayerEntity player) {
            return original || TraitComponent.get(player).hasTrait(Trait.WEIGHTLESS);
        }
        else return original;
    }
}
