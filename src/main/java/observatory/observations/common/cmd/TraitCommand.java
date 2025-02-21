package observatory.observations.common.cmd;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
                                    .executes(ctx -> execute(
                                            ctx.getSource(),
                                            EntityArgumentType.getPlayer(ctx, "player"),
                                            StringArgumentType.getString(ctx, "operation"),
                                            null
                                    ))
                                    .then(argument("trait", StringArgumentType.string())
                                            .suggests((context, builder) -> CommandSource.suggestMatching(getTraitNames(), builder)) // Suggests available traits
                                            .executes(ctx -> execute(
                                                    ctx.getSource(),
                                                    EntityArgumentType.getPlayer(ctx, "player"),
                                                    StringArgumentType.getString(ctx, "operation"),
                                                    StringArgumentType.getString(ctx, "trait")
                                            ))
                                    )
                            )
                    )
            );
        });
    }

    private static int execute(ServerCommandSource source, ServerPlayerEntity player, String operationId, String traitId) {
        TraitComponent component = TraitComponent.get(player);
        Trait trait = Trait.fromString(traitId);
        Operation operation = Operation.fromId(operationId);

        // Check for invalid Operation
        if (operation == null) {
            source.sendFeedback(() -> {
                return Text.translatable("commands.observations.trait.invalid_operation", operationId).formatted(Formatting.RED);
            }, true);
            return 0;
        }

        // Check for invalid Trait
        if (trait == null && operation.requiresTrait()) {
            source.sendFeedback(() -> {
                return Text.translatable("commands.observations.trait.invalid_trait", traitId == null ? "<Empty>" : traitId).formatted(Formatting.RED);
            }, true);
            return 0;
        }

        // Run the Operation
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
                .map(Trait::getId)
                .collect(Collectors.toList());
    }


    private enum Operation implements StringIdentifiable {
        ADD("add", true),
        REMOVE("remove", true),
        GET("get", false),
        CLEAR("clear", false);

        private final String id;
        private final boolean requiresTrait;

        Operation(String id, boolean requiresTrait) {
            this.id = id;
            this.requiresTrait = requiresTrait;
        }

        public boolean requiresTrait() {
            return this.requiresTrait;
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
