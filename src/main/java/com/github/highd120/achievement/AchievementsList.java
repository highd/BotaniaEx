package com.github.highd120.achievement;

import com.github.highd120.BotaniaExMain;
import com.github.highd120.item.ItemList;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;

/**
 * 実績の管理。
 * @author hdgam
 */
public class AchievementsList {
    public static Achievement shotSword;
    private static AchievementPage page;

    /**
     * 初期化。
     */
    public static void init() {
        shotSword = new Achievement(
                "achievement." + BotaniaExMain.MOD_ID + ":shotsword",
                BotaniaExMain.MOD_ID + ":shotsword",
                0, 0, new ItemStack(ItemList.shotSwordItem), null);
        page = new AchievementPage(BotaniaExMain.MOD_NAME, new Achievement[] {
            shotSword
        });
        AchievementPage.registerAchievementPage(page);
        MinecraftForge.EVENT_BUS.register(AchievementTriggerer.class);
    }
}
