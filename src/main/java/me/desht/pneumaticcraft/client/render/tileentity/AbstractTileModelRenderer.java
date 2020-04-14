package me.desht.pneumaticcraft.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.desht.pneumaticcraft.common.tileentity.TileEntityBase;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public abstract class AbstractTileModelRenderer<T extends TileEntityBase> extends TileEntityRenderer<T> {
    AbstractTileModelRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    abstract void renderModel(T te, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn);

    protected boolean shouldRender(T te) {
        return true;
    }

    @Override
    public void render(T te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        // boilerplate translation code, common to all our model-rendering TERs, is done here

        if (!shouldRender(te) || !te.getWorld().isAreaLoaded(te.getPos(), 0)) return;

        matrixStack.push();

        // necessary transforms to make models render in the right place
        matrixStack.translate(0.5, 1.5, 0.5);
        matrixStack.scale(1f, -1f, -1f);

        // actual model rendering work
        renderModel(te, partialTicks, matrixStack, iRenderTypeBuffer, combinedLightIn, combinedOverlayIn);

        matrixStack.pop();

        renderExtras(te, partialTicks, matrixStack, iRenderTypeBuffer, combinedLightIn, combinedOverlayIn);
    }

    protected void renderExtras(T te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
    }

    public static abstract class BaseModel extends EntityModel {
        public void setRotation(ModelRenderer model, float x, float y, float z){
            model.rotateAngleX = x;
            model.rotateAngleY = y;
            model.rotateAngleZ = z;
        }
    }
}