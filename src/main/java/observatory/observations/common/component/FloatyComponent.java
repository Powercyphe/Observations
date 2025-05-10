package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.NotNull;

public class FloatyComponent implements AutoSyncedComponent, CommonTickingComponent {
    private int ticks = 0;
    public PlayerEntity player;
    MinecraftClient client = MinecraftClient.getInstance();

    public FloatyComponent(PlayerEntity player) {
        this.player = player;
    }

    public static FloatyComponent get(@NotNull PlayerEntity player) {
        return ModComponents.FLOATY.get(player);
    }

    public void sync() {
        ModComponents.FLOATY.sync(player);
    }

    @Override
    public void tick() {
        if (TraitComponent.get(player).hasTrait(Trait.FLOATY)) {
            boolean isInputting = client.options.forwardKey.isPressed() ||
                    client.options.backKey.isPressed() ||
                    client.options.leftKey.isPressed() ||
                    client.options.rightKey.isPressed();
            if (!isInputting) {
                if (ticks > 80) {
                    player.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.LEVITATION, 20, 0, false, false, false));
                }
                ticks++;
            } else ticks = 0;
        }
    }

    @Override
    public void readFromNbt(NbtCompound compound) {
        this.ticks = compound.getInt("ticks");
    }

    @Override
    public void writeToNbt(NbtCompound compound) {
        compound.putInt("ticks", this.ticks);
    }
}
