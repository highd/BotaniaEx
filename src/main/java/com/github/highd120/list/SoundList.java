package com.github.highd120.list;

import com.github.highd120.BotaniaExMain;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SoundList {
    public static SoundEvent injectionEffect;
    public static SoundEvent injectionComplete;

    public static void init() {
        injectionEffect = register("injection_effect");
        injectionComplete = register("injection_complete");
    }

    private static SoundEvent register(String name) {
        ResourceLocation location = new ResourceLocation(BotaniaExMain.MOD_ID + ":" + name);
        return GameRegistry.register(new SoundEvent(location), location);
    }

    /**
     * 効果音の発生。
     * @param world ワールド。
     * @param sound 効果音。
     * @param postion 座標。
     */
    public static void playSoundBlock(World world, SoundEvent sound, BlockPos postion) {
        if (!world.isRemote) {
            world.playSound(null, postion.getX(), postion.getY(), postion.getZ(), sound,
                    SoundCategory.BLOCKS, 1, 1);
        }

    }
}
