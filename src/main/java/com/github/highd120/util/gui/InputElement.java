package com.github.highd120.util.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class InputElement {
	private GuiTextField textField;
	private String text;
	private int x;
	private int y;
	private FontRenderer fontRendererObj;
	public InputElement(int id, int x, int y,String text, FontRenderer fontRendererObj) {
		textField = new GuiTextField(id, fontRendererObj, x, y, 100, 14);
		this.text = text;
		this.x = x;
		this.fontRendererObj = fontRendererObj;
		this.y = y;
	}

	public String getText() {
		return textField.getText();
	}

	public void draw() {
		fontRendererObj.drawString(text, x-50 , y + 5, 0xFFFFFF);
		textField.drawTextBox();
	}
	public void keyTyped(char c, int keyCode) {
    	textField.textboxKeyTyped(c, keyCode);
    }

	public void mouseClicked(int x, int y, int button) {
        textField.mouseClicked(x, y, button);
    }
}
