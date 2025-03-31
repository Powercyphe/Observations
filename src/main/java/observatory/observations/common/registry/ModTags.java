package observatory.observations.common.registry;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import observatory.observations.Observations;

public class ModTags {
    // why did you use a subclass?
    public static class Items {
        public static final TagKey<Item> PROJECTILE_WEAPONS = TagKey.of(RegistryKeys.ITEM, Observations.id("projectile_weapons"));
    }

    public static final TagKey<Item> NON_FOOD_CONSUMABLES = TagKey.of(RegistryKeys.ITEM, Observations.id("non_food_consumables"));

    public void register() {}
}
