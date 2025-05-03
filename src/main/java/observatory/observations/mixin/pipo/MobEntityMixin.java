package observatory.observations.mixin.pipo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MobEntityMixin {
//    @Inject(
//            method = "setTarget",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    public void preventTargeting(LivingEntity target, CallbackInfo ci) {
//        if (isNetherMob((MobEntity) (Object) this) && target instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.FAMILIARITY)) {
//            target = null;
//            ci.cancel();
//        }
//    }

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
