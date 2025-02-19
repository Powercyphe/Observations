package observatory.observations.registry;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;
import observatory.observations.Observations;
import observatory.observations.component.WaterSkippingComponent;

public class ModNetworking {

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(Observations.WATER_SKIPPING_PACKET, ((minecraftServer, playerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            WaterSkippingComponent component = WaterSkippingComponent.get(playerEntity);

            playerEntity.sendMessage(Text.literal("attempt skip"));
            if (component.canSkipOnWater()) {
                component.skipOnWater();
                playerEntity.sendMessage(Text.literal("skip"));
            }
        }));
    }
}
