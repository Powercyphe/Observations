package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.block.Blocks;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.world.World;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import observatory.observations.common.util.TraitUtil;

public class PrivilegedComponent implements AutoSyncedComponent, CommonTickingComponent {
    public PlayerEntity player;
    public int ticks = 0;
    boolean cooldown = false;

    public PrivilegedComponent(PlayerEntity player) {
        this.player = player;
    }

    /*public static PrivilegedComponent get(PlayerEntity player) {
        return ModComponents.PRIVILEGED.get(player);
    }

    public void sync() {
        ModComponents.PRIVILEGED.sync(player);
    }*/

    @Override
    public void tick() {
        if (TraitComponent.get(player).hasTrait(Trait.PRIVILEGED_PLATES)) {
            World world = player.getWorld();

            if (TraitUtil.isWearingItem(player, Items.GOLDEN_HELMET)) {

            } if (TraitUtil.isWearingItem(player, Items.GOLDEN_CHESTPLATE)) {
                if (player.isSubmergedIn(FluidTags.WATER) || player.isSubmergedIn(FluidTags.LAVA)) {
                    player.addVelocity(player.getVelocity().x, player.getVelocity().y - 0.5, player.getVelocity().z);
                }
            } if (TraitUtil.isWearingItem(player,Items.GOLDEN_LEGGINGS)) {
                TraitUtil.multiplyBaseModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25);
            } if (TraitUtil.isWearingItem(player,Items.GOLDEN_BOOTS)) {
                System.out.println(ticks);
                if (player.getVelocity().y < 0 && !player.isOnGround()) {
                    if (!cooldown) {
                        player.setNoGravity(true);
                        ticks++;
                    }
                }
                if (ticks > 40) {
                    player.setNoGravity(false);
                    ticks = 0;
                    cooldown = true;
                } else {
                    cooldown = false;
                }
            }
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {}

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {}
}
