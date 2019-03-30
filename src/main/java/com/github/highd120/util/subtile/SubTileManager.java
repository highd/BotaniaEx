package com.github.highd120.util.subtile;

import java.util.ArrayList;
import java.util.List;

import com.github.highd120.BotaniaExMain;
import com.github.highd120.util.ClassUtil;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.subtile.SubTileEntity;

public class SubTileManager {
    private static List<Class<?>> classList = new ArrayList<>();

    static {
        classList = ClassUtil.getClassList("com.github.highd120.block", SubTileRegister.class);
    }

    /**
     * 初期化。
     */
    @SuppressWarnings("unchecked")
    public static void init(boolean isClient) {
        classList.forEach(clazz -> {
            String name = clazz.getAnnotation(SubTileRegister.class).name();
            BotaniaAPI.registerSubTile(name, (Class<? extends SubTileEntity>) clazz);
            BotaniaAPI.addSubTileToCreativeMenu(name);
            if (isClient) {
                BotaniaAPIClient.registerSubtileModel((Class<? extends SubTileEntity>) clazz,
                        new ModelResourceLocation(BotaniaExMain.MOD_ID + ":" + name));
            }
        });
    }

}
