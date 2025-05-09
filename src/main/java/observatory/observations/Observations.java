package observatory.observations;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import observatory.observations.common.cmd.GravitySoftlockFix;
import observatory.observations.common.cmd.SafetyCommand;
import observatory.observations.common.cmd.SelfTraitCommand;
import observatory.observations.common.cmd.TraitCommand;
import observatory.observations.common.event.ConsumableEventHandler;
import observatory.observations.common.registry.ModNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.LodestoneLibClient;
import team.lodestar.lodestone.network.screenshake.ScreenshakePacket;

public class Observations implements ModInitializer {
	public static final String MOD_ID = "observations";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier WATER_SKIPPING_PACKET = id("water_skipping");
	public static final TagKey<Item> PROJECTILE_WEAPONS = TagKey.of(RegistryKeys.ITEM, new Identifier(MOD_ID, "projectile_weapons"));

	public static boolean safety = false;

	@Override
	public void onInitialize() {
		ModNetworking.init();
		TraitCommand.init();
		GravitySoftlockFix.init();
		SelfTraitCommand.init();
		SafetyCommand.init();

		// I'm so sorry i have to put this here i know its ugly
		ConsumableEventHandler.register();
	}

	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}
}