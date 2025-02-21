package observatory.observations.mixin.client.shiny;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import observatory.observations.Observations;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> implements ModelWithArms, ModelWithHead {

    @Shadow @Final public ModelPart rightLeg;

    @Shadow @Final public ModelPart leftLeg;

    @Shadow public float leaningPitch;

//    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At(value = "TAIL"))
//    private void observations$setFlyingPose(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
//        if (livingEntity instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.INFINITE_FREEDOM) && !player.groundCollision) {
//            //Observations.LOGGER.info("Modifying player pose!");
//            float k = (float) livingEntity.getVelocity().lengthSquared();
//            k /= 0.2F;
//            k *= k * k;
//
//            if (k < 1.0f) {
//                k = 1.0f;
//            }
//
//            this.rightLeg.pitch = MathHelper.cos(f * 0.6662f) * 1.4f * g / k;
//            this.leftLeg.pitch = MathHelper.cos(f * 0.6662f + (float) Math.PI) * 1.4f * g / k;
//            //this.leftLeg.pitch = MathHelper.lerp(this.leaningPitch, this.leftLeg.pitch, 0.3f * MathHelper.cos(f * 0.33333334f + (float) Math.PI));
//            //this.rightLeg.pitch = MathHelper.lerp(this.leaningPitch, this.rightLeg.pitch, 0.3f * MathHelper.cos(f * 0.33333334f));
//        }
//    }
}
