package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.NotNull;

//WIP, currently unused
public class WeightlessComponent implements AutoSyncedComponent, CommonTickingComponent {

    private final PlayerEntity provider;
    private boolean isFlying;
    private boolean isSprinting;

    public WeightlessComponent(PlayerEntity provider) {
        this.provider = provider;
    }

    public static WeightlessComponent get(@NotNull PlayerEntity player) {
        return ModComponents.WEIGHTLESS.get(player);
    }

    @Override
    public void tick() {
        this.setFlying(isWeightlessFlying(this.provider));
        this.setSprinting(!this.provider.horizontalCollision && this.provider.isSprinting());
        ModComponents.WEIGHTLESS.sync(this.provider);
    }

    private static boolean isWeightlessFlying(PlayerEntity player) {
        if (player != null && ModComponents.TRAIT.getNullable(player) != null) {
            return TraitComponent.get(player).hasTrait(Trait.WEIGHTLESS)
                    && !player.isSwimming()
                    && !player.isUsingRiptide()
                    && !player.isFallFlying()
                    && (!player.verticalCollision || player.getPitch() < 0.0);
        }
        return false;
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.isFlying = nbtCompound.getBoolean("isFlying");
        this.isSprinting = nbtCompound.getBoolean("isSprinting");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putBoolean("isFlying", this.isFlying);
        nbtCompound.putBoolean("isSprinting", this.isSprinting);
    }

    public void setFlying(boolean isFlying) {
        this.isFlying = isFlying;
    }

    public boolean isFlying() {
        return this.isFlying;
    }

    public void setSprinting(boolean isSprinting) {
        this.isSprinting = isSprinting;
    }

    public boolean isSprinting() {
        return this.isSprinting;
    }

    public boolean isSprintFlying() {
        return this.isFlying && this.isSprinting;
    }
}
