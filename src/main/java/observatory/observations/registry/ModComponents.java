package observatory.observations.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import observatory.observations.Observations;
import observatory.observations.component.*;

public class ModComponents implements EntityComponentInitializer {

    public static final ComponentKey<BuddingComponent> BUDDING =
            ComponentRegistry.getOrCreate(Observations.id("budding"), BuddingComponent.class);
    public static final ComponentKey<StarComponent> STAR =
            ComponentRegistry.getOrCreate(Observations.id("star"), StarComponent.class);
    public static final ComponentKey<WaterSkippingComponent> WATER =
            ComponentRegistry.getOrCreate(Observations.id("water_skipping"), WaterSkippingComponent.class);
    public static final ComponentKey<WeightlessComponent> WEIGHTLESS =
            ComponentRegistry.getOrCreate(Observations.id("weightless"), WeightlessComponent.class);
    public static final ComponentKey<TraitComponent> TRAIT =
            ComponentRegistry.getOrCreate(Observations.id("trait"), TraitComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(BUDDING, BuddingComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(STAR, StarComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(WATER, WaterSkippingComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(WEIGHTLESS, WeightlessComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(TRAIT, TraitComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }

}
