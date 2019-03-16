package com.github.highd120.achievement;

import net.minecraft.stats.Achievement;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class AchievementTriggerer {
	@SubscribeEvent
	public static void onItemCrafted(ItemCraftedEvent event) {
		if(event.crafting != null && event.crafting.getItem() instanceof ICraftedAchievement) {
			Achievement achievement = ((ICraftedAchievement) event.crafting.getItem()).getAchievement();
			event.player.addStat(achievement, 1);
		}
	}

}
