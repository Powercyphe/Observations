package observatory.observations.mixin.wazzo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpiderEntity.AttackGoal.class)
public class LivingEntityMixin extends MeleeAttackGoal {
    public LivingEntityMixin(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
    }

    @ModifyReturnValue(method = "canStart", at = @At("RETURN"))
    private boolean observations$bugLikeAppearance(boolean original) {
        LivingEntity entity = this.mob.getTarget();
        if (entity instanceof PlayerEntity player && this.mob instanceof SpiderEntity spider) {
            System.out.println((spider.getLastAttacker() != player) + " " + !TraitComponent.get(player).hasTrait(Trait.BUG_LIKE_APPEARANCE));
            return !(spider.getLastAttacker() != player && !TraitComponent.get(player).hasTrait(Trait.BUG_LIKE_APPEARANCE)) && !original;
        }
        return original;
    }
}
