package observatory.observations.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import observatory.observations.registry.ModComponents;
import org.jetbrains.annotations.NotNull;

public class StarComponent implements AutoSyncedComponent, CommonTickingComponent {
    private static final String STARRY_KEY = "starry";
    private static final String TIME_KEY = "time";

    private float time = 0;
    private boolean starry = false;

    private final PlayerEntity player;

    public StarComponent(PlayerEntity player) {
        this.player = player;
    }

    public static StarComponent get(@NotNull PlayerEntity player) {
        return ModComponents.STAR.get(player);
    }

    @Override
    public void tick() {
        World world = player.getWorld();

        if (starry) {
            int random = player.getRandom().nextBetween(1, 20);
            if (random == 1) {
                double offsetX = (player.getRandom().nextDouble() - 0.5) * 2 * 0.5;
                double offsetY = (player.getRandom().nextDouble() - 0.5) * 2 * 0.5;
                double offsetZ = (player.getRandom().nextDouble() - 0.5) * 2 * 0.5;

                player.getWorld().addParticle(ParticleTypes.CRIT, player.getX(), player.getEyeY() - 0.4, player.getZ(), offsetX, offsetY, offsetZ);

                for (PlayerEntity target : world.getPlayers()) {
                    if (target.squaredDistanceTo(player) <= 100) {
                        get(target).setStarry(player.getRandom().nextBetween(1, 20) * 20);
                    }
                }
            }
        }
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.starry = buf.readBoolean();
        this.time = buf.readFloat();
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeBoolean(starry);
        buf.writeFloat(time);
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.starry = nbtCompound.getBoolean(STARRY_KEY);
        this.time = nbtCompound.getFloat(TIME_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putBoolean(STARRY_KEY, this.starry);
        nbtCompound.putFloat(TIME_KEY, this.time);
    }

    public void rotateStarry() {
        starry = !starry;
    }

    public boolean getStarry() {
        return starry;
    }

    public void setStarry(float ticks) {
        time = ticks;

        if (!starry) {
            rotateStarry();
        }
    }
}