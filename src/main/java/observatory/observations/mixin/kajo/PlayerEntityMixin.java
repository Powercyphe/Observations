package observatory.observations.mixin.kajo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import observatory.observations.Observations;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import observatory.observations.common.util.TraitUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Unique private int cooldownTicks = 0;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    //Trait: Soulless Creature
    @ModifyReturnValue(method = "canFoodHeal", at = @At(value = "RETURN"))
    private boolean observations$preventRegen(boolean original) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        return original && !TraitComponent.get(player).hasTrait(Trait.SOULLESS_CREATURE);
    }

    //Trait: Photosynthesis
    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void observations$photosynthesis(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (this.cooldownTicks > 0) {
            this.cooldownTicks--;
        }
        else if (TraitComponent.get(player).hasTrait(Trait.PHOTOSYNTHESIS) && TraitUtil.isInSunlight(player) && player.age % 20 == 0) {
            HungerManager hungerManager = player.getHungerManager();

            if (hungerManager.getFoodLevel() < 20 && !this.getWorld().isClient()) {
                hungerManager.add(1, 0.25f);
                this.cooldownTicks = 40;
            }
        }
    }

    //Trait: Strong Hands
    @WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean observations$onDamageEntity(Entity entity, DamageSource source, float amount, Operation<Boolean> original) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (entity instanceof LivingEntity && TraitComponent.get(player).hasTrait(Trait.STRONG_HANDS_EVEN_STRONGER_MORALS)) {
            if (((LivingEntity) entity).getHealth() - amount <= 0.0) {
                return false;
            }
            else if (TraitUtil.isInSunlight(player)){
                amount *= 1.2f;
            }
        }
        return original.call(entity, source, amount);
    }

    //Trait: Strong Hands
    @WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean observations$onDamageLivingEntity(LivingEntity entity, DamageSource source, float amount, Operation<Boolean> original) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (TraitComponent.get(player).hasTrait(Trait.STRONG_HANDS_EVEN_STRONGER_MORALS)) {
            if (entity.getHealth() - amount <= 0.0) {
                return false;
            }
            else if (TraitUtil.isInSunlight(player)){
                amount *= 1.2f;
            }
        }
        return original.call(entity, source, amount);
    }
}
