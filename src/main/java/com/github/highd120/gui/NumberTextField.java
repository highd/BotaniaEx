package com.github.highd120.gui;

import java.util.Optional;

import com.github.highd120.editer.NumberElementDataProxy;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NumberTextField extends GuiTextField {
    private NumberElementDataProxy proxy;

    /**
     * コンストラクター。
     * @param componentId コンポーネントのID
     * @param fontrendererObj フォントレンダラー。
     * @param x x座標。
     * @param y y座標。
     * @param par5Width 幅
     * @param par6Height 高さ。
     */
    public NumberTextField(int componentId, FontRenderer fontrendererObj, int x, int y,
            int par5Width, int par6Height, NumberElementDataProxy proxy) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
        setValidator(text -> parse(text)
                .flatMap(val -> 1 <= val && val <= 5 ? Optional.of(1) : Optional.empty())
                .isPresent());
        this.proxy = proxy;
    }

    /**
     * 更新。
     */
    public void upDate() {
        parse(getText()).ifPresent(number -> proxy.setNumber(number));
    }

    private static Optional<Integer> parse(String str) {
        try {
            return Optional.of(Integer.parseInt(str));
            //
        } catch (NumberFormatException e) { // CHECKSTYLE IGNORE THIS LINE
        }
        return Optional.empty();
    }
}