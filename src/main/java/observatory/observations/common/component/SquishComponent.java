package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.NotNull;

public class SquishComponent implements AutoSyncedComponent, CommonTickingComponent {
    public PlayerEntity player;

    public SquishComponent(PlayerEntity player) {
        this.player = player;
    }

    public static SquishComponent get(@NotNull PlayerEntity player) {
        return ModComponents.SQUISH.get(player);
    }

    public void sync() {
        ModComponents.SQUISH.sync(player);
    }

    @Override
    public void tick() {
        if (TraitComponent.get(player).hasTrait(Trait.SQUISH)) {
            System.out.println("yah you got the trait");
            player.setSwimming(true);
        }
    }

    private static boolean canCrawlHere(PlayerEntity player) {
        BlockPos pos = player.getBlockPos();
        World world = player.getWorld();
        Direction facing = player.getHorizontalFacing();

        boolean oneBlockGap = world.getBlockState(pos.offset(facing)).isAir() && !player.isSprinting() && !world.getBlockState(pos.offset(facing)).isAir();

        System.out.println(oneBlockGap);
        return oneBlockGap;
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {

    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {

    }
}
