package observatory.observations.registry;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import observatory.observations.Observations;

public class ModNetworking {

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(Observations.WATER_SKIPPING_PACKET, ((minecraftServer, playerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            
        }));
    }
}
