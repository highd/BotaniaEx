package com.github.highd120.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * 剣のエンティティのレンダー。
 * @author hdgam
 */
public class RenderEntitySword extends Render<EntitySword> {
    private final RenderItem itemRenderer;

    public RenderEntitySword(RenderManager renderManagerIn) {
        super(renderManagerIn);
        itemRenderer = Minecraft.getMinecraft().getRenderItem();
    }

    @SuppressWarnings("deprecation")
    private void transformModelCount(EntitySword entity, double x, double y, double z,
            float partialTicks, IBakedModel ibakedmodel) {
        boolean isGui3d = ibakedmodel.isGui3d();
        float f2 = ibakedmodel.getItemCameraTransforms()
                .getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
        GlStateManager.translate((float) x, (float) y + 0.45F * f2, (float) z);
        if (isGui3d || this.renderManager.options != null) {
            GlStateManager.rotate(
                    entity.prevRotationYaw
                            + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F,
                    0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(entity.prevRotationPitch
                    + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks - 45.0F,
                    0.0F, 0.0F, 1.0F);
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * エンティティのレンダリング。
     */
    @Override
    public void doRender(EntitySword entity, double x, double y, double z, float entityYaw,
            float partialTicks) {
        final ItemStack itemStack = entity.getItem();
        boolean flag = false;
        if (this.bindEntityTexture(entity)) {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity))
                    .setBlurMipmap(false, false);
            flag = true;
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        IBakedModel ibakedmodel = this.itemRenderer.getItemModelWithOverrides(itemStack,
                entity.worldObj, (EntityLivingBase) null);
        transformModelCount(entity, x, y, z, partialTicks, ibakedmodel);
        boolean isGui3d = ibakedmodel.isGui3d();

        if (!isGui3d) {
            GlStateManager.translate(0, 0, 0);
        }

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        if (isGui3d) {
            GlStateManager.pushMatrix();
            ibakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                    ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
            this.itemRenderer.renderItem(itemStack, ibakedmodel);
            GlStateManager.popMatrix();
        } else {
            GlStateManager.pushMatrix();
            ibakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                    ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
            this.itemRenderer.renderItem(itemStack, ibakedmodel);
            GlStateManager.popMatrix();
            GlStateManager.translate(0.0F, 0.0F, 0.09375F);
        }

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindEntityTexture(entity);

        if (flag) {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity))
                    .restoreLastBlurMipmap();
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * エンティティのテクスチャーの場所の取得。
     */
    @Override
    protected ResourceLocation getEntityTexture(EntitySword entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
