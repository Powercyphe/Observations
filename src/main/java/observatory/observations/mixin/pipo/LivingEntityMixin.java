package observatory.observations.mixin.pipo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyReturnValue(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At(value = "RETURN"))
    private boolean observations$preventBeingTargeted(boolean original, LivingEntity target) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (isNetherMob(entity) && target instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.FAMILIARITY)) {
            return false;
        }
        return original;
    }

    @Unique
    private static boolean isNetherMob(Entity entity) {
        return entity instanceof BlazeEntity ||
                entity instanceof GhastEntity ||
                entity instanceof WitherSkeletonEntity ||
                entity instanceof MagmaCubeEntity ||
                entity instanceof PiglinEntity ||
                entity instanceof PiglinBruteEntity ||
                entity instanceof ZombifiedPiglinEntity ||
                entity instanceof HoglinEntity ||
                entity instanceof StriderEntity;
    }
}
