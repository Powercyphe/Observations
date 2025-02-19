package observatory.observations;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import observatory.observations.cmd.TraitCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Observations implements ModInitializer {
	public static final String MOD_ID = "observations";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier WATER_SKIPPING_PACKET = id("water_skipping");

	@Override
	public void onInitialize() {
		TraitCommand.init();

	}

	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}
}