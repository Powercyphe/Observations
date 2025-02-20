package observatory.observations;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import observatory.observations.common.cmd.TraitCommand;
import observatory.observations.common.registry.ModNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Observations implements ModInitializer {
	public static final String MOD_ID = "observations";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModNetworking.init();

		TraitCommand.init();

	}

	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}
}