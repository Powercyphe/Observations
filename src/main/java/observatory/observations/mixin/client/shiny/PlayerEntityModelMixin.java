package observatory.observations.mixin.client.shiny;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import observatory.observations.common.util.TraitUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin<T extends LivingEntity> extends BipedEntityModel<T> {

    public PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @WrapOperation(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V"))
    private void observations$limitLegsAngle(PlayerEntityModel instance, T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, Operation<Void> original) {
        if (entity instanceof PlayerEntity player && TraitUtil.isWeightlessFlying(player) && player.isSprinting()) {
            limbAngle *= 0.025f;
            limbDistance *= 0.025f;
        }
        original.call(instance, entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
    }
}
