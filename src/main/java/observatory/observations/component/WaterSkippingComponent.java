package observatory.observations.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import observatory.observations.Observations;
import observatory.observations.registry.ModComponents;
import observatory.observations.registry.Trait;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Unique;

public class WaterSkippingComponent implements AutoSyncedComponent, CommonTickingComponent {

    private final PlayerEntity player;
    public int remainingSkippingTicks = 0;

    public WaterSkippingComponent(PlayerEntity player) {
        this.player = player;
    }

    public static WaterSkippingComponent get(@NotNull PlayerEntity player) {
        return ModComponents.WATER.get(player);
    }

    @Override
    public void tick() {
        if (TraitComponent.get(player).hasTrait(Trait.NO_LONGER_FLESH)) {
            if (remainingSkippingTicks > 0 && !player.isSubmergedInWater()) {
                remainingSkippingTicks--;
            } else {
                remainingSkippingTicks = 0;
            }
        }
    }

    public void skipOnWater() {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, Observations.WATER_SKIPPING_PACKET, PacketByteBufs.create());
        }
        player.jumpingCooldown = 3;
        remainingSkippingTicks = 15;
    }

    public boolean canSkipOnWater() {
        player.sendMessage(Text.literal((player.jumpingCooldown == 0) + " " + player.isTouchingWater() + " " + player.isTouchingWater() + " " + (player.getFluidHeight(FluidTags.WATER) < 0.5)));
        return player.jumpingCooldown == 0 && player.isTouchingWater() && player.isTouchingWater() && player.getFluidHeight(FluidTags.WATER) < 0.5;
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {

    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        // rizzing up the opps with style!!!
    }
}
