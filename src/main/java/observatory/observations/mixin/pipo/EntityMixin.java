package observatory.observations.mixin.pipo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    
    @ModifyReturnValue(method = "isInvulnerableTo", at = @At("RETURN"))
    private boolean observations$removeLavaDamage(boolean original, DamageSource source) {
        Entity entity = (Entity) (Object) this;
        if (source.isIn(DamageTypeTags.IS_FIRE) && entity instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.MAGMA_COVERED)) {
            return true;
        }
        return original;
    }
}
