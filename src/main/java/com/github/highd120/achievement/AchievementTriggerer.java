package com.github.highd120.achievement;

import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

/**
 * 実績のトリガー。
 * @author hdgam
 *
 */
public class AchievementTriggerer {
    /**
     * クラフト時のトリガー。
     * @param event イベントのデータ。
     */
    @SubscribeEvent
    public static void onItemCrafted(ItemCraftedEvent event) {
        if (event.crafting != null && event.crafting.getItem() instanceof ICraftedAchievement) {
            Item item = event.crafting.getItem();
            Achievement achievement = ((ICraftedAchievement) item).getAchievement();
            event.player.addStat(achievement, 1);
        }
    }

}
