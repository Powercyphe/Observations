package observatory.observations.mixin.pipo;

import net.fabricmc.fabric.impl.biome.modification.BuiltInRegistryKeys;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    public final RegistryKey<World> THE_NETHER = RegistryKey.of(
            RegistryKeys.WORLD,
            new Identifier("minecraft", "the_nether")
    );

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void observations$removeLavaDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if ((LivingEntity) (Object) this instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.MAGMA_COVERED)) {
            cir.setReturnValue(source.isIn(DamageTypeTags.IS_FIRE));
        }
    }

    @Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At(value = "RETURN"), cancellable = true)
    private void observations$preventBeingTargeted(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof HostileEntity && entity.getWorld().getRegistryKey().equals(THE_NETHER) && target instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.FAMILIARITY)) {
            System.out.println("falseing iy");
            cir.setReturnValue(false);
        }
        else cir.setReturnValue(cir.getReturnValue());
    }
}
