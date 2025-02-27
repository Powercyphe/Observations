package observatory.observations.mixin.rafsa;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.world.World;
import observatory.observations.Observations;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    //Trait: Infection
    @WrapOperation(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean observations$modifyDamageTaken(PlayerEntity player, DamageSource source, float amount, Operation<Boolean> original) {
        if (TraitComponent.get(player).hasTrait(Trait.INFECTION)) {
            if (source.isIn(DamageTypeTags.IS_FIRE)) amount *= 1.25f;
            else if (source.isOf(DamageTypes.FREEZE)) amount *= 0.75f;
        }
        return original.call(player, source, amount);
    }
}
