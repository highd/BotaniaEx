package com.github.highd120.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.github.highd120.ExtraWarpConfig;
import com.github.highd120.util.EnergyUtil;
import com.github.highd120.util.gui.Gui;
import com.github.highd120.util.gui.GuiField;
import com.github.highd120.util.gui.InputElement;

import ibxm.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Gui
public class GuiWarp extends GuiScreen {
	@GuiField
	private EntityPlayer entityPlayer;
	private List<InputElement> textFieldList = new ArrayList<>();

	@Override
	public void initGui() {
		buttonList.clear();
		String labelList[] = {"X", "Z"};
		textFieldList = IntStream.rangeClosed(0, 1).
			mapToObj(n->new InputElement(n, 100, 80 + n * 20, labelList[n], fontRendererObj)).
			collect(Collectors.toList());
		GuiButton warp = this.new WarpButton(3, 70, 140);
		buttonList.add(warp);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		int cost = getCost();
    	int energy = EnergyUtil.getEnergy(entityPlayer.getHeldItemMainhand());
		String msg = "cost:" + String.format("%,d", cost) + "RF" + (energy < cost ? " Not enough energy!" : "");
		fontRendererObj.drawString(msg, 70 , 60, energy >= cost ? 0xFFFFFF : 0xFF0000);
		super.drawScreen(mouseX, mouseY, partialTicks);
		textFieldList.forEach(textField->textField.draw());
	}

	@Override
    protected void keyTyped(char c, int keyCode) throws IOException {
        super.keyTyped(c, keyCode);
        textFieldList.forEach(textField->textField.keyTyped(c, keyCode));
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);
        textFieldList.forEach(textField->textField.mouseClicked(x, y, button));
    }

    private Optional<BlockPos> getWarpPositon() {
    	List<Optional<Integer> > list = textFieldList.stream().
    			map(textField->GuiWarp.parse(textField.getText())).
    			collect(Collectors.toList());
    	if (list.stream().allMatch(v->v.isPresent())) {
    		BlockPos positon = entityPlayer.getPosition();
    		int x = list.get(0).orElse(0);
    		int y = 300;
    		int z = list.get(1).orElse(0);
    		return Optional.of(new BlockPos(x, y, z));
    	}
    	return Optional.empty();
    }
    private int calculateCost(BlockPos positon1) {
		final BlockPos positon2 = entityPlayer.getPosition();
    	return(int) Math.sqrt(
			(positon1.getX() - positon2.getX()) * (positon1.getX() - positon2.getX()) +
			(positon1.getY() - positon2.getY()) * (positon1.getY() - positon2.getY()) +
			(positon1.getZ() - positon2.getZ()) * (positon1.getZ() - positon2.getZ())
		)*ExtraWarpConfig.useRf;
    }
    private int getCost() {
    	return getWarpPositon().map(pos->calculateCost(pos)).orElse(0);
    }

    static public Optional<Integer> parse(String str){
    	try {
    		return Optional.of(Integer.parseInt(str));
    	}catch(NumberFormatException e) {
    	}
		return Optional.empty();
    }

    @SideOnly(Side.CLIENT)
    class WarpButton extends GuiButton {
        public WarpButton(int buttonId, int x, int y) {
            super(buttonId, x, y, 120, 20, "Warp!");
        }
        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        	boolean result = super.mousePressed(mc, mouseX, mouseY);
        	if (!result) return false;
        	int energy = EnergyUtil.getEnergy(entityPlayer.getHeldItemMainhand());
        	getWarpPositon().ifPresent(pos->{
        		int cost = calculateCost(pos);
        		if (energy >= cost) {
        			EnergyUtil.setEnergy(entityPlayer.getHeldItemMainhand(), energy - cost);
            		entityPlayer.setPosition(pos.getX(), pos.getY(), pos.getZ());
            		entityPlayer.closeScreen();
        		}
        	});
			return result;
        }
    }
}
