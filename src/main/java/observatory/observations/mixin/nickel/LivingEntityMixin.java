package observatory.observations.mixin.nickel;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    //Trait: Half Gills
    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void observations$applyConduitPower(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.HALF_GILLS)) {
            if (player.isTouchingWaterOrRain())
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 160, 0, true, true));
            else player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 160, 1, true, true));
        }
    }
}
