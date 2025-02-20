package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.ModNetworking;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.NotNull;

public class WaterSkippingComponent implements CommonTickingComponent, AutoSyncedComponent {
    private static final String WAS_GROUNDED = "WasGrounded";
    private static final String COOLDOWN = "Cooldown";
    private static final String WATER_SKIPPING_TICKS = "WaterSkippingTicks";

    private static final double MAX_WATER_HEIGHT = 0.6;

    private boolean wasGrounded = false;
    private boolean canWaterSkip = false;

    private int cooldown = 0;
    private int waterSkippingTicks = 0;

    private int remainingPressTicks = 0;

    private final PlayerEntity player;

    public WaterSkippingComponent(PlayerEntity player) {
        this.player = player;
    }

    public static WaterSkippingComponent get(PlayerEntity player) {
        return ModComponents.WATER_SKIPPING.get(player);
    }

    public void sync() {
        ModComponents.WATER_SKIPPING.sync(player);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound nbt) {
        this.wasGrounded = nbt.getBoolean(WAS_GROUNDED);
        this.cooldown = nbt.getInt(COOLDOWN);
        this.waterSkippingTicks = nbt.getInt(WATER_SKIPPING_TICKS);
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound nbt) {
        nbt.putBoolean(WAS_GROUNDED, this.wasGrounded);
        nbt.putInt(COOLDOWN, this.cooldown);
        nbt.putInt(WATER_SKIPPING_TICKS, this.waterSkippingTicks);
    }

    @Override
    public void tick() {
        if (TraitComponent.get(player).hasTrait(Trait.NO_LONGER_FLESH)) {
            if (this.cooldown > 0) {
                this.cooldown--;
                this.canWaterSkip = false;
            } else {
                if (!this.wasGrounded) {
                    this.wasGrounded = player.isOnGround() && player.getFluidHeight(FluidTags.WATER) <= MAX_WATER_HEIGHT;
                } else if (player.isSubmergedInWater()) {
                    this.wasGrounded = false;
                }
                this.canWaterSkip = (this.wasGrounded || this.waterSkippingTicks > 0) && player.isTouchingWater() && player.getFluidHeight(FluidTags.WATER) <= MAX_WATER_HEIGHT;
            }
            if (this.waterSkippingTicks > 0) {
                this.waterSkippingTicks--;

            }
        }
    }

    @Override
    public void clientTick() {
        tick();
        if (player == MinecraftClient.getInstance().player && TraitComponent.get(player).hasTrait(Trait.NO_LONGER_FLESH)) {
            GameOptions options = MinecraftClient.getInstance().options;
            boolean pressingKey = options.jumpKey.isPressed();
            if (remainingPressTicks > 0) {
                remainingPressTicks--;
            }
            if (pressingKey && remainingPressTicks != 0) {
                remainingPressTicks--;
                if (this.canWaterSkip) {
                    remainingPressTicks = 0;

                    Vec3d inputVelocity = new Vec3d(player.getVelocity().getX() * 2.14, player.getJumpVelocity(), player.getVelocity().getZ() * 2.14);
                    skipOnWater(inputVelocity);

                    PacketByteBuf packet = PacketByteBufs.create();
                    packet.writeVector3f(inputVelocity.toVector3f());
                    ClientPlayNetworking.send(ModNetworking.WATER_SKIPPING_PACKET, packet);
                }
            } else if (!pressingKey) {
                remainingPressTicks = 4;
            }
        }
    }

    public boolean canWaterSkip() {
        return this.canWaterSkip;
    }

    public void skipOnWater(Vec3d velocity) {
        player.setVelocity(velocity);
        player.getWorld().playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.PLAYERS, 1f, 1.2f, true);

        this.cooldown = 3;
        this.waterSkippingTicks = 15;
        this.wasGrounded = false;
    }
}
