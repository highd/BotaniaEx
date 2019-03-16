package com.github.highd120.entity;

import com.github.highd120.gui.GuiTest;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.MapData;

public class RenderEntitySword extends Render<EntitySword> {
    private final ModelResourceLocation itemFrameModel = new ModelResourceLocation("item_frame", "normal");
    private final RenderItem itemRenderer;
    private static final ResourceLocation RES_ARROW = new ResourceLocation("textures/entity/projectiles/arrow.png");
	public RenderEntitySword(RenderManager renderManagerIn) {
		super(renderManagerIn);
		itemRenderer = Minecraft.getMinecraft().getRenderItem();
	}

    private void transformModelCount(EntitySword entity, double x, double y, double z, float partialTicks, IBakedModel ibakedmodel){
        ItemStack itemstack = entity.getItem();
        Item item = itemstack.getItem();
        boolean isGui3d = ibakedmodel.isGui3d();
        float f2 = ibakedmodel.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
        GlStateManager.translate((float)x, (float)y + 0.45F * f2, (float)z);
        if (isGui3d || this.renderManager.options != null) {
            //GlStateManager.rotate(180F / (float)Math.PI, 0.0F, 1.0F, 0.0F);
            //GlStateManager.rotate(GuiTest.X, 1.0F, 0.0F, 0.0F);
            //GlStateManager.rotate(GuiTest.Y, 0.0F, 1.0F, 0.0F);
            //GlStateManager.rotate(GuiTest.Z, 0.0F, 0.0F, 1.0F);
        	//GlStateManager.rotate(140F / (float)Math.PI, 0.0F, 0.0F, 1.0F);
        	//GlStateManager.rotate(45.0F, 0.0F, 0.0F, 1.0F);
            //GlStateManager.rotate(45.0F , 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks -45.0F, 0.0F, 0.0F, 1.0F);
            //GlStateManager.rotate(45.0F , 0.0F, 0.0F, 1.0F);
            //GlStateManager.rotate(90.0F , 1.0F, 0.0F, 0.0F);
        	GuiTest.text = Minecraft.getMinecraft().thePlayer.getLookVec().toString();
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    @Override
    public void doRender(EntitySword entity, double x, double y, double z, float entityYaw, float partialTicks) {
        ItemStack itemStack = entity.getItem();
        boolean flag = false;
        if (this.bindEntityTexture(entity)) {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
            flag = true;
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        IBakedModel ibakedmodel = this.itemRenderer.getItemModelWithOverrides(itemStack, entity.worldObj, (EntityLivingBase)null);
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
            ibakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
            this.itemRenderer.renderItem(itemStack, ibakedmodel);
            GlStateManager.popMatrix();
        }else {
            GlStateManager.pushMatrix();
            ibakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
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
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(EntitySword entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    /*==================================== FORGE START ===========================================*/

    /**
     * Items should spread out when rendered in 3d?
     * @return
     */
    public boolean shouldSpreadItems()  {
        return true;
    }

    /**
     * Items should have a bob effect
     * @return
     */
    public boolean shouldBob() {
        return true;
    }
    /*==================================== FORGE END =============================================*/
}
