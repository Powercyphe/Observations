package observatory.observations.mixin.nopeable;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import observatory.observations.common.util.TraitUtil;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @WrapMethod(method = "dropLoot")
    private void injectConstantLooting(DamageSource damageSource, boolean causedByPlayer, Operation<Void> original) {
        LivingEntity entity = (LivingEntity)(Object)this;

        if (entity instanceof PlayerEntity player) {
            if (TraitComponent.get(player).hasTrait(Trait.PICKPOCKET)) {
                Identifier identifier = entity.getLootTable();
                LootTable lootTable = entity.getWorld().getServer().getLootManager().getLootTable(identifier);
                LootContextParameterSet.Builder builder = (new LootContextParameterSet.Builder((ServerWorld)entity.getWorld())).add(LootContextParameters.THIS_ENTITY, entity).add(LootContextParameters.ORIGIN, entity.getPos()).add(LootContextParameters.DAMAGE_SOURCE, damageSource).addOptional(LootContextParameters.KILLER_ENTITY, damageSource.getAttacker()).addOptional(LootContextParameters.DIRECT_KILLER_ENTITY, damageSource.getSource());

                LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
                lootTable.generateLoot(lootContextParameterSet, entity.getLootTableSeed(), entity::dropStack);
                lootTable.generateLoot(lootContextParameterSet, entity.getLootTableSeed(), entity::dropStack);
            }
            original.call(damageSource, causedByPlayer);
        }
        original.call(damageSource, causedByPlayer);
    }
}
