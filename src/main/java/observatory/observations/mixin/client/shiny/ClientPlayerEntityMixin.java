package observatory.observations.mixin.client.shiny;


import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.ElytraSoundInstance;
import net.minecraft.client.world.ClientWorld;
import observatory.observations.Observations;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    @Shadow @Final protected MinecraftClient client;
    @Unique private boolean startedSprinting = false;
    @Unique private boolean hasPlayedSound = false;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V"))
    private void observations$sprintStatus(CallbackInfo ci) {
        if (TraitComponent.get(this).hasTrait(Trait.INFINITE_FREEDOM) && this.isSprinting() && !this.client.options.sprintKey.isPressed()) {
            this.setSprinting(false);
        }

        if (!this.hasPlayedSound && TraitComponent.get(this).hasTrait(Trait.INFINITE_FREEDOM) && (!this.isSprinting() || this.isOnGround()) && !this.horizontalCollision && this.client.options.sprintKey.isPressed()) {
            this.startedSprinting = true;
        }
        this.hasPlayedSound = false;
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tickMovement()V", shift = At.Shift.AFTER))
    private void observations$playWeightlessFlyingSound(CallbackInfo ci) {
        if (this.startedSprinting) {
            if (Observations.isWeightlessFlying(this) && this.isSprinting()) {
                ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
                this.client.getSoundManager().play(new ElytraSoundInstance(player));
                this.startedSprinting = false;
                this.hasPlayedSound = true;
            }
        }
    }

    @Override
    public void setSprinting(boolean sprinting) {
        if (sprinting && Observations.isWeightlessFlying(this) && this.horizontalCollision) sprinting = false;
        super.setSprinting(sprinting);
    }
}
