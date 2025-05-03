package observatory.observations.common.cmd;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;

import static net.minecraft.server.command.CommandManager.literal;

public class SelfTraitCommand {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("start")
                .executes(ctx -> execute(
                        ctx.getSource()
                ))
            );
        });
    }

    private static int execute(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();
        String uuid = player.getUuidAsString();

        TraitComponent component = TraitComponent.get(player);
        if (uuid.equals("4dcc927a-f441-40d1-b6ee-04714fa29c9f")) { // Nopeable
            component.addTrait(Trait.BANDIT);
            component.addTrait(Trait.PICKPOCKET);
//            component.addTrait(Trait.);

            source.sendFeedback(() -> {
                return Text.literal("Squish isn't done cause i dont have time, ill do it in the future");
            }, true);
            return 1;
        } else if (uuid.equals("60551cc5-1093-42ca-af9c-0edf4fba4969")) { // Harper
            component.addTrait(Trait.SHAPES_AND_COLOURS);
            component.addTrait(Trait.NUTRITIONAL);
            component.addTrait(Trait.GASSED_UP);

            source.sendFeedback(() -> {
                return Text.literal("Added your traits! Enjoy!");
            }, true);
            return 1;
        } else if (uuid.equals("e7fc72db-9cde-4c62-818f-0faa9ec79741")) { // Hailey
            component.addTrait(Trait.WEEEE);
            component.addTrait(Trait.HEAVY_HITTER);
            component.addTrait(Trait.WEIGHED_DOWN);

            source.sendFeedback(() -> {
                return Text.literal("Added your traits! Enjoy!");
            }, true);
            return 1;
        } else if (uuid.equals("ed8fda88-cf2b-46a0-8058-5b9a9ce660e4")) { // Plur
            component.addTrait(Trait.CRESCENT_THIEF);

            source.sendFeedback(() -> {
                return Text.literal("Added your trait! Enjoy!");
            }, true);
            return 1;
        } else if (uuid.equals("6a3f9818-7a84-49d6-9976-e3e8cfc369ee")) { // Kaja??
            component.addTrait(Trait.SOULLESS_CREATURE);
            component.addTrait(Trait.STRONG_HANDS_EVEN_STRONGER_MORALS);
            component.addTrait(Trait.PHOTOSYNTHESIS);

            source.sendFeedback(() -> {
                return Text.literal("Added your traits! Enjoy!");
            }, true);
            return 1;
        } else if (uuid.equals("80b7c27a-0f55-4237-a213-a998a67dbf10")) { // Buttery Boy
            component.addTrait(Trait.MAGMA_COVERED);
            component.addTrait(Trait.FAMILIARITY);
            component.addTrait(Trait.COMFORTING_WARMTH);

            source.sendFeedback(() -> {
                return Text.literal("Added your traits! Enjoy!");
            }, true);
            return 1;
        } else if (uuid.equals("8fdff6b5-f6a5-4fed-a006-0f883e1d6a4a")) { // Winter
            component.addTrait(Trait.PRIVILEGED_PLATES);
            component.addTrait(Trait.SMOKED_LUNGS);
            component.addTrait(Trait.AURIC_ARTERIES);

            source.sendFeedback(() -> {
                return Text.literal("Added your traits! Enjoy!");
            }, true);
            return 1;
        } else if (uuid.equals("4086e5d8-8a0c-477f-a4ec-7ee25f038bcd")) { // Wazzo
            component.addTrait(Trait.BUG_LIKE_APPEARANCE);
            component.addTrait(Trait.SILK_SACK);
            component.addTrait(Trait.AGILE);

            source.sendFeedback(() -> {
                return Text.literal("Added your traits! Enjoy!");
            }, true);
            return 1;
        } else if (uuid.equals("4c8c5665-a9c8-43fe-b6d3-d8046846b399")) { // Rafsa!
            component.addTrait(Trait.STRONGER_THAN_STONE);
            component.addTrait(Trait.WILL_TO_LIVE_ON);
            component.addTrait(Trait.INFECTION);

            source.sendFeedback(() -> {
                return Text.literal("Added your traits! Enjoy!");
            }, true);
            return 1;
        } else if (uuid.equals("765ad8ec-ebe5-4754-ab33-a876ac783e6d")) { // Mel!
            component.addTrait(Trait.LINE_OF_SIGHT);
            component.addTrait(Trait.NO_ENZYMES);
            component.addTrait(Trait.ACROBATICS);

            source.sendFeedback(() -> {
                return Text.literal("Added your traits! Enjoy!");
            }, true);
            return 1;
        } else if (uuid.equals("a9bcfe9b-bb80-463d-848e-11e0b03f2b6e")) { // Eight Tiny
            component.addTrait(Trait.WEIGHTLESS);
            component.addTrait(Trait.INFINITE_FREEDOM);
            component.addTrait(Trait.LIKE_VOID);

            source.sendFeedback(() -> {
                return Text.literal("Added your traits! Enjoy!");
            }, true);
            return 1;
        } else if (uuid.equals("c58ec595-82d5-476c-a5fc-be369f6708a2")) { // Vannie
            component.addTrait(Trait.GEODE_SOUL);
            component.addTrait(Trait.OBVIOUS_CRACKS);
            component.addTrait(Trait.NO_LONGER_FLESH);

            source.sendFeedback(() -> {
                return Text.literal("Added your traits! Enjoy!");
            }, true);
            return 1;
        } else if (uuid.equals("9a5abccf-5013-423d-b137-453b13f07cab")) { // Oh my god its star person
            component.addTrait(Trait.GRAVITY_CORE);
            component.addTrait(Trait.STELLAR_FEEDER);
            component.addTrait(Trait.ENTROPY_PULSE);

            source.sendFeedback(() -> {
                return Text.literal("Added your traits! Enjoy!");
            }, true);
            return 1;
        }
        source.sendError(Text.literal("You don't have traits, ask everest"));
        return 0;
    }
}
