package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;

public class NutritionalComponent implements ServerTickingComponent, AutoSyncedComponent {
    public PlayerEntity player;

    public NutritionalComponent(PlayerEntity player) {
        this.player = player;
    }

    public static NutritionalComponent get(PlayerEntity player) {
        return ModComponents.NUTRITIONAL.get(player);
    }

    public void sync() {
        ModComponents.NUTRITIONAL.sync(player);
    }

    @Override
    public void serverTick() {
        if (TraitComponent.get(player).hasTrait(Trait.STELLAR_FEEDER)) {
            grantFood(player);
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {}

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {}

    private static void grantFood(PlayerEntity player) {
        if (player.getWorld().getRandom().nextFloat() > 0.01) {
            player.getHungerManager().add(1, 0);
        }
    }
}
