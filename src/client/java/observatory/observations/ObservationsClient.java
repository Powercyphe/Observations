package observatory.observations;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class ObservationsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		initKeybinds();

	}

	public void initNetworking() {
		ClientPlayNetworking.registerGlobalReceiver(Observations.WATER_SKIPPING_PACKET, ((minecraftClient, clientPlayNetworkHandler, packetByteBuf, packetSender) -> {
			ClientPlayerEntity player = minecraftClient.player;
			if (player != null) {
				player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.PLAYERS, 1f, 1f);
				for (int i = 0; i < 14; i++) {
					player.getWorld().addParticle(ParticleTypes.SPLASH, player.getX(), player.getY(), player.getZ(), player.getVelocity().getX(), player.getVelocity().getY(), player.getVelocity().getZ());
				}

				player.jump();
				player.setVelocity(player.getVelocity().getX() * 2.14, player.getVelocity().getY(), player.getVelocity().getZ() * 2.14);
			}
		}));
	}

	public void initKeybinds() {
		ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
			while (minecraftClient.options.jumpKey.wasPressed()) {
				ClientPlayNetworking.send(Observations.WATER_SKIPPING_PACKET, PacketByteBufs.create());
			}
		});
	}
}