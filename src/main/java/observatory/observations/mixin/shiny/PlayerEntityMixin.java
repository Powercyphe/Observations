package observatory.observations.mixin.shiny;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import observatory.observations.common.component.LikeVoidComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "damage", at = @At(value = "TAIL"))
    private void observations$stunOnDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (amount >= 8) {
            LikeVoidComponent.get(player).setStunned();
        }
    }

    @ModifyReturnValue(method = "isImmobile", at = @At(value = "RETURN"))
    private boolean observations$isImmobile(boolean original) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        return original || LikeVoidComponent.get(player).isStunned();
    }

    @WrapWithCondition(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;travel(Lnet/minecraft/util/math/Vec3d;)V"))
    private boolean observations$preventMovement(LivingEntity instance, Vec3d movementInput) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (LikeVoidComponent.get(player).isStunned()) {
            player.updateLimbs(player instanceof Flutterer);
            return false;
        }
        return true;
    }
}
