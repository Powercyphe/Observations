package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;

public class StellarFeederComponent implements ServerTickingComponent, AutoSyncedComponent {
    private static final int NIGHT_START = 13000;
    private static final int NIGHT_END = 23000;

    public PlayerEntity player;

    public StellarFeederComponent(PlayerEntity player) {
        this.player = player;
    }

    public static StellarFeederComponent get(PlayerEntity player) {
        return ModComponents.STELLAR_FEEDER.get(player);
    }

    public void sync() {
        ModComponents.STELLAR_FEEDER.sync(player);
    }


    @Override
    public void serverTick() {
        if (TraitComponent.get(player).hasTrait(Trait.STELLAR_FEEDER)) {
            if (player.getWorld().isNight() && isLookingUp(player)) {
                grantFood(player);
            }
        }
    }

    @Override
    public void readFromNbt(NbtCompound compound) {

    }

    @Override
    public void writeToNbt(NbtCompound compound) {

    }

    private static boolean isNightTime(World world) {
        long time = world.getTimeOfDay();
        return time >= NIGHT_START && time <= NIGHT_END;
    }

    private static boolean isLookingUp(PlayerEntity player) {
        return player.getPitch() < -55;
    }

    private static void grantFood(PlayerEntity player) {
        if (player.getWorld().getRandom().nextFloat() > 0.7) {
            player.getHungerManager().add(2, 1);
        }
    }
}
