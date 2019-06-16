package com.github.highd120.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.highd120.BotaniaExMain;
import com.github.highd120.editer.CheckElementDataProxy;
import com.github.highd120.editer.EditerData;
import com.github.highd120.editer.EditerData.ElementData;
import com.github.highd120.editer.EditerData.ElementType;
import com.github.highd120.editer.ElementDataProxy;
import com.github.highd120.editer.NumberElementDataProxy;
import com.github.highd120.item.ItemShotSword;
import com.github.highd120.util.Constant;
import com.github.highd120.util.NbtTagUtil;
import com.github.highd120.util.gui.Gui;
import com.github.highd120.util.gui.GuiField;
import com.github.highd120.util.item.ItemManager;

import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Gui(server = ContainerEditer.class)
public class GuiEditer extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(
            BotaniaExMain.MOD_ID + ":textures/gui/inventorySlot.png");

    @GuiField
    private EntityPlayer player;
    private List<NumberTextField> textFieldList = new ArrayList<>();
    private List<ElementDataProxy> dataList = new ArrayList<>();
    private List<TextData> textList = new ArrayList<>();
    private Item oldItem;

    private static Map<Item, EditerData> dataMap = new HashMap<>();

    /**
     * データの初期化。
     */
    public static void initData() {
        EditerData shotSword = EditerData.builder()
                .inventoryName(Constant.SHOT_SWORD_TAG)
                .element(new ElementData(ElementType.CHECKER, "HOMING"))
                .element(new ElementData(ElementType.NUMBER, "LV"))
                .build();
        dataMap.put(ItemManager.getItem(ItemShotSword.class), shotSword);
    }

    private Optional<EditerData> getData() {
        ItemStack main = inventorySlots.getSlot(0).getStack();
        if (main != null && dataMap.containsKey(main.getItem())) {
            return Optional.of(dataMap.get(main.getItem()));
        }
        return Optional.empty();
    }

    public GuiEditer() {
        super(null);
    }

    /**
     * 初期化。
     */
    public void init() {
        InventoryPlayer playerInv = player.inventory;
        InventoryEditer editerInventory = new InventoryEditer(
                player.getHeldItem(EnumHand.MAIN_HAND));
        inventorySlots = new ContainerEditer(playerInv, editerInventory);
    }

    private void addElement(ElementData data, int id) {
        String name = data.getName();
        int x = guiLeft + 30;
        int y = guiTop + 26 + id * 25;
        textList.add(new TextData(x, y, name));
        switch (data.getType()) {
        case CHECKER: {
            CheckElementDataProxy proxy = new CheckElementDataProxy(name);
            dataList.add(proxy);
            buttonList.add(new CheckerButton(id, x + 80, y, proxy));
            break;
        }
        case NUMBER: {
            NumberElementDataProxy proxy = new NumberElementDataProxy(name);
            dataList.add(proxy);
            textFieldList.add(new NumberTextField(id, fontRendererObj, x + 80, y, 70, 14, proxy));
            break;
        }
        default:
            break;
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        guiTop = 0;
    }

    private void changeData() {
        buttonList.clear();
        textFieldList.clear();
        textList.clear();
        dataList.clear();
        getData().ifPresent(data -> {
            List<ElementData> list = data.getElements();
            for (int i = 0; i < list.size(); i++) {
                addElement(list.get(i), i);
            }
            buttonList.add(new CraftButton(0, guiLeft + 26, guiTop + 130));
        });

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        ItemStack main = inventorySlots.getSlot(0).getStack();
        if (main == null ? oldItem != null : main.getItem() != oldItem) {
            changeData();
        }
        oldItem = main != null ? main.getItem() : null;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        inventorySlots.inventorySlots.forEach(slot -> {
            int x = guiLeft + slot.xDisplayPosition - 1;
            int y = guiTop + slot.yDisplayPosition - 1;
            drawModalRectWithCustomSizedTexture(x, y, 0, 0, 18, 18, 18, 18);
        });
        textFieldList.forEach(textField -> {
            textField.upDate();
            textField.drawTextBox();
        });
        textList.forEach(data -> {
            fontRendererObj.drawString(data.name, data.x, data.y + 5, 0xFFFFFF);
        });
    }

    @Override
    protected void keyTyped(char c, int keyCode) throws IOException {
        super.keyTyped(c, keyCode);
        textFieldList.forEach(textField -> textField.textboxKeyTyped(c, keyCode));
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);
        textFieldList.forEach(textField -> textField.mouseClicked(x, y, button));
    }

    @SideOnly(Side.CLIENT)
    public class CraftButton extends GuiButton {
        public CraftButton(int buttonId, int x, int y) {
            super(buttonId, x, y, 120, 20, "craft");
        }

        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            boolean flag = super.mousePressed(mc, mouseX, mouseY);
            if (!flag) {
                return false;
            }
            ItemStack main = inventorySlots.getSlot(0).getStack();
            EditerData data = getData().orElse(null);
            if (main != null && data != null) {
                ItemStack result = main.copy();
                NBTTagCompound compound = NbtTagUtil.getCompound(result);
                int slotCount = data.getInventoryNames().size();
                for (int i = 0; i < slotCount; i++) {
                    ItemStack inner = inventorySlots.getSlot(i + 1).getStack();
                    NbtTagUtil.setInnerItem(data.getInventoryNames().get(i), result, inner);
                }
                dataList.forEach(proxy -> {
                    proxy.writeNbt(compound);
                });
                inventorySlots.putStackInSlot(0, result);
                inventorySlots.transferStackInSlot(player, 0);
                inventorySlots.putStackInSlot(0, main);
            }
            return flag;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class CheckerButton extends GuiButton {
        private CheckElementDataProxy data;

        public CheckerButton(int buttonId, int x, int y, CheckElementDataProxy data) {
            super(buttonId, x, y, 50, 20, "OFF");
            this.data = data;
        }

        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            boolean result = super.mousePressed(mc, mouseX, mouseY);
            if (!result) {
                return false;
            }
            data.change();
            displayString = data.isHas() ? "ON" : "OFF";
            return result;
        }
    }

    @AllArgsConstructor
    public static class TextData {
        int x;
        int y;
        String name;
    }
}
