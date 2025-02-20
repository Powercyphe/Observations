package observatory.observations.common.cmd;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.collection.DefaultedList;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TraitCommand {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("trait")
                    .requires(source -> source.hasPermissionLevel(2)) // Requires OP level 2+
                    .then(argument("player", EntityArgumentType.player())
                            .then(argument("operation", StringArgumentType.string())
                                    .suggests((context, builder) -> CommandSource.suggestMatching(addOrRemove(), builder))
                                    .then(argument("trait", StringArgumentType.string())
                                            .suggests((context, builder) -> CommandSource.suggestMatching(getTraitNames(), builder)) // Suggests available traits
                                            .executes(ctx -> setTraits(
                                                    ctx.getSource(),
                                                    EntityArgumentType.getPlayer(ctx, "player"),
                                                    Operation.fromId(StringArgumentType.getString(ctx, "operation")),
                                                    Trait.fromString(StringArgumentType.getString(ctx, "trait"))
                                            ))
                                    )
                            )
                    )
            );
        });
    }

    private static int setTraits(ServerCommandSource source, ServerPlayerEntity player, Operation operation, Trait trait) {
        TraitComponent component = TraitComponent.get(player);
        if (operation == Operation.ADD) {
            component.addTrait(trait);
            source.sendFeedback(() -> {
                return Text.translatable("commands.observations.trait.add", trait, player.getDisplayName());
            }, true);
            return 1;
        }
        if (operation == Operation.REMOVE) {
            component.removeTrait(trait);
            source.sendFeedback(() -> {
                return Text.translatable("commands.observations.trait.remove", trait, player.getDisplayName());
            }, true);
            return 1;
        }
        if (operation == Operation.GET) {
            DefaultedList<Trait> traits = component.getTraits();
            source.sendFeedback(() -> {
                return Text.translatable("commands.observations.trait.get", player.getDisplayName(), traits.toString());
            }, true);
            return 1;
        }
        if (operation == Operation.CLEAR) {
            component.clearTraits();
            source.sendFeedback(() -> {
                return Text.translatable("commands.observations.trait.clear", player.getDisplayName());
            }, true);
            return 1;
        }

        return 0;
    }

    private static List<String> getTraitNames() {
        return Arrays.stream(Trait.values())
                .map(trait -> trait.id)
                .collect(Collectors.toList());
    }


    private enum Operation implements StringIdentifiable {
        ADD("add"),
        REMOVE("remove"),
        GET("get"),
        CLEAR("clear");

        public final String id;

        Operation(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return this.id;
        }

        public static Operation fromId(String id) {
            for (Operation operation : values()) {
                if (operation.id.equals(id)) {
                    return operation;
                }
            }
            return null;
        }
    }

    private static List<String> addOrRemove() {
        List<String> strings = new ArrayList<>();
        for (Operation operation : Operation.values()) {
            strings.add(operation.id);
        }
        return strings;
    }
}
