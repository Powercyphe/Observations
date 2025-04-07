package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.NotNull;

public class NoEnzymesComponent implements ServerTickingComponent, AutoSyncedComponent {
    public PlayerEntity player;

    public NoEnzymesComponent(PlayerEntity player) {
        this.player = player;
    }

    public static NoEnzymesComponent get(@NotNull PlayerEntity player) {
        return ModComponents.NO_ENZYMES.get(player);
    }

    @Override
    public void serverTick() {
        if (TraitComponent.get(player).hasTrait(Trait.NO_ENZYMES)) {
            StatusEffectInstance regen = new StatusEffectInstance(StatusEffects.REGENERATION, 20, 0, true, false);
            player.addStatusEffect(regen);
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {}

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {}
}
