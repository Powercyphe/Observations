package observatory.observations.mixin.shiny;

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
import observatory.observations.common.util.TraitUtil;
import observatory.observations.mixin.accessor.EntityAccessor;
import observatory.observations.mixin.accessor.LivingEntityAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract float getMovementSpeed();
    @Shadow public abstract void updateLimbs(boolean flutter);
    @Shadow @Final public LimbAnimator limbAnimator;

    @Unique private boolean wasSprinting = false;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    //Trait: Weightless
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void observations$updateFlyingDimensions(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.WEIGHTLESS)) {
            if (this.isSprinting() && !this.wasSprinting) {
                this.calculateDimensions();
                this.wasSprinting = true;
            }
            else if (!this.isSprinting() && this.wasSprinting) {
                this.calculateDimensions();
                this.wasSprinting = false;
            }
        }
    }

    //Trait: Weightless
    @WrapOperation(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;travel(Lnet/minecraft/util/math/Vec3d;)V"))
    private void observations$crosshairBasedMovement(LivingEntity entity, Vec3d movementInput, Operation<Void> original) {

        float speed = this.getMovementSpeed() * (this.isSprinting() ? 1.0f : 0.35f);
        Vec3d movement = observations$movementInputToVelocity(movementInput, speed, this.getPitch(), this.getYaw());
        Vec3d velocity = this.getVelocity().add(movement).multiply(0.85, 0.85, 0.85);

        Vec3d vec3d = ((EntityAccessor) this).observations$adjustMovementForCollisions(velocity);
        this.verticalCollision = velocity.y != vec3d.y;

        if (entity instanceof PlayerEntity player && TraitUtil.isWeightlessFlying(player)) {
            if (this.isLogicalSideForUpdatingMovement()) {
                this.setVelocity(velocity);
                this.move(MovementType.SELF, this.getVelocity());
            }
            if (this.isSprinting()) {
                float f = (float) MathHelper.magnitude(this.getX() - this.prevX, 0.0, this.getZ() - this.prevZ);
                f = Math.min(f * 4.0f, 1.0f);
                this.limbAnimator.updateLimbs(f * 0.2f, 0.05f);
            }
            else this.updateLimbs(false);
        }
        else {
            original.call(entity, movementInput);
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isFallFlying()Z"))
    private boolean observations$updateRoll(LivingEntity entity, Operation<Boolean> original) {
        return (entity instanceof PlayerEntity player && TraitUtil.isWeightlessFlying(player) && !player.horizontalCollision && player.isSprinting()) || original.call(entity);
    }

    //Trait: Weightless
    //This doesn't work outside of singleplayer for some reason
    @WrapOperation(method = "takeKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(D)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d observations$receiveIncreasedKnockback(Vec3d vector, double value, Operation<Vec3d> original) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.WEIGHTLESS)) {
            value *= 5.0f;
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
                    (vec3d.z > 0 ? h : -h) * speed * (movementInput.z != 0 ? 1.1 : 0),
                    vec3d.z * g + vec3d.x * f
            );
        }
    }
}
