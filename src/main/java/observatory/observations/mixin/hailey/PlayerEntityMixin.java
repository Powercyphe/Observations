package observatory.observations.mixin.hailey;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Unique
    private float observations$attackCooldown = 0;

    @ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F", ordinal = 0))
    private float observations$weeeee(float original) {
        this.observations$attackCooldown = original;
        return original;
    }

    @ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean observations$weeeee(boolean original, Entity target) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (TraitComponent.get(player).hasTrait(Trait.WEEEE) && player.getMainHandStack().isEmpty() && original) {
            target.addVelocity(0, this.observations$attackCooldown / 1.35, 0);
        }
        return original;
    }

}
