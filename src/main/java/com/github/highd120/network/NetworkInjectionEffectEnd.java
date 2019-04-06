package com.github.highd120.network;

import com.github.highd120.util.MathUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.botania.common.Botania;

public class NetworkInjectionEffectEnd implements IMessage {
    private BlockPos point;

    public NetworkInjectionEffectEnd(BlockPos point) {
        this.point = point;
    }

    public NetworkInjectionEffectEnd() {
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

    public static class Handler implements IMessageHandler<NetworkInjectionEffectEnd, IMessage> {

        @Override
        public IMessage onMessage(NetworkInjectionEffectEnd message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Vec3d init = MathUtil.blockPosToVec3dCenter(message.point);
                for (int i = 0; i < 360; i++) {

                    Botania.proxy.wispFX(init.xCoord, init.yCoord + 4, init.zCoord, 1, 1, 1, 0.3f,
                            (float) Math.sin(Math.toRadians(i)) * 1.5f, 0.0f,
                            (float) Math.cos(Math.toRadians(i)) * 1.5f);
                }
            });
            return null;
        }

    }
}
