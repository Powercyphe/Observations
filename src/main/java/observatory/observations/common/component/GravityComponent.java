package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;

public class GravityComponent implements ServerTickingComponent, AutoSyncedComponent {
    public PlayerEntity player;

    public GravityComponent(PlayerEntity player) {
        this.player = player;
    }

    public static GravityComponent get(@NotNull PlayerEntity player) {
        return ModComponents.GRAVITY.get(player);
    }

    @Override
    public void serverTick() {
        World world = player.getWorld();

        if (TraitComponent.get(player).hasTrait(Trait.GRAVITY_CORE)) {
            boolean allAir = IntStream.rangeClosed(1, 3)
                    .allMatch(i -> world.getBlockState(player.getBlockPos().down(i)).isOf(Blocks.AIR));

            if (allAir) {
                StatusEffectInstance fallEffect = new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20, 2, true, false);
                player.addStatusEffect(fallEffect);
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
