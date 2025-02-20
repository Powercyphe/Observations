package observatory.observations.client.render.feature;

import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import observatory.observations.common.component.BuddingComponent;
import observatory.observations.common.registry.ModComponents;
import observatory.observations.mixin.accessor.ItemRendererAccessor;

public class BuddingFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
    private ItemRenderer itemRenderer;
    private BlockRenderManager blockRenderManager;

    public BuddingFeatureRenderer(FeatureRendererContext<T, M> context, ItemRenderer itemRenderer, BlockRenderManager blockRenderManager) {
        super(context);
        this.itemRenderer = itemRenderer;
        this.blockRenderManager = blockRenderManager;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        BuddingComponent buddingComponent = ModComponents.BUDDING.get(entity);
        Block block = buddingComponent.getBlock();
        if (block instanceof AmethystClusterBlock) {
            matrixStack.push();
            matrixStack.peek().getPositionMatrix().translate(0f, (entity.getHeight() / 6), -0.03f);
            BlockState blockState = block.getDefaultState();
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(170));
            matrixStack.scale(0.9f, 0.9f, 0.9f);
            matrixStack.translate(-0.5F, -0.5F, -0.5F);
            BakedModel model = this.blockRenderManager.getModel(blockState);
            model.getTransformation().getTransformation(ModelTransformationMode.NONE).apply(false, matrixStack);
            RenderLayer renderLayer = RenderLayers.getEntityBlockLayer(blockState, false);
            ((ItemRendererAccessor) this.itemRenderer).observations$renderBakedItemModel(model, block.asItem().getDefaultStack(), light, OverlayTexture.DEFAULT_UV, matrixStack, ItemRenderer.getItemGlintConsumer(vertexConsumerProvider, renderLayer, false, false));
            matrixStack.pop();
        }
    }
}
