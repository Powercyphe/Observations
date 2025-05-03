package observatory.observations.common.component;

import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import observatory.observations.common.util.TraitUtil;
import org.jetbrains.annotations.NotNull;

public class WillToLiveOnComponent implements AutoSyncedComponent, CommonTickingComponent {
    public int lastHealthTier = 20;
    public PlayerEntity player;

    public WillToLiveOnComponent(PlayerEntity player) {
        this.player = player;
    }

    public static WillToLiveOnComponent get(@NotNull PlayerEntity player) {
        return ModComponents.WILL.get(player);
    }

    public void sync() {
        ModComponents.WILL.sync(player);
    }

    @Override
    public void tick() {
        if (TraitComponent.get(player).hasTrait(Trait.WILL_TO_LIVE_ON)) {
            float health = MathHelper.clamp(player.getHealth(), 1.0f, 20.0f);
            double DIG_SPEED = AdditionalEntityAttributes.DIG_SPEED.getDefaultValue();
            double MOVEMENT_SPEED = EntityAttributes.GENERIC_MOVEMENT_SPEED.getDefaultValue();
            double ATTACK_DAMAGE = EntityAttributes.GENERIC_ATTACK_DAMAGE.getDefaultValue();
            double ATTACK_SPEED = EntityAttributes.GENERIC_ATTACK_SPEED.getDefaultValue();

            double addition = health * 0.04;

            TraitUtil.setBaseModifier(player, AdditionalEntityAttributes.DIG_SPEED, (addition + DIG_SPEED));
            TraitUtil.setBaseModifier(player, EntityAttributes.GENERIC_MOVEMENT_SPEED, (addition + MOVEMENT_SPEED));
            TraitUtil.setBaseModifier(player, EntityAttributes.GENERIC_ATTACK_DAMAGE, (addition + ATTACK_DAMAGE));
            TraitUtil.setBaseModifier(player, EntityAttributes.GENERIC_ATTACK_SPEED, (addition + ATTACK_SPEED));
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.lastHealthTier = nbtCompound.getInt("lastHealthLevel");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt("lastHealthLevel", lastHealthTier);
    }
}
