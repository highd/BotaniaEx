package com.github.highd120.block.injection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.highd120.block.TileStand;
import com.github.highd120.block.base.SparkManager;
import com.github.highd120.list.SoundList;
import com.github.highd120.network.NetworkHandler;
import com.github.highd120.network.NetworkInjectionEffect;
import com.github.highd120.network.NetworkInjectionEffectEnd;
import com.github.highd120.util.ItemUtil;
import com.github.highd120.util.MathUtil;
import com.github.highd120.util.NbtTagUtil;
import com.github.highd120.util.WorldUtil;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Delegate;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.sound.BotaniaSoundEvents;

public class TileInjection extends TileStand implements ISparkAttachable, SparkManager.IOverride {
    private static final int MAX_MANA = 100000;

    public enum InjectionState {
        NOT_WORKING, CHARGE_MANA, EFFECT, ITEM_FLOW;

    }

    private ItemStack resultItem;
    private InjectionState state = InjectionState.NOT_WORKING;
    @Delegate(types = ISparkAttachable.class)
    private SparkManager sparkManager;
    private int complateMane;
    private int soundCount = 0;
    private InjectionEffectManager effect;

    private static InjectionState[] stateList = InjectionState.values();

    public TileInjection() {
        sparkManager = new SparkManager(MAX_MANA, this);
    }

    public InjectionEffectManager getEffect() {
        return effect;
    }

    /**
     * 現在の状態の取得。
     * @return 状態。
     */
    public InjectionState getState() {
        return state;
    }

    @Override
    public void subReadNbt(NBTTagCompound compound) {
        super.subReadNbt(compound);
        resultItem = NbtTagUtil.readItem(compound, "resultItem");
        state = stateList[compound.getInteger("state")];
        NbtTagUtil.readListFunction(compound, "standList", NbtTagUtil::readBlockPos);
        if (compound.hasKey("effect")) {
            effect = new InjectionEffectManager();
            effect.readNbt(compound);
        }
    }

    @Override
    public void subWriteNbt(NBTTagCompound compound) {
        super.subWriteNbt(compound);
        compound.setInteger("state", state.ordinal());
        NbtTagUtil.writeItem(compound, "resultItem", resultItem);
        if (effect != null) {
            effect.writeNbt(compound);
        }
    }

    @Override
    public void update() {
        if (effect != null) {
            effect.upDate();
            NetworkHandler.sendToNearby(getWorld(), getPos(), new NetworkInjectionEffect(getPos()));
        }
        if (state == InjectionState.CHARGE_MANA) {
            sparkManager.sparkUpDate();
            soundCount++;
            if (soundCount % 9 == 0) {
                SoundList.playSoundBlock(getWorld(), BotaniaSoundEvents.ding, getPos());
            }
            if (getCurrentMana() >= complateMane) {
                SoundList.playSoundBlock(getWorld(), SoundList.injectionEffect, getPos());
                state = InjectionState.EFFECT;
                effect.start();
                soundCount = 0;
                blockUpdate();
            }
        }
        if (state == InjectionState.EFFECT) {
            if (effect.isEnd()) {
                SoundList.playSoundBlock(getWorld(), SoundList.injectionComplete, getPos());
                effect = null;
                complete();
                blockUpdate();
            }
        }
        super.update();
    }

    private void complete() {
        NetworkHandler.sendToNearby(getWorld(), getPos(), new NetworkInjectionEffectEnd(getPos()));
        if (!getWorld().isRemote) {
            EntityItem result = ItemUtil.dropItem(getWorld(), getPos().add(0, 4, 0), resultItem);
            result.setNoGravity(true);
            result.setVelocity(0, -0.2f, 0);
        }
        sparkManager.useMana(complateMane);
        state = InjectionState.NOT_WORKING;
    }

    @AllArgsConstructor
    private static class LaunchableResult {
        List<TileStand> standList;
        boolean isLaunchable;
    }

    /**
     * モードをアクティブにする。
     */
    public void active() {
        LaunchableResult result = isLaunchable();
        if (state == InjectionState.NOT_WORKING && result.isLaunchable) {
            List<BlockPos> standList = result.standList.stream()
                    .filter(stand -> stand.getItem() != null)
                    .map(stand -> stand.getPos())
                    .collect(Collectors.toList());
            effect = new InjectionEffectManager(standList, getPos());
            state = InjectionState.CHARGE_MANA;
            result.standList.forEach(stand -> stand.removeItem());
            removeItem();
        }
    }

    @Value
    private static class InjectionItemData implements Comparable<InjectionItemData> {
        String name;

        @Override
        public int compareTo(InjectionItemData o) {
            return name.compareTo(o.name);
        }
    }

    /**
     * 稼働できるかの判定。
     * @return 稼働できるか。
     */
    public LaunchableResult isLaunchable() {
        if (getItem() == null) {
            return new LaunchableResult(new ArrayList<>(), false);
        }
        List<TileStand> standList = WorldUtil.scanTileEnity(getWorld(),
                MathUtil.getAxisAlignedPlane(getPos(), 3),
                tile -> tile instanceof TileStand && !(tile instanceof TileInjection));
        List<ItemStack> itemList = standList.stream()
                .map(stand -> stand.getItem())
                .filter(item -> item != null)
                .collect(Collectors.toList());
        for (InjectionRecipeData data : InjectionRecipe.recipes) {
            if (data.checkRecipe(itemList, getItem())) {
                resultItem = data.craft(itemList, getItem());
                complateMane = data.getUseMana();
                return new LaunchableResult(standList, true);
            }
        }
        return new LaunchableResult(new ArrayList<>(), false);
    }

    @Override
    public boolean canRecieveMana() {
        return state == InjectionState.CHARGE_MANA;
    }
}
