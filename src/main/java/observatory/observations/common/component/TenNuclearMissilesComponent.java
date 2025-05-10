package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TenNuclearMissilesComponent implements AutoSyncedComponent, CommonTickingComponent {
    private boolean isDiffused = false;
    private int ticks = 0;
    public PlayerEntity player;

    public TenNuclearMissilesComponent(PlayerEntity player) {
        this.player = player;
    }

    public static TenNuclearMissilesComponent get(@NotNull PlayerEntity player) {
        return ModComponents.TEN_NUCLEAR_MISSILES.get(player);
    }

    public void sync() {
        ModComponents.TEN_NUCLEAR_MISSILES.sync(player);
    }

    @Override
    public void tick() {
        if (TraitComponent.get(player).hasTrait(Trait.FIVE_BIG_BOOMS)) {
            boolean isBelowHalfHealth = player.getMaxHealth() / 2 > player.getHealth();
            if (isBelowHalfHealth && !player.isWet() && !isDiffused) {
                if (player.isOnFire()) {
                    if (ticks > 60) {
                        explosionRadiusSearch(player, 32);
                        ticks = 0;
                    } else {
                        ticks++;
                    }
                } else {
                    if (ticks > 100) {
                        explosionRadiusSearch(player, 32);
                        ticks = 0;
                    } else {
                        ticks++;
                    }
                }
            } else {
                if (isDiffused && !isBelowHalfHealth) {
                    isDiffused = false;
                }
            } if (player.isWet()) {
                ticks = 0;
            }
        }
    }

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

    @Override
    public void readFromNbt(NbtCompound compound) {
        this.ticks = compound.getInt("ticks");
        this.isDiffused = compound.getBoolean("isDiffused");
    }

    @Override
    public void writeToNbt(NbtCompound compound) {
        compound.putInt("ticks", this.ticks);
        compound.putBoolean("isDiffused", this.isDiffused);
    }
}
