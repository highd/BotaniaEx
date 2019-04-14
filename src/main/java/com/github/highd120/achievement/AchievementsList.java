package com.github.highd120.achievement;

import com.github.highd120.BotaniaExMain;
import com.github.highd120.block.injection.BlockInjection;
import com.github.highd120.item.RuneItem;
import com.github.highd120.item.ShotSwordItem;
import com.github.highd120.util.item.ItemManager;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;

/**
 * 実績の管理。
 * @author hdgam
 */
public class AchievementsList {
    public static Achievement rune;
    public static Achievement shotSword;
    public static Achievement injection;
    private static AchievementPage page;

    private static Achievement getAchievement(String name, ItemStack item, int x, int y,
            Achievement parent) {
        return new Achievement("achievement." + BotaniaExMain.MOD_ID + name,
                BotaniaExMain.MOD_ID + name, x, y, item, parent);
    }

    /**
     * 初期化。
     */
    public static void init() {
        rune = getAchievement(":rune", ItemManager.getItemStack(RuneItem.class), 0,
                0, null);

        shotSword = getAchievement(":shotsword", ItemManager.getItemStack(ShotSwordItem.class), 2,
                0, rune);

        injection = getAchievement(":injection", ItemManager.getItemStack(BlockInjection.class), 0,
                2, rune);

        Achievement[] achievementList = new Achievement[] {
                rune,
                shotSword,
                injection
        };
        for (Achievement acievement : achievementList) {
            acievement.registerStat();
        }

        page = new AchievementPage(BotaniaExMain.MOD_NAME, achievementList);
        AchievementPage.registerAchievementPage(page);
        MinecraftForge.EVENT_BUS.register(AchievementTriggerer.class);
    }
}
