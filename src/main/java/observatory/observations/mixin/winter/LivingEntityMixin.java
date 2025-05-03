package observatory.observations.mixin.winter;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import observatory.observations.common.util.TraitUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Shadow private int jumpingCooldown;

    @Shadow protected abstract void jump();

    @Inject(method = "getNextAirUnderwater", at = @At("HEAD"), cancellable = true)
    protected void getNextAirUnderwater(int air, CallbackInfoReturnable<Integer> cir) {
        LivingEntity living = (LivingEntity) (Object) this;
        int i = EnchantmentHelper.getRespiration(living);
        if (living.getEyeY() >= 200 && living instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.SMOKED_LUNGS)) {
            cir.setReturnValue(i > 0 && living.getRandom().nextInt(i + 1) > 0 ? air : air - 2);
        }
    }

    @Inject(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V", shift = At.Shift.AFTER))
    private void extraSaturation(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        LivingEntity living = (LivingEntity) (Object) this;

        boolean hasPlate = living instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.PRIVILEGED_PLATES) && TraitUtil.isWearingItem(player, Items.GOLDEN_HELMET);

        if (hasPlate && living instanceof PlayerEntity player) {
            player.getHungerManager().add(0, 2.0f);
        }
    }

    @Unique
    private int ticksFalling = 0;
    @Unique
    private boolean hasJumped = false;

    @Inject(method = "jump", at = @At(value = "HEAD"))
    private void coyote_time_setHasJumped(CallbackInfo ci) {
        hasJumped = true;
    }

    @Inject(method = "tickMovement", at = @At(value = "HEAD"))
    private void coyote_time_countTicksFalling(CallbackInfo ci) {
        if (!this.isOnGround()) {
            ticksFalling++;
        } else {
            ticksFalling = 0;
            hasJumped = false;
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void observations$no_longer_flesh(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity player) {
            if (TraitComponent.get(player).hasTrait(Trait.PRIVILEGED_PLATES) && player.isTouchingWater() && player.isOnGround() && player.jumping && player.jumpingCooldown == 0 && TraitUtil.isWearingItem(player, Items.GOLDEN_CHESTPLATE)) {
                player.jump();
                player.jumpingCooldown = 10;
            }
        }
    }

    @Inject(method = "tickMovement", at = @At(target = "Lnet/minecraft/entity/LivingEntity;getSwimHeight()D", value = "INVOKE", shift = At.Shift.AFTER))
    private void coyote_time_coyoteTimeJump(CallbackInfo ci) {
        if (this.ticksFalling <= 60 && !this.hasJumped && (LivingEntity) (Object) this instanceof PlayerEntity player && TraitComponent.get(player).hasTrait(Trait.PRIVILEGED_PLATES) && TraitUtil.isWearingItem(player, Items.GOLDEN_BOOTS)) {
            this.jump();
            this.jumpingCooldown = 60;
        }
    }
}
