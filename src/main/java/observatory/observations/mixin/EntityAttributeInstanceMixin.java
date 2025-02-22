package observatory.observations.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import observatory.observations.common.util.AttributeEntityValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Mixin(EntityAttributeInstance.class)
public class EntityAttributeInstanceMixin implements AttributeEntityValue {

    @Unique
    private Entity entity;

    @Override
    public Entity observations$getEntity() {
        return this.entity;
    }

    @Override
    public void observations$setEntity(Entity entity) {
        this.entity = entity;
    }

    @ModifyReturnValue(method = "getModifiers()Ljava/util/Set;", at = @At("RETURN"))
    private Set<EntityAttributeModifier> observations$traitModifiers(Set<EntityAttributeModifier> original) {
        EntityAttributeInstance instance = (EntityAttributeInstance) (Object) this;
        Set<EntityAttributeModifier> modifiers = new java.util.HashSet<>(Set.copyOf(original));

        if (this.entity instanceof PlayerEntity player) {
            TraitComponent component = TraitComponent.get(player);
            for (Trait trait : component.getTraits()) {
                EntityAttributeModifier modifier = trait.getModifierFor(instance.getAttribute());
                if (modifier != null) {
                    modifiers.add(modifier);
                }
            }
        }
        return ImmutableSet.copyOf(modifiers);
    }

    @ModifyReturnValue(method = "getModifier", at = @At("RETURN"))
    private EntityAttributeModifier observations$traitModifiers(EntityAttributeModifier original, UUID id) {
        EntityAttributeInstance instance = (EntityAttributeInstance) (Object) this;

        if (this.entity instanceof PlayerEntity player) {
            TraitComponent component = TraitComponent.get(player);
            for (Trait trait : component.getTraits()) {
                EntityAttributeModifier modifier = trait.getModifierFor(instance.getAttribute());
                if (modifier != null && modifier.getId() == id) {
                    return modifier;
                }
            }
        }
        return original;
    }

}
