package observatory.observations.mixin.client.qol;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

import static net.minecraft.client.gui.screen.ingame.InventoryScreen.drawEntity;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin extends HandledScreen<PlayerScreenHandler> {
    public InventoryScreenMixin(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        PlayerEntity player = client.player;

        int i = this.x;
        int j = this.y;
        context.drawTexture(BACKGROUND_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        drawEntity(context, i + 51, j + 75, 30, (float)(i + 51) - mouseX, (float)(j + 75 - 50) - mouseY, player);

        List<Trait> traits = TraitComponent.get(player).getTraits();

        if (traits.isEmpty()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        client.getTextureManager().bindTexture(new Identifier("observations", "textures/gui/traits.png"));

        int x = this.x + this.backgroundWidth - 202;
        int y = this.y + 2;

        for (int iteration = 0; iteration < traits.size(); iteration++) {
            Trait trait = traits.get(iteration);

            int iconX = x + 4;
            int iconY = y + iteration * 26;

            context.drawTexture(
                    new Identifier("observations", "textures/gui/traits/background.png"),
                    iconX - 6, iconY - 2,
                    0, 0, 24, 24,
                    24, 24
            );

            context.drawTexture(
                    new Identifier("observations", "textures/gui/traits/" + trait.getId() + ".png"),
                    iconX - 2, iconY + 2,
                    0, 0, 16, 16,
                    16, 16
            );

            if (mouseX >= iconX && mouseX <= iconX + 16 && mouseY >= iconY && mouseY <= iconY + 16) {
                context.drawTooltip(client.textRenderer, Text.of(trait.formatTraitName(trait.toString())), mouseX, mouseY);
            }
        }
    }
}
