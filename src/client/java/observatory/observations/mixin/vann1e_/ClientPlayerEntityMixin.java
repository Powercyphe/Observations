package observatory.observations.mixin.vann1e_;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.MathHelper;
import observatory.observations.component.TraitComponent;
import observatory.observations.component.WaterSkippingComponent;
import observatory.observations.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @ModifyReturnValue(method = "canStartSprinting", at = @At("RETURN"))
    private boolean observations$no_longer_flesh(boolean original) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        return original && !(TraitComponent.get(player).hasTrait(Trait.NO_LONGER_FLESH) && player.isTouchingWater());
    }

    @Override
    public boolean shouldSwimInFluids() {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if (TraitComponent.get(player).hasTrait(Trait.NO_LONGER_FLESH) && this.isTouchingWater()) {
            return false;
        }
        return super.shouldSwimInFluids();
    }

    @Override
    public void swimUpward(TagKey<Fluid> fluid) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if (TraitComponent.get(player).hasTrait(Trait.NO_LONGER_FLESH)) {
            return;
        }
        super.swimUpward(fluid);
    }

    @Override
    public boolean canWalkOnFluid(FluidState state) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        boolean bl = TraitComponent.get(player).hasTrait(Trait.NO_LONGER_FLESH) && WaterSkippingComponent.get(player).remainingSkippingTicks > 0;
        return super.canWalkOnFluid(state) || bl;
    }
}
