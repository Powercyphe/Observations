package observatory.observations.mixin.shiny;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import observatory.observations.Observations;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    private void observations$preventItemUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack stack = user.getStackInHand(hand);

        if (TraitComponent.get(user).hasTrait(Trait.LIKE_VOID) && stack.isIn(Observations.PROJECTILE_WEAPONS)) {
            cir.setReturnValue(TypedActionResult.fail(stack));
        }
    }

    //Get this to work; the item is correctly prevented from being used, but usageTick is still called (idk what magic apoli is using but there is no modification of usageTick there???)
    @WrapWithCondition(method = "usageTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;usageTick(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;I)V"))
    private boolean observations$preventItemUsageTick(Item instance, World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof PlayerEntity && TraitComponent.get((PlayerEntity) user).hasTrait(Trait.LIKE_VOID) && stack.isIn(Observations.PROJECTILE_WEAPONS)) {
            Observations.LOGGER.info("Can't use this ranged weapon!");
            return false;
        }
        else return true;
    }
}
