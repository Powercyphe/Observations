package observatory.observations.common.util;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import observatory.observations.mixin.accessor.EntityAccessor;

import java.util.UUID;

public class TraitUtil {

    public static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("2dae7af8-f1d4-40a2-aaf4-073cabf1a98a");
    public static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("d4bf65d9-d226-4b71-b7b2-b2d7c052b2ff");
    public static final UUID ATTACK_KNOCKBACK_MODIFIER = UUID.fromString("a20c2a71-1071-49f5-b603-41f4642ee834");
    public static final UUID MOVEMENT_SPEED_MODIFIER = UUID.fromString("dde6dbe9-732b-4c85-8e51-b033d9446b43");
    public static final UUID ARMOR_MODIFIER = UUID.fromString("7a8fb222-7e95-48b9-9bfe-b17d47b04089");
    public static final UUID ARMOR_TOUGHNESS_MODIFIER = UUID.fromString("e3c9c6c5-39eb-4974-a052-5f4f0b123f0a");
    public static final UUID KNOCKBACK_RESISTANCE_MODIFIER = UUID.fromString("9d0eff0e-3ca7-4f24-8b88-832a973e3b87");

    public static Pair<EntityAttribute, EntityAttributeModifier> createModifier(EntityAttribute attribute, EntityAttributeModifier.Operation operation, double value) {
        return new Pair<>(attribute, new EntityAttributeModifier(UUID.randomUUID(), Text.translatable(attribute.getTranslationKey()).getString() + "Modifier", value, operation));
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

    public static boolean isInSunlight(PlayerEntity player) {
        if (player.getWorld().isClient) player.getWorld().calculateAmbientDarkness();

        BlockPos blockPos = BlockPos.ofFloored(player.getX(), player.getBoundingBox().maxY, player.getZ());
        return player.getWorld().isDay()
                && player.getWorld().getBrightness(blockPos) > 0.5
                && player.getWorld().isSkyVisible(blockPos)
                && !((EntityAccessor) player).observations$isBeingRainedOn();
    }
}
