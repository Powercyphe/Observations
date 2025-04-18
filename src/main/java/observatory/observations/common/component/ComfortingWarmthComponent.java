package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import observatory.observations.client.shader.HeatShader;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import observatory.observations.common.util.TraitUtil;
import org.jetbrains.annotations.NotNull;

public class ComfortingWarmthComponent implements AutoSyncedComponent, CommonTickingComponent {
    public PlayerEntity player;

    public ComfortingWarmthComponent(PlayerEntity player) {
        this.player = player;
    }

    public static ComfortingWarmthComponent get(@NotNull PlayerEntity player) {
        return ModComponents.COMFORTING_WARMTH.get(player);
    }

    public void sync() {
        ModComponents.GRAVITY.sync(player);
    }

    @Override
    public void tick() {
        if (TraitComponent.get(player).hasTrait(Trait.COMFORTING_WARMTH)) {
            if (player.getWorld().isClient) {
                HeatShader.INSTANCE.setActive(true);
            } else {
                boolean inLava = player.isSubmergedIn(FluidTags.LAVA);
                boolean onFire = player.isOnFire();

                if (inLava) {
                    player.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.REGENERATION, 40, 1, true, false, true));
                } else if (onFire) {
                    player.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.REGENERATION, 40, 0, true, false, true));
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
