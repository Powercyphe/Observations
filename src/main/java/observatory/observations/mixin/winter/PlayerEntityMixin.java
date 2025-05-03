package observatory.observations.mixin.winter;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private float currentSpeedMultiplier = 1.0f;
    @Unique
    public final float MAX_SPEED = 1.5f;
    @Unique
    public final float SPEED_INCREMENT = 0.01f;


    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;

        if (!player.getWorld().isClient) return;

        boolean canSpeedUp = player.isSprinting() && !player.getWorld().getBlockState(player.getBlockPos().down()).isOf(Blocks.AIR) && !player.getWorld().getBlockState(player.getBlockPos().down()).isOf(Blocks.WATER)
                && TraitComponent.get(player).hasTrait(Trait.PRIVILEGED_PLATES) && TraitUtil.isWearingItem(player, Items.GOLDEN_LEGGINGS);

        if (canSpeedUp) {
            currentSpeedMultiplier += SPEED_INCREMENT;
            if (currentSpeedMultiplier > MAX_SPEED) {
                currentSpeedMultiplier = MAX_SPEED;
            }
        } else {
            currentSpeedMultiplier = 1.0f;
        }

        Vec3d velocity = player.getVelocity();
        Vec3d movement = new Vec3d(velocity.x * currentSpeedMultiplier,
                velocity.y,
                velocity.z * currentSpeedMultiplier);
        player.setVelocity(movement);
    }

    @ModifyReturnValue(method = "shouldSwimInFluids", at = @At("RETURN"))
    private boolean observations$no_longer_flesh(boolean original) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (TraitComponent.get(player).hasTrait(Trait.PRIVILEGED_PLATES) && player.isTouchingWater() && TraitUtil.isWearingItem(player, Items.GOLDEN_CHESTPLATE)) {
            return false;
        }
        return original;
    }

    @Override
    public void swimUpward(TagKey<Fluid> fluid) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (TraitComponent.get(player).hasTrait(Trait.PRIVILEGED_PLATES) && player.isTouchingWater() && TraitUtil.isWearingItem(player, Items.GOLDEN_CHESTPLATE)) {
            return;
        }
        super.swimUpward(fluid);
    }
}