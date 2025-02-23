package observatory.observations.mixin.shiny;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import observatory.observations.common.util.TraitUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @WrapWithCondition(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;fall(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V"))
    private boolean observations$preventFall(Entity entity, double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
        if (entity instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.WEIGHTLESS)) {
            return false;
        }
        else return true;
    }

    @ModifyReturnValue(method = "getStandingEyeHeight", at = @At(value = "RETURN"))
    private float observations$weightlessFlyingEyeHeight(float original) {
        Entity entity = (Entity) (Object) this;

        if (entity instanceof PlayerEntity player && TraitUtil.isWeightlessFlying(player) && !player.horizontalCollision && player.isSprinting()) {
            original = 0.5f;
        }
        return original;
    }
}
