package com.github.highd120.achievement;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;

public class AchievementTriggerer {
    private AchievementTriggerer() {
    }

    /**
     * アイテムを拾った時の実績解除。
     * @param event イベントのデータ。
     */
    @SubscribeEvent
    public static void onItemPickedUp(ItemPickupEvent event) {
        ItemStack stack = event.pickedUp.getEntityItem();
        if (stack == null || stack.getItem() instanceof ItemBlock) {
            return;
        }
        Block block = ((ItemBlock) stack.getItem()).getBlock();
        if (block instanceof IPickupBlockAchievement) {
            Achievement achievement = ((IPickupBlockAchievement) block).getAchievementOnPickup();
            event.player.addStat(achievement, 1);
        }
    }
}
