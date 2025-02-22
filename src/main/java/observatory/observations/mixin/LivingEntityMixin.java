package observatory.observations.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import observatory.observations.common.util.AttributeEntityValue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyReturnValue(method = "getAttributes", at = @At("TAIL"))
    private AttributeContainer observations$attributeEntityValue(AttributeContainer original) {
        if (original != null) {
            ((AttributeEntityValue) original).observations$setEntity((Entity) (Object) this);
        }
        return original;
    }


}
