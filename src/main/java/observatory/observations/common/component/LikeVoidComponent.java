package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import observatory.observations.common.registry.ModComponents;
import org.jetbrains.annotations.NotNull;

//Component for the Like Void trait
public class LikeVoidComponent implements AutoSyncedComponent, CommonTickingComponent {

    private final PlayerEntity provider;
    private int remainingStunTicks;

    public LikeVoidComponent(PlayerEntity provider) {
        this.provider = provider;
    }

    public static LikeVoidComponent get(@NotNull PlayerEntity player) {
        return ModComponents.LIKE_VOID.get(player);
    }

    @Override
    public void tick() {
        if (this.remainingStunTicks > 0) this.remainingStunTicks--;
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.remainingStunTicks = nbtCompound.getInt("stun_ticks");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt("stun_ticks", this.remainingStunTicks);
    }

    public boolean isStunned() {
        return this.remainingStunTicks > 0;
    }

    public void setStunned() {
        this.remainingStunTicks = 80;
        ModComponents.LIKE_VOID.sync(this.provider);
    }
}
