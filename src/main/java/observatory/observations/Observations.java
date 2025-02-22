package observatory.observations;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import observatory.observations.common.cmd.TraitCommand;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.ModNetworking;
import observatory.observations.common.registry.Trait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Observations implements ModInitializer {
	public static final String MOD_ID = "observations";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier WATER_SKIPPING_PACKET = id("water_skipping");
	public static final TagKey<Item> PROJECTILE_WEAPONS = TagKey.of(RegistryKeys.ITEM, new Identifier(MOD_ID, "projectile_weapons"));

	@Override
	public void onInitialize() {
		ModNetworking.init();
		TraitCommand.init();
	}

	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}

	public static boolean isWeightlessFlying(PlayerEntity player) {
		if (player != null && ModComponents.TRAIT.getNullable(player) != null) {
			return TraitComponent.get(player).hasTrait(Trait.INFINITE_FREEDOM)
					&& !player.isSwimming()
					&& !player.isUsingRiptide()
					&& !player.isFallFlying()
					&& (!player.verticalCollision || player.getPitch() < 0.0);
		}
		return false;
	}
}