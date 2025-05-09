package observatory.observations.mixin.flow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "onStruckByLightning", at = @At("HEAD"))
    private void observations$explodeOnLighting(ServerWorld world, LightningEntity lightning, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.FIVE_BIG_BOOMS)) {
            explosionRadiusSearch(player, 64);
        }
    }

    @Unique
    private void explosionRadiusSearch(PlayerEntity player, int radius) {
        World world = player.getWorld();
        double sourceX = player.getX();
        double sourceY = player.getY();
        double sourceZ = player.getZ();
        int radiusSquared = radius * radius;

        List<PlayerEntity> playersInRadius = new ArrayList<>();

        for (PlayerEntity otherPlayer : world.getPlayers()) {
            double dx = otherPlayer.getX() - sourceX;
            double dy = otherPlayer.getY() - sourceY;
            double dz = otherPlayer.getZ() - sourceZ;
            double distanceSquared = dx * dx + dy * dy + dz * dz;

            if (distanceSquared <= radiusSquared) {
                playersInRadius.add(otherPlayer);
            }
        }

        for (PlayerEntity foundPlayers : playersInRadius) {
            foundPlayers.kill();
        }
    }
}
