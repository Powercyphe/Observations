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
        if (TraitComponent.get(client.player).hasTrait(Trait.LINE_OF_SIGHT) && client.player.getUuidAsString().equals("765ad8ec-ebe5-4754-ab33-a876ac783e6d")) {
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
}
