package observatory.observations.common.cmd;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class GravitySoftlockFix {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("gravityfix")
                    .requires(source -> source.hasPermissionLevel(0)) // Requires OP level 2+
                    .then(argument("player", EntityArgumentType.player())
                            .executes(ctx -> execute(
                                    ctx.getSource(),
                                    EntityArgumentType.getPlayer(ctx, "player")
                            )
                        )
                    )
            );
        });
    }

    private static int execute(ServerCommandSource source, ServerPlayerEntity player) {
        player.setNoGravity(false);
        return 1;
    }
}
