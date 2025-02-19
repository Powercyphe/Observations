package observatory.observations;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public class ObservationsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		initKeybinds();

	}

	public void initKeybinds() {
		ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
			while (minecraftClient.options.jumpKey.wasPressed()) {
				PacketByteBuf packet = PacketByteBufs.create();
				ClientPlayNetworking.send(Observations.WATER_SKIPPING_PACKET, PacketByteBufs.create());
			}
		});
	}
}