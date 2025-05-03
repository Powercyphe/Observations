package observatory.observations.common.event;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.random.Random;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.ModTags;
import observatory.observations.common.registry.Trait;

public class ConsumableEventHandler {
    // if anyone can help with getting this to work with arrows and fireworks in bows / crossbows i would much appropriate it

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (TraitComponent.get(player).hasTrait(Trait.ENTROPY_PULSE)) {
                if (world.isClient) return TypedActionResult.pass(ItemStack.EMPTY);

                Item item = stack.getItem();

                boolean isConsumable = item.isFood() || item.getRegistryEntry().isIn(ModTags.NON_FOOD_CONSUMABLES);

                if (!isConsumable) return TypedActionResult.pass(stack);

                Random random = world.getRandom();
                float chance = random.nextFloat();

                if (chance < 0.15) {
                    world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    return TypedActionResult.success(stack.copy());
                } else if (chance < 0.10) {
                    stack.decrement(1);
                    world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 0.5f);
                    return TypedActionResult.success(stack);
                }
            }
            if (TraitComponent.get(player).hasTrait(Trait.NUTRITIONAL)) {
                Item item = stack.getItem();
                boolean isConsumable = item.isFood() || item instanceof PotionItem;

                if (!isConsumable) return TypedActionResult.pass(stack);

                return TypedActionResult.fail(stack);
            }

            return TypedActionResult.pass(stack);
        });
    }
}
