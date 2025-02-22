package observatory.observations.mixin.shiny;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import observatory.observations.Observations;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract float getMovementSpeed();
    @Shadow public abstract void updateLimbs(boolean flutter);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @WrapOperation(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;travel(Lnet/minecraft/util/math/Vec3d;)V"))
    private void observations$crosshairBasedMovement(LivingEntity entity, Vec3d movementInput, Operation<Void> original) {

        if (entity instanceof PlayerEntity player && Observations.isWeightlessFlying(player)) {
            if (this.isLogicalSideForUpdatingMovement()) {
                float speed = this.getMovementSpeed() * (this.isSprinting() ? 1.0f : 0.35f);
                Vec3d velocity = observations$movementInputToVelocity(movementInput, speed, this.getPitch(), this.getYaw());

                this.setVelocity(this.getVelocity().add(velocity).multiply(0.85, 0.85, 0.85));
                this.move(MovementType.SELF, this.getVelocity());
            }
            this.updateLimbs(this instanceof Flutterer);
        }
        else {
            original.call(entity, movementInput);
        }
    }

    @ModifyReturnValue(method = "isFallFlying", at = @At(value = "RETURN"))
    private boolean observations$weightlessFallFlying(boolean original) {
        LivingEntity entity = (LivingEntity) (Object) this;
        return original || (entity instanceof PlayerEntity player && (Observations.isWeightlessFlying(player) && player.isSprinting()));
    }

    @WrapOperation(method = "takeKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(D)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d observations$receiveIncreasedKnockback(Vec3d vector, double value, Operation<Vec3d> original) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.WEIGHTLESS)) {
            value *= 2.0f;
        }
        return original.call(vector, value);
    }

    @Unique
    private static Vec3d observations$movementInputToVelocity(Vec3d movementInput, float speed, float pitch, float yaw) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7) {
            return Vec3d.ZERO;
        }
        else {
            Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply(speed);
            float f = MathHelper.sin(yaw * (float) (Math.PI / 180.0));
            float g = MathHelper.cos(yaw * (float) (Math.PI / 180.0));
            float h = -MathHelper.sin(pitch * (float) (Math.PI / 180.0));

            return new Vec3d(
                    vec3d.x * g - vec3d.z * f,
                    (vec3d.z > 0 ? h : -h) * speed * 1.1,
                    vec3d.z * g + vec3d.x * f
            );
        }
    }
}
