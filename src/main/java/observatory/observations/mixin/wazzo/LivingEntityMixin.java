package observatory.observations.mixin.wazzo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyReturnValue(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("RETURN"))
    private boolean observations$bugLikeAppearance(boolean original, LivingEntity target) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof HostileEntity && target instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.BUG_LIKE_APPEARANCE)) {
            return false;
        }
        return original;
    }
}
