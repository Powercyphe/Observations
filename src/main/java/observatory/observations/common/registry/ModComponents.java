package observatory.observations.common.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import observatory.observations.Observations;
import observatory.observations.common.component.*;

public class ModComponents implements EntityComponentInitializer {

    public static final ComponentKey<TraitComponent> TRAIT =
            ComponentRegistry.getOrCreate(Observations.id("trait"), TraitComponent.class);
    public static final ComponentKey<BuddingComponent> BUDDING =
            ComponentRegistry.getOrCreate(Observations.id("budding"), BuddingComponent.class);
    public static final ComponentKey<WaterSkippingComponent> WATER_SKIPPING =
            ComponentRegistry.getOrCreate(Observations.id("water_skipping"), WaterSkippingComponent.class);
    public static final ComponentKey<LikeVoidComponent> LIKE_VOID =
            ComponentRegistry.getOrCreate(Observations.id("like_void"), LikeVoidComponent.class);
    public static final ComponentKey<CopyTraitComponent> COPY_TRAIT =
            ComponentRegistry.getOrCreate(Observations.id("copy_trait"), CopyTraitComponent.class);
    public static final ComponentKey<GravityComponent> GRAVITY =
            ComponentRegistry.getOrCreate(Observations.id("gravity"), GravityComponent.class);
    public static final ComponentKey<StellarFeederComponent> STELLAR_FEEDER =
            ComponentRegistry.getOrCreate(Observations.id("stellar_feeder"), StellarFeederComponent.class);
    public static final ComponentKey<NoEnzymesComponent> NO_ENZYMES =
            ComponentRegistry.getOrCreate(Observations.id("no_enzymes"), NoEnzymesComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(TRAIT, TraitComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(BUDDING, BuddingComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(WATER_SKIPPING, WaterSkippingComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(LIKE_VOID, LikeVoidComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(COPY_TRAIT, CopyTraitComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(GRAVITY, GravityComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(NO_ENZYMES, NoEnzymesComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
