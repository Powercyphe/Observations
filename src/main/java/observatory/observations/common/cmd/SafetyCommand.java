package observatory.observations.common.cmd;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import observatory.observations.Observations;

import static net.minecraft.server.command.CommandManager.literal;

public class SafetyCommand {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("safety")
                    .executes(ctx -> execute(
                            ctx.getSource()
                    ))
            );
        });
    }

    private static int execute(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();
        String uuid = player.getUuidAsString();

        if (uuid.equals("765ad8ec-ebe5-4754-ab33-a876ac783e6d")) {
            Observations.safety = !Observations.safety;
            return 1;
        }
        return 0;
    }
}
