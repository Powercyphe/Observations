package observatory.observations.mixin.client.pom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.math.RotationAxis;
import observatory.observations.mixin.accessor.BossBarHudAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderBossbarsVertically(DrawContext context, CallbackInfo ci) {
        Map<UUID, ClientBossBar> bossBars = ((BossBarHudAccessor) this).getBossBars();
        if (bossBars.isEmpty()) return;

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        int x = 20;
        int y = screenHeight / 4;

        int barWidth = 20;
        int barHeight = 300;
        int spacing = 10;

        for (ClientBossBar bossBar : bossBars.values()) {
            context.fill(x, y, x + barWidth, y + barHeight, 0x80000000);

            int progressHeight = (int)(barHeight * bossBar.getPercent());
            context.fill(x, y + barHeight - progressHeight,
                    x + barWidth, y + barHeight,
                    getBossBarColor(bossBar.getColor()));

            context.getMatrices().push();
            context.getMatrices().translate(x + barWidth + 5, y + barHeight, 0);
            context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90));
            context.drawText(client.textRenderer, bossBar.getName(), 0, 0, 0xFFFFFF, false);
            context.getMatrices().pop();

            y += barHeight + spacing;
        }

        ci.cancel();
    }

    @Unique
    private int getBossBarColor(BossBar.Color color) {
        return switch (color) {
            case PINK -> 0xFFF500FF;
            case BLUE -> 0xFF0094FF;
            case RED -> 0xFFFF0000;
            case GREEN -> 0xFF00FF00;
            case YELLOW -> 0xFFFFFF00;
            case PURPLE -> 0xFFAA00FF;
            case WHITE -> 0xFFFFFFFF;
        };
    }
}
