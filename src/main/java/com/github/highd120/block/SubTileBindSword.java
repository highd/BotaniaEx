package com.github.highd120.block;

import java.util.List;
import java.util.Optional;

import org.lwjgl.opengl.GL11;

import com.github.highd120.Lexicon;
import com.github.highd120.item.ShotSwordItem;
import com.github.highd120.network.NetworkCreateItemEffect;
import com.github.highd120.network.NetworkHandler;
import com.github.highd120.util.CollectionUtil;
import com.github.highd120.util.Constant;
import com.github.highd120.util.ItemUtil;
import com.github.highd120.util.MathUtil;
import com.github.highd120.util.NbtTagUtil;
import com.github.highd120.util.item.ItemManager;
import com.github.highd120.util.subtile.SubTileRegister;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.sound.BotaniaSoundEvents;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;

/**
 * 剣とアイテムの結合を行う花。
 * @author hdgam
 */
@SubTileRegister(name = SubTileBindSword.NAME)
public class SubTileBindSword extends SubTileFunctional {
    public static final String NAME = "bindsword";
    private static final int RANGE = 1;
    private boolean isDisassemble = false;

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (mana < getMaxMana()) {
            return;
        }
        List<EntityItem> items = getWorld().getEntitiesWithinAABB(EntityItem.class,
                MathUtil.getAxisAlignedCube(getPos(), RANGE));
        if (isDisassemble) {
            disassemble(items);
        } else {
            assemble(items);
        }
    }

    /**
     * エフェクトの発生。
     * @param postion 座標。
     */
    private void spawnParticle() {
        NetworkHandler.sendToNearby(getWorld(), getPos().add(0, 1, 0),
                new NetworkCreateItemEffect(getPos().add(0, 1, 0)));
    }

    /**
     * 効果音の発生。
     */
    private void playSound() {
        getWorld().playSound(null, getPos(), BotaniaSoundEvents.terrasteelCraft,
                SoundCategory.BLOCKS, 0.2F, 1F);
    }

    /**
     * アイテムの分解。
     * @param items 範囲内にあるアイテム。
     */
    private void disassemble(List<EntityItem> items) {
        CollectionUtil.findIf(items, item -> {
            ItemStack itemStack = item.getEntityItem();
            if (itemStack.getItem() != ItemManager.getItem(ShotSwordItem.class)) {
                return false;
            }
            Optional<ItemStack> inner = NbtTagUtil.getInnerItem(Constant.SHOT_SWORD_TAG, itemStack);
            return inner.isPresent();
        }).ifPresent(item -> {
            if (!getWorld().isRemote) {
                spawnParticle();
                playSound();
                ItemStack itemStack = item.getEntityItem();
                ItemStack inner = NbtTagUtil.getInnerItem(Constant.SHOT_SWORD_TAG, itemStack).get();
                NbtTagUtil.removeTag(Constant.SHOT_SWORD_TAG, itemStack);
                Vec3d postion = MathUtil.blockPosToVec3dCenter(getPos().add(0, 1, 0));
                EntityItem result = ItemUtil.dropItem(getWorld(), postion, inner);
                result.setNoGravity(true);
                result.setVelocity(0, 0, 0);
                mana = 0;
            }
        });
    }

    /**
     * アイテムの合成。
     * @param items 範囲内にあるアイテム。
     */
    private void assemble(List<EntityItem> items) {
        EntityItem dropShotSword = CollectionUtil.findIf(items, item -> {
            ItemStack itemStack = item.getEntityItem();
            if (itemStack.getItem() != ItemManager.getItem(ShotSwordItem.class)) {
                return false;
            }
            Optional<ItemStack> inner = NbtTagUtil.getInnerItem(Constant.SHOT_SWORD_TAG, itemStack);
            return !inner.isPresent();
        }).orElse(null);
        EntityItem sword = CollectionUtil.findIf(items, item -> {
            return item.getEntityItem().getItem() instanceof ItemSword;
        }).orElse(null);
        if (dropShotSword == null || sword == null) {
            return;
        }
        mana = 0;
        if (!getWorld().isRemote) {
            spawnParticle();
            getWorld().playSound(null, getPos(), BotaniaSoundEvents.terrasteelCraft,
                    SoundCategory.BLOCKS, 0.2F, 1F);
            NbtTagUtil.setInnerItem(Constant.SHOT_SWORD_TAG, dropShotSword.getEntityItem(),
                    sword.getEntityItem());
            sword.setDead();
            dropShotSword.setNoGravity(true);
            dropShotSword.setVelocity(0, 0, 0);
            dropShotSword.setPosition(getPos().getX() + 0.5, getPos().getY() + 1,
                    getPos().getZ() + 0.5);
        }
    }

    @Override
    public int getMaxMana() {
        return 10000;
    }

    @Override
    public boolean onWanded(EntityPlayer player, ItemStack wand) {
        if (player == null) {
            return false;
        }
        if (player.isSneaking()) {
            isDisassemble = !isDisassemble;
            sync();
            return true;
        }
        return super.onWanded(player, wand);
    }

    @Override
    public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(toBlockPos(), RANGE);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUD(Minecraft mc, ScaledResolution res) {
        super.renderHUD(mc, res);

        int color = getColor();
        String filter = isDisassemble ? "disassemble" : "assemble";
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int x = res.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(filter) / 2;
        int y = res.getScaledHeight() / 2 + 30;

        mc.fontRendererObj.drawStringWithShadow(filter, x, y, color);
        GlStateManager.disableBlend();
    }

    @Override
    public void readFromPacketNBT(NBTTagCompound cmp) {
        super.readFromPacketNBT(cmp);
        isDisassemble = cmp.getBoolean(NAME);
    }

    @Override
    public void writeToPacketNBT(NBTTagCompound cmp) {
        super.writeToPacketNBT(cmp);
        cmp.setBoolean(NAME, isDisassemble);
    }

    @Override
    public LexiconEntry getEntry() {
        return Lexicon.bindSwordEntry;
    }
}
