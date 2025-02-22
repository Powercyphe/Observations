package observatory.observations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import observatory.observations.common.util.AttributeEntityValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DefaultAttributeContainer.class)
public class DefaultAttributeContainerMixin implements AttributeEntityValue {

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


    @ModifyExpressionValue(method = "getValue", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/attribute/DefaultAttributeContainer;require(Lnet/minecraft/entity/attribute/EntityAttribute;)Lnet/minecraft/entity/attribute/EntityAttributeInstance;"))
    private EntityAttributeInstance observations$attributeEntityValue(EntityAttributeInstance original) {
        if (original != null) {
            ((AttributeEntityValue) original).observations$setEntity(this.observations$getEntity());
        }

        return original;

    }
}
