package observatory.observations.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import observatory.observations.common.util.AttributeEntityValue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(AttributeContainer.class)
public class AttributeContainerMixin implements AttributeEntityValue {

    @Shadow @Final private Map<EntityAttribute, EntityAttributeInstance> custom;
    @Shadow @Final private DefaultAttributeContainer fallback;
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

    @ModifyReturnValue(method = "getCustomInstance(Lnet/minecraft/entity/attribute/EntityAttribute;)Lnet/minecraft/entity/attribute/EntityAttributeInstance;", at = @At("RETURN"))
    private EntityAttributeInstance observations$attributeEntityValue(EntityAttributeInstance original) {
        if (original != null) {
            ((AttributeEntityValue) original).observations$setEntity(this.observations$getEntity());
        }

        return original;
    }


    @Inject(method = "getValue", at = @At("HEAD"))
    private void observations$attributeEntityValue(EntityAttribute attribute, CallbackInfoReturnable<Double> cir) {
        EntityAttributeInstance instance = this.custom.get(attribute);
        if (instance != null) {
            ((AttributeEntityValue) instance).observations$setEntity(this.observations$getEntity());
        }

        if (this.fallback != null) {
            ((AttributeEntityValue) this.fallback).observations$setEntity(this.observations$getEntity());
        }

    }
}
