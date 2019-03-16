package com.github.highd120;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.fml.common.Mod;

@Config(modid = KeyConfigEditer.MOD_ID, type = Type.INSTANCE, name =  KeyConfigEditer.MOD_ID)
public class ExtraWarpConfig {
	public static int maxCapacity = 10000 * 10000 * 10;
	public static int useRf = 1000;
}
