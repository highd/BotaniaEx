package com.github.highd120.achievement;

import com.github.highd120.KeyConfigEditer;
import com.github.highd120.item.ItemList;
import com.github.highd120.item.ShotSwordItem;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import scala.reflect.internal.Variance.SbtCompat;

public class AchievementsList {
	public static Achievement shotSword;
	private static int pageIndex;
	private static AchievementPage page;
	public static void init() {
		shotSword = new Achievement("achievement.extrawarp:shotsword", "extrawarp:shotsword", 0, 0, new ItemStack(ItemList.shotSwordItem), null);
		pageIndex = AchievementPage.getAchievementPages().size();
		page = new AchievementPage(KeyConfigEditer.MOD_NAME, new Achievement[] {
			shotSword
		});
		AchievementPage.registerAchievementPage(page);
		MinecraftForge.EVENT_BUS.register(AchievementTriggerer.class);
	}
}
