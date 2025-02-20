package observatory.observations.common.registry;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import observatory.observations.Observations;
import observatory.observations.common.component.WaterSkippingComponent;

public class ModNetworking {

    public static final Identifier WATER_SKIPPING_PACKET = Observations.id("water_skipping");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(WATER_SKIPPING_PACKET, ((minecraftServer, playerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            Vec3d vec3d = new Vec3d(packetByteBuf.readVector3f());

            WaterSkippingComponent component = WaterSkippingComponent.get(playerEntity);
            if (component.canWaterSkip()) {
                component.skipOnWater(vec3d);
            }
        }));
    }
}
