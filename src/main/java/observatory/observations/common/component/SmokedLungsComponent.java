package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.NotNull;

public class SmokedLungsComponent implements AutoSyncedComponent, CommonTickingComponent {
    public PlayerEntity player;

    public static String AIR_KEY = "observations.air";
    public int air = 20;

    public static String TICK_COUNTER = "observations.tick_counter";
    public int tickCounter = 0;

    public SmokedLungsComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        ModComponents.SMOKED_LUNGS.sync(player);
    }

    public static SmokedLungsComponent get(@NotNull PlayerEntity player) {
        return ModComponents.SMOKED_LUNGS.get(player);
    }

    @Override
    public void tick() {
        if (TraitComponent.get(player).hasTrait(Trait.SMOKED_LUNGS)) {
            if (player.getEyeY() > 200) {
                if (tickCounter > 20) {
                    tickCounter = 20;
                    air = air - 2;
                    if (air < 0) {
                        if (!player.isAlive()) {
                            this.air = 20;
                        }
                        player.damage(player.getDamageSources().cramming(), 2f);
                    }
                }
                tickCounter++;
            }
        }
    }

    @Override
    public void readFromNbt(NbtCompound compound) {
        this.air = compound.getInt(AIR_KEY);
        this.tickCounter = compound.getInt(TICK_COUNTER);
    }

    @Override
    public void writeToNbt(NbtCompound compound) {
        compound.putInt(AIR_KEY, this.air);
        compound.putInt(TICK_COUNTER, this.tickCounter);
    }
}
