package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.NotNull;

public class DenseComponent implements AutoSyncedComponent, CommonTickingComponent {
    public PlayerEntity player;

    public DenseComponent(PlayerEntity player) {
        this.player = player;
    }

    public static DenseComponent get(@NotNull PlayerEntity player) {
        return ModComponents.DENSE.get(player);
    }

    public void sync() {
        ModComponents.DENSE.sync(player);
    }

    @Override
    public void tick() {
        if (TraitComponent.get(player).hasTrait(Trait.DENSE) && player.isSubmergedIn(FluidTags.WATER)) {
            player.addVelocity(0, 0.2, 0);
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {

    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {

    }
}
