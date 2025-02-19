package observatory.observations.mixin.vann1e_;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import observatory.observations.component.BuddingComponent;
import observatory.observations.component.TraitComponent;
import observatory.observations.registry.ModComponents;
import observatory.observations.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "applyDamage", at = @At("HEAD"), index = 2, argsOnly = true)
    private float observations$geode_soul(float amount, DamageSource source) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (TraitComponent.get(player).hasTrait(Trait.GEODE_SOUL)) {
            amount = amount / 2;
            if (ModComponents.BUDDING.get(this).buddingLevel > 0
                    && source.getAttacker() != null && !player.isInvulnerableTo(source)) {
                BuddingComponent.get(player).decreaseBuddingLevel();
                amount = 0;
            }
        }
        return amount;
    }

    @ModifyVariable(method = "applyDamage", at = @At("HEAD"), index = 2, argsOnly = true)
    private float observations$obvious_cracks(float amount, DamageSource source) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (TraitComponent.get(player).hasTrait(Trait.OBVIOUS_CRACKS)) {
            if (source.getAttacker() instanceof LivingEntity entity && entity.getMainHandStack().isIn(ItemTags.PICKAXES)) {
                StatusEffectInstance instance = new StatusEffectInstance(StatusEffects.SLOWNESS, 70, 0, false, false, true);
                player.addStatusEffect(instance, source.getAttacker());

                if (!player.getWorld().isClient()) {
                    ((ServerWorld) player.getWorld()).spawnParticles(ParticleTypes.FIREWORK,
                            player.getX(), player.getY() + player.getHeight() / 2, player.getZ(), 7, 0, 0, 0, 0.05);
                }
                return amount * 2;
            }
        }
        return amount;
    }

}
