package observatory.observations.mixin.shiny;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import observatory.observations.common.component.LikeVoidComponent;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import observatory.observations.common.util.TraitUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow public abstract void increaseStat(Stat<?> stat, int amount);

    @Shadow public abstract void addExhaustion(float exhaustion);

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

    @Inject(method = "increaseTravelMotionStats", at = @At(value = "HEAD"))
    private void observations$applyExhaustionWhenFlying(double dx, double dy, double dz, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (!this.hasVehicle() && TraitUtil.isWeightlessFlying(player)) {

            int i = Math.round((float) Math.sqrt(dx * dx + dz * dz) * 100.0f);
            if (i > 0) {
                if (this.isSprinting()) {
                    this.addExhaustion(0.1f * (float) i * 0.007f);
                }
                else {
                    this.addExhaustion(0.0f * (float) i * 0.01f);
                }
            }
        }
    }

    @Inject(method = "handleFallDamage", at = @At(value = "HEAD"), cancellable = true)
    private void observations$preventFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (TraitComponent.get(player).hasTrait(Trait.WEIGHTLESS)) {
            cir.setReturnValue(false);
        }
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (TraitUtil.isWeightlessFlying(player) && !player.horizontalCollision && player.isSprinting()) {
            return EntityDimensions.changing(0.7f, 0.8f);
        }
        else return super.getDimensions(pose);
    }
}
