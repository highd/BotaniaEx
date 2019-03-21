package com.github.highd120.achievement;

import net.minecraft.stats.Achievement;

/**
 * クラフト時に実績が解除されるアイテム。
 * @author hdgam
 *
 */
public interface ICraftedAchievement {
    /**
     * 解除されるアイテムの取得。
     * @return 解除されるアイテム。
     */
    Achievement getAchievement();
}
