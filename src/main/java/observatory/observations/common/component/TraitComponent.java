package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TraitComponent implements AutoSyncedComponent, CommonTickingComponent {
    private static final String TRAITS_KEY = "traits";
    private static final String TRAIT_ID_KEY = "id";

    private static final int REFRESH_TICKS = 100;

    private int tick = 0;
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

    public boolean addTrait(Trait trait) {
        if (!this.traits.contains(trait)) {
            this.traits.add(trait);
            sync();
            return true;
        }
        return false;
    }

    public boolean removeTrait(Trait trait) {
        if (this.traits.contains(trait)) {
            this.traits.remove(trait);
            sync();
            return true;
        }
        return false;
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
    public void readFromNbt(@NotNull NbtCompound nbt) {
        clearTraits();
        NbtList nbtList = nbt.getList(TRAITS_KEY, 10);
        for (NbtElement nbtElement : nbtList) {
            if (nbtElement instanceof NbtCompound traitNbt && traitNbt.contains(TRAIT_ID_KEY)) {
                addTrait(Trait.fromString(traitNbt.getString(TRAIT_ID_KEY)));
            }
        }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound nbt) {
        NbtList nbtList = new NbtList();
        for (Trait trait : this.getTraits()) {
            NbtCompound traitNbt = new NbtCompound();
            traitNbt.putString(TRAIT_ID_KEY, trait.getId());
            nbtList.add(traitNbt);
        }
        nbt.put(TRAITS_KEY, nbtList);
    }

    @Override
    public void tick() {
        tick++;
        if (tick > REFRESH_TICKS) {
            tick = 0;
            sync();
        }
    }

}
