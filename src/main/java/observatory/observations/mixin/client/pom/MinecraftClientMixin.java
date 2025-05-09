package observatory.observations.mixin.client.pom;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;

    @ModifyReturnValue(method = "hasOutline", at = @At("RETURN"))
    private boolean observations$lineOfSight(boolean original, Entity entity) {
        MinecraftClient minecraftClient = (MinecraftClient) (Object) this;
        if (minecraftClient.player != null) {
            ClientPlayerEntity player = minecraftClient.player;
            if (entity instanceof PlayerEntity otherPlayer && TraitComponent.get(player).hasTrait(Trait.LINE_OF_SIGHT)) {
                List<PlayerEntity> players = player.getWorld().getEntitiesByClass(PlayerEntity.class, Box.of(player.getEyePos(), 1, 1, 1).stretch(player.getRotationVector().multiply(40)), EntityPredicates.EXCEPT_SPECTATOR);
                if (players.contains(otherPlayer) && player != otherPlayer) {
                    Vec3d start = player.getPos();
                    Vec3d end = otherPlayer.getPos();

                    BlockHitResult hit = player.getWorld().raycast(new RaycastContext(
                            start,
                            end,
                            RaycastContext.ShapeType.COLLIDER,
                            RaycastContext.FluidHandling.NONE,
                            player
                    ));

                    boolean blockBetween = hit.getType() == HitResult.Type.BLOCK &&
                            hit.getPos().squaredDistanceTo(start) < end.squaredDistanceTo(start);

                    if (!blockBetween) {
                        return true;
                    }
                }
            }
        }
        return original ;
    }
}
