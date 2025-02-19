package observatory.observations.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
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
        player.jump();
        player.setVelocity(player.getVelocity().getX() * 2.14, player.getVelocity().getY(), player.getVelocity().getZ() * 2.14);
        player.jumpingCooldown = 3;
        remainingSkippingTicks = 15;
    }

    public boolean canSkipOnWater() {
        return player.jumpingCooldown == 0 && player.isTouchingWater() && !player.isSubmergedInWater() && player.getFluidHeight(FluidTags.WATER) < 0.5 && player.getVelocity().multiply(1, 0, 1).length() > 0.05;
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {

    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        // rizzing up the opps with style!!!
    }
}
