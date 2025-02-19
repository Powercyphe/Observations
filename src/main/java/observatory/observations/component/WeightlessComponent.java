package observatory.observations.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import observatory.observations.registry.ModComponents;
import org.jetbrains.annotations.NotNull;


//Combines the traits for Weightless and Infinite Freedom
//Weightless is essentially just a boolean required for Entity#hasNoGravity & the like
public class WeightlessComponent implements AutoSyncedComponent, CommonTickingComponent {

    private final PlayerEntity provider;

    public WeightlessComponent(PlayerEntity provider) {
        this.provider = provider;
    }

    public static WeightlessComponent get(@NotNull PlayerEntity player) {
        return ModComponents.WEIGHTLESS.get(player);
    }

    @Override
    public void tick() {

    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
    }

    public boolean isPlayerInAir() {
        return !this.provider.isOnGround();
    }
}
