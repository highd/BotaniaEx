package com.github.highd120.item;

import com.github.highd120.achievement.AchievementsList;
import com.github.highd120.util.item.ItemRegister;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.IFlowerComponent;
import vazkii.botania.common.achievement.IPickupAchievement;

@ItemRegister(name = "rune")
public class ItemRune extends ItemBase implements IFlowerComponent, IPickupAchievement {

    @Override
    public boolean canFit(ItemStack stack, IPetalApothecary apothecary) {
        return true;
    }

    @Override
    public int getParticleColor(ItemStack stack) {
        return 0xA8A8A8;
    }

    @Override
    public Achievement getAchievementOnPickup(ItemStack arg0, EntityPlayer arg1, EntityItem arg2) {
        return AchievementsList.rune;
    }
}
