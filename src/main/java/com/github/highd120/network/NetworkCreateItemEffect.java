package com.github.highd120.network;

import com.github.highd120.BotaniaExMain;
import com.github.highd120.util.MathUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class NetworkCreateItemEffect implements IMessage {
    private BlockPos point;

    public NetworkCreateItemEffect(BlockPos point) {
        this.point = point;
    }

    public NetworkCreateItemEffect() {
        this.point = new BlockPos(0, 0, 0);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        point = new BlockPos(x, y, z);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(point.getX());
        buf.writeInt(point.getY());
        buf.writeInt(point.getZ());
    }

    public static class Handler implements IMessageHandler<NetworkCreateItemEffect, IMessage> {

        @Override
        public IMessage onMessage(NetworkCreateItemEffect message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Vec3d point = MathUtil.blockPosToVec3dCenter(message.point).addVector(0, 0.4, 0);
                BotaniaExMain.proxy.spawnWispInjection(point, 2, 3);
            });
            return null;
        }

    }
}
