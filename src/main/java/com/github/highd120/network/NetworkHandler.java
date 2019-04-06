package com.github.highd120.network;

import com.github.highd120.BotaniaExMain;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
    private static final SimpleNetworkWrapper HANDLER = new SimpleNetworkWrapper(
            BotaniaExMain.MOD_ID);

    /**
     * 初期化。
     */
    public static void init() {
        int id = 0;
        HANDLER.registerMessage(NetworkInjectionEffect.Handler.class, NetworkInjectionEffect.class,
                id++, Side.CLIENT);
        HANDLER.registerMessage(NetworkInjectionEffectEnd.Handler.class,
                NetworkInjectionEffectEnd.class,
                id++, Side.CLIENT);
    }

    /**
     * メッセージの送信。
     * @param world ワールド。
     * @param pos 座標。
     * @param toSend メッセージ。
     */
    public static void sendToNearby(World world, BlockPos pos, IMessage toSend) {
        if (world instanceof WorldServer) {
            WorldServer ws = (WorldServer) world;

            for (EntityPlayer player : ws.playerEntities) {
                EntityPlayerMP playerMp = (EntityPlayerMP) player;

                if (playerMp.getDistanceSq(pos) < 64 * 64 && ws.getPlayerChunkMap()
                        .isPlayerWatchingChunk(playerMp, pos.getX() >> 4, pos.getZ() >> 4)) {
                    HANDLER.sendTo(toSend, playerMp);
                }
            }

        }
    }

}
