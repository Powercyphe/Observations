package observatory.observations.common.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import observatory.observations.mixin.accessor.EntityAccessor;

import java.util.List;
import java.util.UUID;

public class TraitUtil {
    public static Pair<EntityAttribute, EntityAttributeModifier> createModifier(EntityAttribute attribute, EntityAttributeModifier.Operation operation, double value) {
        return new Pair<>(attribute, new EntityAttributeModifier(UUID.randomUUID(), Text.translatable(attribute.getTranslationKey()).getString() + "Modifier", value, operation));
    }

    public static void resetAttribute(LivingEntity entity, EntityAttribute attribute) {
        var instance = entity.getAttributeInstance(attribute);
        if (instance != null) {
            for (EntityAttributeModifier modifier : List.copyOf(instance.getModifiers())) {
                instance.removeModifier(modifier);
            }

            instance.setBaseValue(attribute.getDefaultValue());
        }
    }

    public static Pair<EntityAttribute, EntityAttributeModifier> additionModifier(EntityAttribute attribute, double value) {
        return createModifier(attribute, EntityAttributeModifier.Operation.ADDITION, value);
    }

    public static Pair<EntityAttribute, EntityAttributeModifier> multiplyBaseModifier(EntityAttribute attribute, double value) {
        return createModifier(attribute, EntityAttributeModifier.Operation.MULTIPLY_BASE, value);
    }

    public static Pair<EntityAttribute, EntityAttributeModifier> multiplyTotalModifier(EntityAttribute attribute, double value) {
        return createModifier(attribute, EntityAttributeModifier.Operation.MULTIPLY_TOTAL, value);
    }

    public static boolean isWeightlessFlying(PlayerEntity player) {
        if (player != null && ModComponents.TRAIT.getNullable(player) != null) {
            return TraitComponent.get(player).hasTrait(Trait.WEIGHTLESS)
                    && !player.isSwimming()
                    && !player.isUsingRiptide()
                    && !player.isFallFlying()
                    && (!player.verticalCollision || player.getPitch() < 0.0);
        }
        return false;
    }

    public static boolean isWearingItem(PlayerEntity player, Item item) {
        for (ItemStack stack : player.getArmorItems()) {
            if (stack.getItem() == item) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInSunlight(PlayerEntity player) {
        if (player.getWorld().isClient) player.getWorld().calculateAmbientDarkness();

        BlockPos blockPos = BlockPos.ofFloored(player.getX(), player.getBoundingBox().maxY, player.getZ());
        return player.getWorld().isDay()
                && player.getWorld().getBrightness(blockPos) > 0.5
                && player.getWorld().isSkyVisible(blockPos)
                && !((EntityAccessor) player).observations$isBeingRainedOn();
    }
}
