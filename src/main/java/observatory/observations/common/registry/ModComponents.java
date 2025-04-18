package observatory.observations.common.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import observatory.observations.Observations;
import observatory.observations.common.component.*;
import oshi.jna.platform.mac.SystemB;

import java.awt.*;

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
    public static final ComponentKey<NutritionalComponent> NUTRITIONAL =
            ComponentRegistry.getOrCreate(Observations.id("nutritional"), NutritionalComponent.class);
    public static final ComponentKey<SmokedLungsComponent> SMOKED_LUNGS =
            ComponentRegistry.getOrCreate(Observations.id("smoked_lungs"), SmokedLungsComponent.class);
    public static final ComponentKey<AgileComponent> AGILE =
            ComponentRegistry.getOrCreate(Observations.id("agile"), AgileComponent.class);
    public static final ComponentKey<ShapesAndColoursComponent> SHAPES_AND_COLOURS =
            ComponentRegistry.getOrCreate(Observations.id("shapes_and_colours"), ShapesAndColoursComponent.class);
    public static final ComponentKey<PrivilegedComponent> PRIVILEGED =
            ComponentRegistry.getOrCreate(Observations.id("privileged"), PrivilegedComponent.class);
    public static final ComponentKey<ComfortingWarmthComponent> COMFORTING_WARMTH =
            ComponentRegistry.getOrCreate(Observations.id("comforting_warmth"), ComfortingWarmthComponent.class);


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(TRAIT, TraitComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(BUDDING, BuddingComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(WATER_SKIPPING, WaterSkippingComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(LIKE_VOID, LikeVoidComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(COPY_TRAIT, CopyTraitComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(GRAVITY, GravityComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(NO_ENZYMES, NoEnzymesComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(NUTRITIONAL, NutritionalComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(SMOKED_LUNGS, SmokedLungsComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(AGILE, AgileComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(SHAPES_AND_COLOURS, ShapesAndColoursComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(PRIVILEGED, PrivilegedComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(COMFORTING_WARMTH, ComfortingWarmthComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
