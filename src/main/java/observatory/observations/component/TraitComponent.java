package observatory.observations.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import observatory.observations.registry.ModComponents;
import observatory.observations.registry.Trait;
import org.jetbrains.annotations.NotNull;

public class TraitComponent implements AutoSyncedComponent, CommonTickingComponent {
    private static final String TRAITS_KEY = "traits";
    private static final String TRAIT_ID_KEY = "id";

    private DefaultedList<Trait> traits;

    private final PlayerEntity player;

    public TraitComponent(PlayerEntity player) {
        this.player = player;
        this.traits = DefaultedList.of();
    }

    public static TraitComponent get(@NotNull PlayerEntity player) {
        return ModComponents.TRAIT.get(player);
    }

    public void sync() {
        ModComponents.TRAIT.sync(this.player);
    }

    public void addTrait(Trait trait) {
        if (!this.traits.contains(trait)) {
            this.traits.add(trait);
            sync();
        }
    }

    public void removeTrait(Trait trait) {
        this.traits.remove(trait);
        sync();
    }

    public void clearTraits() {
        this.traits.clear();
        sync();
    }

    public DefaultedList<Trait> getTraits() {
        return this.traits;
    }

    public boolean hasTrait(Trait trait) {
        return this.getTraits().contains(trait);
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.traits.clear();
        NbtList nbtList = nbtCompound.getList(TRAITS_KEY, 0);
        for (NbtElement nbtElement : nbtList) {
            if (nbtElement instanceof NbtCompound traitNbt && traitNbt.contains(TRAIT_ID_KEY)) {
                this.traits.add(Trait.fromString(traitNbt.getString(TRAIT_ID_KEY)));
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        NbtList nbtList = new NbtList();
        for (Trait trait : this.traits) {
            NbtCompound traitNbt = new NbtCompound();
            traitNbt.putString(TRAIT_ID_KEY, trait.id);
            nbtList.add(traitNbt);
        }
        nbtCompound.put(TRAITS_KEY, nbtList);
    }

    @Override
    public void tick() {
    }

    @Override
    public void clientTick() {

    }
}
