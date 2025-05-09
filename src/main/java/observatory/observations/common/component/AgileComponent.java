package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.NotNull;

public class AgileComponent implements AutoSyncedComponent, CommonTickingComponent {
    public PlayerEntity player;

    public AgileComponent(PlayerEntity player) {
        this.player = player;
    }

    public static AgileComponent get(@NotNull PlayerEntity player) {
        return ModComponents.AGILE.get(player);
    }

    public void sync() {
        ModComponents.AGILE.sync(player);
    }


    @Override
    public void tick() {
        if (TraitComponent.get(player).hasTrait(Trait.AGILE) || TraitComponent.get(player).hasTrait(Trait.BANDIT)) {
            if (onWall(player)) {
                player.setNoGravity(true);
                if (player.isSneaking()) {
                    player.setNoGravity(false);

                    Vec3d pushVector = getWallPushVector(player);
                    player.setVelocity(pushVector);
                }
            } else if (player.hasNoGravity()) {
                player.setNoGravity(false);
            }
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {

    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {

    }

    private boolean onWall(PlayerEntity player) {
        BlockPos blockPos = player.getBlockPos();
        World world = player.getWorld();

        if (world.getBlockState(blockPos.down()).isOf(Blocks.AIR)) {
            boolean hasNonAirNeighbor = Direction.Type.HORIZONTAL.stream()
                    .map(blockPos::offset)
                    .anyMatch(pos -> !world.getBlockState(pos).isOf(Blocks.AIR) && world.getBlockState(pos).isFullCube(world, pos));

            if (hasNonAirNeighbor) {
                return true;
            }
        }
        return false;
    }

    private Vec3d getWallPushVector(PlayerEntity player) {
        BlockPos playerPos = player.getBlockPos();
        World world = player.getWorld();

        for (Direction direction : Direction.Type.HORIZONTAL) {
            BlockPos neighborPos = playerPos.offset(direction);
            if (!world.getBlockState(neighborPos).isAir()) {
                Vec3d away = new Vec3d(direction.getOpposite().getOffsetX(), 0.5, direction.getOpposite().getOffsetZ());
                return away.normalize().multiply(0.8);
            }
        }

        return Vec3d.ZERO;
    }
}
