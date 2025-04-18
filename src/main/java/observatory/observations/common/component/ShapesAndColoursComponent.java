package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import observatory.observations.client.shader.ShittyShader;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;

public class ShapesAndColoursComponent implements AutoSyncedComponent, ClientTickingComponent {
    public int ticks = 0;
    public PlayerEntity player;

    public ShapesAndColoursComponent(PlayerEntity player) {
        this.player = player;
    }

    public static ShapesAndColoursComponent get(PlayerEntity player) {
        return ModComponents.SHAPES_AND_COLOURS.get(player);
    }

    public void sync() {
        ModComponents.SHAPES_AND_COLOURS.sync(player);
    }

    @Override
    public void clientTick() {
        if (TraitComponent.get(player).hasTrait(Trait.SHAPES_AND_COLOURS)) {
            if (ticks < 6000) {
                ticks++;
            } else {
                if (player.getRandom().nextInt(1) + 10 > 5) {
                    ticks++;
                    ShittyShader.INSTANCE.setActive(true);
                    if (ticks > 7200) {
                        ShittyShader.INSTANCE.setActive(false);
                        ticks = 0;
                    }
                } else {
                    ticks = 0;
                }
            }
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {

    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {

    }
}
