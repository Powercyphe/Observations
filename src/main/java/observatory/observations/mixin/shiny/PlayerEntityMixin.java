package observatory.observations.mixin.shiny;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import observatory.observations.Observations;
import observatory.observations.common.component.LikeVoidComponent;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
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

        if (amount >= 8 && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY) && player.isAlive() && TraitComponent.get(player).hasTrait(Trait.LIKE_VOID)) {
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

    @Inject(method = "handleFallDamage", at = @At(value = "HEAD"), cancellable = true)
    private void observations$preventFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (TraitComponent.get(player).hasTrait(Trait.WEIGHTLESS)) {
            cir.setReturnValue(false);
        }
    }

    @ModifyReturnValue(method = "getDimensions", at = @At(value = "RETURN"))
    private EntityDimensions observations$weightlessFlyingDimensions(EntityDimensions original) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (Observations.isWeightlessFlying(player) && player.isSprinting()) {
            original = EntityDimensions.changing(0.8f, 0.8f);
        }
        return original;
    }

    @WrapOperation(method = "updatePose", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setPose(Lnet/minecraft/entity/EntityPose;)V"))
    private void observations$setWeightlessFlyingDimensions(PlayerEntity player, EntityPose entityPose, Operation<Void> original) {
        original.call(player, entityPose);

        if (Observations.isWeightlessFlying(player) && player.isSprinting()) {
            player.calculateDimensions();
        }
    }
}
