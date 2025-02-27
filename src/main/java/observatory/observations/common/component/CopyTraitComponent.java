package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;

public class CopyTraitComponent implements AutoSyncedComponent, ServerTickingComponent {

    private final PlayerEntity provider;
    private int copyCooldown;

    public CopyTraitComponent(PlayerEntity provider) {
        this.provider = provider;
    }

    @Override
    public void serverTick() {
        if (this.copyCooldown > 0) {
            this.copyCooldown--;
        }
        else if (TraitComponent.get(this.provider).hasTrait(Trait.CRESCENT_THIEF)) {
            TraitComponent.get(this.provider).clearCopiedTraits();

            PlayerEntity fromPlayer = this.provider.getWorld().getClosestPlayer(this.provider.getX(), this.provider.getY(), this.provider.getZ(), 32, entity -> !entity.isSpectator() && entity!= this.provider);
            if (fromPlayer != null) {
                if (TraitComponent.get(this.provider).copyTraits(fromPlayer)) {
                    this.provider.sendMessage(Text.literal("Stole " + fromPlayer.getName().getString() + "'s traits!"), true);
                    this.copyCooldown = 1200;
                }
            }
        }
        ModComponents.COPY_TRAIT.sync(this.provider);
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.copyCooldown = nbtCompound.getInt("copy_cooldown");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt("copy_cooldown", this.copyCooldown);
    }
}
