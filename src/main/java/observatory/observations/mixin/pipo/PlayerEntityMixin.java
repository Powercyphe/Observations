package observatory.observations.mixin.pipo;

import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.Vec3d;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(
            method = "travel",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void onTravel(Vec3d movementInput, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player.isSprinting() && player.isSubmergedIn(FluidTags.LAVA) && TraitComponent.get(player).hasTrait(Trait.MAGMA_COVERED)) {
            Vec3d lookVec = player.getRotationVector().normalize();

            float forwardBoost = 0.5f;
            float upwardBoost = 0.3f;

            Vec3d newVelocity = new Vec3d(
                    lookVec.x * forwardBoost,
                    lookVec.y * upwardBoost,
                    lookVec.z * forwardBoost
            );

            if (movementInput.lengthSquared() > 1.0E-7) {
                newVelocity = newVelocity.add(
                        movementInput.x * 0.2,
                        0,
                        movementInput.z * 0.2
                ).normalize().multiply(forwardBoost);
            }

            player.setVelocity(newVelocity);
            player.move(MovementType.SELF, player.getVelocity());

            player.setVelocity(player.getVelocity().multiply(0.8));

            ci.cancel();
        }
    }
}
