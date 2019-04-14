package com.github.highd120.proxy;

import com.github.highd120.block.TileRockDropper;
import com.github.highd120.block.TileStand;
import com.github.highd120.block.TileStandRenderer;
import com.github.highd120.block.injection.TileInjection;
import com.github.highd120.entity.EntitySword;
import com.github.highd120.entity.RenderEntitySwordFactory;
import com.github.highd120.list.FluidList;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntitySword.class,
                new RenderEntitySwordFactory());
        ClientRegistry.bindTileEntitySpecialRenderer(TileStand.class,
                new TileStandRenderer(0.5f));
        ClientRegistry.bindTileEntitySpecialRenderer(TileInjection.class,
                new TileStandRenderer(0.1f));
        ClientRegistry.bindTileEntitySpecialRenderer(TileRockDropper.class,
                new TileStandRenderer(0.1f));
    }

    @Override
    public void registerFluid() {
        FluidList.initClient();
    }

    @Override
    public void spawnWispInjection(Vec3d initPoint) {
        FxWispInjection wisp = new FxWispInjection(Minecraft.getMinecraft().theWorld, initPoint);
        wisp.setSpeed(0, 0, 0);
        Minecraft.getMinecraft().effectRenderer.addEffect(wisp);
    }

    @Override
    public void spawnWispInjection(Vec3d initPoint, int size, int lifeTime) {
        World world = Minecraft.getMinecraft().theWorld;
        FxWispInjection wisp = new FxWispInjection(world, initPoint, size, lifeTime);
        wisp.setSpeed(0, 0, 0);
        Minecraft.getMinecraft().effectRenderer.addEffect(wisp);
    }
}
