package observatory.observations.common.registry;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import observatory.observations.Observations;

public class ModTags {

    public static class Items {
        public static final TagKey<Item> PROJECTILE_WEAPONS = TagKey.of(RegistryKeys.ITEM, Observations.id("projectile_weapons"));
    }
}
