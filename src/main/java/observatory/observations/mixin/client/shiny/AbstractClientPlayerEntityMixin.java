package observatory.observations.mixin.client.shiny;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {
    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "getSkinTexture", at = @At("HEAD"), cancellable = true)
    private void getEliminatedSkinTexture(CallbackInfoReturnable<Identifier> cir) {
        if(this.getUuidAsString().equals("a9bcfe9b-bb80-463d-848e-11e0b03f2b6e")){
            cir.setReturnValue(new Identifier("observations", "textures/entity/1usterless.png"));
        }
    }
}
