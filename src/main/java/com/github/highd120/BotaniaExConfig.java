package com.github.highd120;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.fml.common.Mod;

@Config(modid = BotaniaExMain.MOD_ID, type = Type.INSTANCE, name =  BotaniaExMain.MOD_ID)
public class BotaniaExConfig {
	public static int maxCapacity = 10000 * 10000 * 10;
	public static int useRf = 1000;
}
