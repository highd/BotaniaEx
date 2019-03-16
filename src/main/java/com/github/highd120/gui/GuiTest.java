package com.github.highd120.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.github.highd120.util.EnergyUtil;
import com.github.highd120.util.gui.Gui;
import com.github.highd120.util.gui.InputElement;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Gui
public class GuiTest extends GuiScreen {
	public static float X = 0;
	public static float Y = 0;
	public static float Z = 0;
	public static String text;
	private List<InputElement> textFieldList = new ArrayList<>();
	@Override
	public void initGui() {
		buttonList.clear();
		String labelList[] = {"X", "Y", "Z"};
		textFieldList = IntStream.rangeClosed(0, 2).
			mapToObj(n->new InputElement(n, 100, 80 + n * 20, labelList[n], fontRendererObj)).
			collect(Collectors.toList());
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		setPositon();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		fontRendererObj.drawString(text, 70 , 60, 0xFFFFFF);
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

    static public Optional<Float> parse(String str){
    	try {
    		return Optional.of(Float.parseFloat(str));
    	}catch(NumberFormatException e) {
    	}
		return Optional.empty();
    }

    private void setPositon() {
    	List<Optional<Float> > list = textFieldList.stream().
    			map(textField->GuiTest.parse(textField.getText())).
    			collect(Collectors.toList());
    	if (list.stream().allMatch(v->v.isPresent())) {
    		GuiTest.X = list.get(0).orElse(0F);
    		GuiTest.Y = list.get(1).orElse(0F);
    		GuiTest.Z = list.get(2).orElse(0F);
    	}
    }
}
