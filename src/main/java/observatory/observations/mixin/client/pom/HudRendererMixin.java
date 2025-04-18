package observatory.observations.mixin.client.pom;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class HudRendererMixin {
    @Shadow private int scaledHeight;
    @Shadow private int scaledWidth;

    @Unique private static final Identifier HOTBAR_TEXTURE = new Identifier("observations", "textures/gui/pom_hotbar.png");
    @Unique private static final Identifier OVERLAY_TEXTURE = new Identifier("observations", "textures/gui/pom_overlay.png");

    @Unique private final MinecraftClient client = MinecraftClient.getInstance();

    @Unique private static final int BAR_COLOR = 0xFFE4002F;

    @Unique
    private float lastHealth = 0;
    @Unique
    private long lastHealTime = 0;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderCustomHud(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (TraitComponent.get(client.player).hasTrait(Trait.LINE_OF_SIGHT) && client.player.getUuidAsString().equals("70b69747-5f11-4c80-b7a3-b39a56ea7327")) {
            renderOverlay(context);

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            float health = client.player.getHealth();
            float maxHealth = client.player.getMaxHealth();
            float healthPercentage = health / maxHealth;

            int barWidth = 720;
            int barHeight = 9;
            int x = (client.getWindow().getScaledWidth() - barWidth) / 2;
            int y = 0;

            boolean isHealing = health > lastHealth;
            if (isHealing) {
                lastHealTime = System.currentTimeMillis();
            }
            lastHealth = health;

            int filledWidth = (int) (barWidth * healthPercentage);
            context.fill(x, y, x + filledWidth, y + barHeight, BAR_COLOR);
            context.fill(x, y, x + filledWidth, y + barHeight - 8, 0xcd0034);

            boolean shouldFlash = System.currentTimeMillis() - lastHealTime < 200;
            if (shouldFlash) {
                context.fill(x, y, x + filledWidth, y + barHeight, 0xAAFFFFFF);
            }
        }
    }

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    private void removeHearts(DrawContext context, CallbackInfo ci) {
        if (TraitComponent.get(client.player).hasTrait(Trait.LINE_OF_SIGHT)) {
            ci.cancel();
        }
    }

    @Unique
    private void renderOverlay(DrawContext context) {
        int screenWidth = this.scaledWidth;
        int screenHeight = this.scaledHeight;
        int originalImageWidth = 320;
        int originalImageHeight = 180;

        float scaleX = (float) screenWidth / (originalImageWidth * 4);
        float scaleY = (float) screenHeight / (originalImageHeight * 4);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        context.getMatrices().push();
        context.getMatrices().translate(0.0F, 0.0F, -90.0F);
        context.getMatrices().scale(scaleX * 2, scaleY * 2, 1.0F);

        context.drawTexture(OVERLAY_TEXTURE, 0, 0, 0, 0, originalImageWidth, originalImageHeight, originalImageWidth, originalImageHeight);

        context.getMatrices().pop();

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void renderCustomHotbar(float tickDelta, DrawContext context, CallbackInfo ci) {
        if (TraitComponent.get(client.player).hasTrait(Trait.LINE_OF_SIGHT)) {
            PlayerEntity playerEntity = client.player;
            if (playerEntity != null) {
                ItemStack itemStack = playerEntity.getOffHandStack();
                Arm arm = playerEntity.getMainArm().getOpposite();

                int hotbarWidth = 182;
                int slotWidth = 20;
                int hotbarX = scaledWidth / 2 - hotbarWidth / 2;
                int hotbarY = scaledHeight - 20;

                context.getMatrices().push();
                context.getMatrices().translate(0.0F, 0.0F, -90.0F);

                context.drawTexture(HOTBAR_TEXTURE, hotbarX, hotbarY + 1, 0, 0, hotbarWidth, 22);

                int selectedSlot = playerEntity.getInventory().selectedSlot;
                context.drawTexture(HOTBAR_TEXTURE, hotbarX + selectedSlot * slotWidth, hotbarY, 0, 22, 24, 22);

                if (!itemStack.isEmpty()) {
                    if (arm == Arm.LEFT) {
                        context.drawTexture(HOTBAR_TEXTURE, hotbarX - 29, hotbarY, 24, 22, 29, 24);
                    } else {
                        context.drawTexture(HOTBAR_TEXTURE, hotbarX + hotbarWidth, hotbarY, 53, 22, 29, 24);
                    }
                }
                context.draw();
                context.getMatrices().pop();

                int l = 1;
                for (int m = 0; m < 9; ++m) {
                    int slotX = hotbarX + m * slotWidth + 3;
                    int slotY = hotbarY + 4;
                    this.renderHotbarItem(context, slotX, slotY, tickDelta, playerEntity, playerEntity.getInventory().main.get(m), l++);
                }

                if (!itemStack.isEmpty()) {
                    int offhandSlotY = hotbarY - 3;
                    if (arm == Arm.LEFT) {
                        this.renderHotbarItem(context, hotbarX - 26, offhandSlotY, tickDelta, playerEntity, itemStack, l++);
                    } else {
                        this.renderHotbarItem(context, hotbarX + hotbarWidth + 10, offhandSlotY, tickDelta, playerEntity, itemStack, l++);
                    }
                }

                RenderSystem.enableBlend();
                if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.HOTBAR) {
                    float f = this.client.player.getAttackCooldownProgress(0.0F);
                    if (f < 1.0F) {
                        int n = scaledHeight - 20;
                        int o = hotbarX + hotbarWidth + 6;
                        if (arm == Arm.RIGHT) {
                            o = hotbarX - 91 - 22;
                        }

                        int p = (int)(f * 19.0F);
                    }
                }

                RenderSystem.disableBlend();
            }
            ci.cancel();
        }
    }

    @Unique
    private void renderHotbarItem(DrawContext context, int x, int y, float f, PlayerEntity player, ItemStack stack, int seed) {
        if (!stack.isEmpty()) {
            float g = (float)stack.getBobbingAnimationTime() - f;
            if (g > 0.0F) {
                float h = 1.0F + g / 5.0F;
                context.getMatrices().push();
                context.getMatrices().translate((float)(x + 8), (float)(y + 12), 0.0F);
                context.getMatrices().scale(1.0F / h, (h + 1.0F) / 2.0F, 1.0F);
                context.getMatrices().translate((float)(-(x + 8)), (float)(-(y + 12)), 0.0F);
            }

            context.drawItem(player, stack, x, y, seed);
            if (g > 0.0F) {
                context.getMatrices().pop();
            }

            context.drawItemInSlot(this.client.textRenderer, stack, x, y);
        }
    }
}
