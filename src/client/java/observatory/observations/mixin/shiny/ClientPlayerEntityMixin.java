package observatory.observations.mixin.shiny;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import observatory.observations.component.TraitComponent;
import observatory.observations.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void observations$multiplySprintSpeed(CallbackInfo ci) {
        if (TraitComponent.get(this).hasTrait(Trait.WEIGHTLESS)) {
            this.forwardSpeed *= 0.3f;
        }
    }
}
