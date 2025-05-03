package observatory.observations.mixin.client.winter;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import observatory.observations.common.util.TraitUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @ModifyReturnValue(method = "canStartSprinting", at = @At("RETURN"))
    private boolean observations$no_longer_flesh(boolean original) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        return original && !(TraitComponent.get(player).hasTrait(Trait.PRIVILEGED_PLATES) && player.isTouchingWater() && TraitUtil.isWearingItem(player, Items.GOLDEN_CHESTPLATE));
    }
}