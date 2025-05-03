package observatory.observations.common.util;

import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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
    private static final UUID DIG_SPEED_MODIFIER_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID MOVE_SPEED_MODIFIER_UUID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID ATTACK_DAMAGE_MODIFIER_UUID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("44444444-4444-4444-4444-444444444444");

    public static Pair<EntityAttribute, EntityAttributeModifier> createModifier(EntityAttribute attribute, EntityAttributeModifier.Operation operation, double value) {
        return new Pair<>(attribute, new EntityAttributeModifier(UUID.randomUUID(), Text.translatable(attribute.getTranslationKey()).getString() + "Modifier", value, operation));
    }

    public static void resetAttribute(PlayerEntity entity, EntityAttribute attribute) {
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

    public static void setBaseModifier(PlayerEntity entity, EntityAttribute attribute, double multiplier) {
        UUID uuid = getUUIDForAttribute(attribute);
        if (uuid == null) return;

        var instance = entity.getAttributeInstance(attribute);
        if (instance == null) return;

        EntityAttributeModifier existing = instance.getModifier(uuid);
        if (existing != null) {
            instance.removeModifier(existing);
        }

        EntityAttributeModifier modifier = new EntityAttributeModifier(
                uuid,
                attribute.getTranslationKey() + "_modifier",
                multiplier - 1.0,
                EntityAttributeModifier.Operation.MULTIPLY_BASE
        );

        instance.addPersistentModifier(modifier);
    }

    public static final LootContextParameter<Integer> LOOTING_ENCHANTMENT =
            new LootContextParameter<>(new Identifier("looting_modifier"));

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

    private static UUID getUUIDForAttribute(EntityAttribute attribute) {
        if (attribute == AdditionalEntityAttributes.DIG_SPEED) return DIG_SPEED_MODIFIER_UUID;
        if (attribute == EntityAttributes.GENERIC_MOVEMENT_SPEED) return MOVE_SPEED_MODIFIER_UUID;
        if (attribute == EntityAttributes.GENERIC_ATTACK_DAMAGE) return ATTACK_DAMAGE_MODIFIER_UUID;
        if (attribute == EntityAttributes.GENERIC_ATTACK_SPEED) return ATTACK_SPEED_MODIFIER_UUID;
        return null;
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
