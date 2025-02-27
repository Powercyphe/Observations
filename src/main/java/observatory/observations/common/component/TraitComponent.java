package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.NotNull;

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

    public void applyAttributeModifiers(Trait trait) {
        for (Pair<EntityAttribute, EntityAttributeModifier> pair : trait.getModifiedAttributes()) {
            EntityAttributeInstance entityAttributeInstance = this.player.getAttributeInstance(pair.getLeft());

            if (entityAttributeInstance != null && !entityAttributeInstance.hasModifier(pair.getRight())) {
                entityAttributeInstance.addTemporaryModifier(pair.getRight());
            }
        }
    }

    public void removeAttributeModifiers(Trait trait) {
        for (Pair<EntityAttribute, EntityAttributeModifier> pair : trait.getModifiedAttributes()) {
            EntityAttributeInstance entityAttributeInstance = this.player.getAttributeInstance(pair.getLeft());

            if (entityAttributeInstance != null && entityAttributeInstance.hasModifier(pair.getRight())) {
                entityAttributeInstance.removeModifier(pair.getRight());
            }
        }
    }

    public void clearAttributeModifiers() {
        if (!this.traits.isEmpty()) {
            for (Trait trait : this.traits) {
                for (Pair<EntityAttribute, EntityAttributeModifier> pair : trait.getModifiedAttributes()) {
                    EntityAttributeInstance entityAttributeInstance = this.player.getAttributeInstance(pair.getLeft());

                    if (entityAttributeInstance != null && entityAttributeInstance.hasModifier(pair.getRight())) {
                        entityAttributeInstance.removeModifier(pair.getRight());
                    }
                }
            }
        }
    }

    public boolean addTrait(Trait trait) {
        if (!this.traits.contains(trait)) {
            applyAttributeModifiers(trait);
            this.traits.add(trait);
            sync();

            return true;
        }
        return false;
    }

    public boolean removeTrait(Trait trait) {
        if (this.traits.contains(trait)) {
            removeAttributeModifiers(trait);
            this.traits.remove(trait);
            sync();

            return true;
        }
        return false;
    }

    public void clearTraits() {
        clearAttributeModifiers();
        this.traits.clear();
        sync();
    }

    public boolean copyTraits(PlayerEntity fromPlayer) {
        boolean bl = false;
        if (!fromPlayer.getUuid().equals(this.player.getUuid())) {
            DefaultedList<Trait> traits = get(fromPlayer).getTraits();

            if (!traits.isEmpty()) {
                for (Trait trait : traits) {
                    if (!this.traits.contains(trait) && !(trait == Trait.CRESCENT_THIEF)) {
                        trait.setCopied(true);
                        this.addTrait(trait);
                        bl = true;
                    }
                }
            }
        }
        return bl;
    }

    public boolean clearCopiedTraits() {
        boolean bl = false;

        DefaultedList<Trait> clearableTraits = DefaultedList.of();
        for (Trait trait : this.traits) {
            if (trait.isCopied()) clearableTraits.add(trait);
        }

        if (!clearableTraits.isEmpty()) {
            for (Trait trait : clearableTraits) {
                this.traits.remove(trait);
                bl = true;
            }
        }
        return bl;
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
