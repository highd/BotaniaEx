package com.github.highd120.gui;

import com.github.highd120.util.gui.Gui;
import com.github.highd120.util.gui.GuiField;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.lib.LibResources;

@Gui(server = ContainerEditer.class)
public class GuiEditer extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(
            LibResources.GUI_FLOWER_BAG);

    @GuiField
    private EntityPlayer player;

    public GuiEditer() {
        super(null);
    }

    /**
     * 初期化。
     */
    public void init() {
        InventoryPlayer playerInv = player.inventory;
        InventoryEditer flowerBagInv = new InventoryEditer(player.getHeldItem(EnumHand.MAIN_HAND));
        this.inventorySlots = new ContainerEditer(playerInv, flowerBagInv);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
    }

}
