package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.awt.*;
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
        // why the fuck is this not ticking???????
        if (TraitComponent.get(player).hasTrait(Trait.FIVE_BIG_BOOMS)) {
            boolean isBelowHalfHealth = player.getMaxHealth() / 2 > player.getHealth();
            System.out.println(isBelowHalfHealth);
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
                System.out.println(ticks);
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

        Vec3d origin = player.getPos();
        Color spiralColor = new Color(201, 108, 35); // Purple

        int spiralPoints = 40;
        for (int i = 0; i < spiralPoints; i++) {
            float progress = i / (float) spiralPoints;
            double angle = 2 * Math.PI * i * 2; // Double helix

            WorldParticleBuilder.create(LodestoneParticleRegistry.TWINKLE_PARTICLE)
                    .setScaleData(GenericParticleData.create(0.3f, 0).build())
                    .setTransparencyData(GenericParticleData.create(1.0f, 0.5f).build())
                    .setColorData(ColorParticleData.create(spiralColor, spiralColor.brighter())
                            .setEasing(Easing.SINE_IN_OUT).build())
                    .setSpinData(SpinParticleData.create(0.5f, 1.0f).build())
                    .setLifetime(35)
                    .addMotion(
                            0.02f * Math.cos(angle),
                            0.15f + 0.05f * progress,
                            0.02f * Math.sin(angle)
                    )
                    .enableNoClip()
                    .spawn(world,
                            origin.x + 0.5 * Math.cos(angle),
                            origin.y + 0.2,
                            origin.z + 0.5 * Math.sin(angle)
                    );
        }

        int landPoints = 32;
        for (int i = 0; i < landPoints; i++) {
            double angle = 2 * Math.PI * i / landPoints;
            double x = origin.x + radius * Math.cos(angle);
            double z = origin.z + radius * Math.sin(angle);

            WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
                    .setScaleData(GenericParticleData.create(0.5f, 1.0f).build())
                    .setTransparencyData(GenericParticleData.create(0.8f, 0.2f).build())
                    .setColorData(ColorParticleData.create(new Color(150, 150, 150), Color.DARK_GRAY).build())
                    .setLifetime(30)
                    .addMotion(
                            (world.random.nextFloat() - 0.5f) * 0.02f,
                            -0.08f,
                            (world.random.nextFloat() - 0.5f) * 0.02f
                    )
                    .enableNoClip()
                    .spawn(world, x, origin.y + 4, z);
        }

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
