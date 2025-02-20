package observatory.observations.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.common.registry.Trait;
import org.jetbrains.annotations.NotNull;

public class BuddingComponent implements AutoSyncedComponent, CommonTickingComponent {
    public static String BUDDING_LEVEL_KEY = "budding_level";
    public static int MAX_BUDDING_LEVEL = 4;

    public static String COOLDOWN_KEY = "cooldown_key";
    public static int COOLDOWN_TICKS = 140;

    public PlayerEntity player;
    public int buddingLevel = 0;
    public int cooldown = 0;

    public BuddingComponent(PlayerEntity player) {
        this.player = player;
    }

    public static BuddingComponent get(@NotNull PlayerEntity player) {
        return ModComponents.BUDDING.get(player);
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.buddingLevel = nbtCompound.getInt(BUDDING_LEVEL_KEY);
        this.cooldown = nbtCompound.getInt(COOLDOWN_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putInt(BUDDING_LEVEL_KEY, this.buddingLevel);
        nbtCompound.putInt(COOLDOWN_KEY, this.cooldown);
    }

    public void sync() {
        ModComponents.BUDDING.sync(this.player);
    }

    @Override
    public void tick() {
        if (TraitComponent.get(this.player).hasTrait(Trait.GEODE_SOUL)) {
            if (isStandingOn(Blocks.BUDDING_AMETHYST) && this.buddingLevel < MAX_BUDDING_LEVEL) {
                this.cooldown--;
                if (this.cooldown <= 0) {
                    this.cooldown = COOLDOWN_TICKS;
                    this.increaseBuddingLevel();
                }
            } else if (this.cooldown != COOLDOWN_TICKS) {
                this.cooldown = COOLDOWN_TICKS;
            }
        }
    }

    public void increaseBuddingLevel() {
        if (this.buddingLevel < MAX_BUDDING_LEVEL) {
            this.player.getWorld().playSound(null, this.player.getBlockPos(), SoundEvents.BLOCK_AMETHYST_CLUSTER_PLACE, SoundCategory.PLAYERS, 1f, 0.65f);
            if (!this.player.getWorld().isClient()) {
                BlockStateParticleEffect particle = new BlockStateParticleEffect(ParticleTypes.BLOCK, this.getBlock().getDefaultState());
                ((ServerWorld) this.player.getWorld()).spawnParticles(particle,
                        this.player.getX(), this.player.getY() + this.player.getHeight() / 2, this.player.getZ(), 14, 0, 0, 0, 3);
                ((ServerWorld) this.player.getWorld()).spawnParticles(ParticleTypes.END_ROD,
                        this.player.getX(), this.player.getY() + this.player.getHeight() / 2, this.player.getZ(), 7, 0, 0, 0, 0.05);

            }

            this.buddingLevel++;
        }
        sync();
    }

    public void decreaseBuddingLevel() {
        if (this.buddingLevel > 0) {
            this.player.getWorld().playSound(null, this.player.getBlockPos(), SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.PLAYERS, 1f, 1.35f);
            if (!this.player.getWorld().isClient()) {
                BlockStateParticleEffect particle = new BlockStateParticleEffect(ParticleTypes.BLOCK, this.getBlock().getDefaultState());
                ((ServerWorld) this.player.getWorld()).spawnParticles(particle,
                        this.player.getX(), this.player.getY() + this.player.getHeight() / 2, this.player.getZ(), 14, 0, 0, 0, 3);
                ((ServerWorld) this.player.getWorld()).spawnParticles(ParticleTypes.END_ROD,
                        this.player.getX(), this.player.getY() + this.player.getHeight() / 2, this.player.getZ(), 7, 0, 0, 0, 0.15);
            }

            this.buddingLevel--;
        }
        sync();
    }

    public boolean isStandingOn(Block block) {
        return player.getSteppingBlockState().isOf(block);
    }

    public Block getBlock() {
        return switch (this.buddingLevel) {
            case 1 -> Blocks.SMALL_AMETHYST_BUD;
            case 2 -> Blocks.MEDIUM_AMETHYST_BUD;
            case 3 -> Blocks.LARGE_AMETHYST_BUD;
            case 4 -> Blocks.AMETHYST_CLUSTER;
            default -> Blocks.AIR;
        };
    }

}
