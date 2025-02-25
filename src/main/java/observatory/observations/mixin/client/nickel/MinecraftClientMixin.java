package observatory.observations.mixin.client.nickel;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;

    //Trait: Blood Scent
    @ModifyReturnValue(method = "hasOutline", at = @At(value = "RETURN"))
    private boolean observations$glowInWater(boolean original, Entity entity) {
        return original || (this.player != null && TraitComponent.get(player).hasTrait(Trait.BLOOD_SCENT) && entity.isTouchingWaterOrRain());
    }
}
