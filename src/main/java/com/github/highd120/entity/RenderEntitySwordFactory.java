package com.github.highd120.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * 剣のエンティティのレンダーのファクトリー。
 * @author hdgam
 */
public class RenderEntitySwordFactory implements IRenderFactory<EntitySword> {
    @Override
    public RenderEntitySword createRenderFor(RenderManager manager) {
        return new RenderEntitySword(manager);
    }
}
