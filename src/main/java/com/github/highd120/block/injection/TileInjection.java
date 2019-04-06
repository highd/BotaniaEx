package com.github.highd120.block.injection;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.highd120.block.TileStand;
import com.github.highd120.network.NetworkHandler;
import com.github.highd120.network.NetworkInjectionEffect;
import com.github.highd120.network.NetworkInjectionEffectEnd;
import com.github.highd120.util.ItemUtil;
import com.github.highd120.util.MathUtil;
import com.github.highd120.util.NbtTagUtil;
import com.github.highd120.util.WorldUtil;
import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkHelper;

public class TileInjection extends TileStand implements ISparkAttachable {
    private static final int MAX_MANA = 100000;

    private ItemStack resultItem;
    private InjectionState state = InjectionState.NOT_WORKING;
    private int mana;
    private int complateMane;
    private InjectionEffectManager effect;

    private static InjectionState[] stateList = InjectionState.values();

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
        mana = compound.getInteger("mana");
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
        compound.setInteger("mana", mana);
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
            sparkUpDate();
            if (getCurrentMana() >= complateMane) {
                state = InjectionState.EFFECT;
                effect.start();
                VanillaPacketDispatcher.dispatchTEToNearbyPlayers(getWorld(), pos);
                getWorld().scheduleUpdate(getPos(), getBlockType(),
                        getBlockType().tickRate(getWorld()));
            }
        }
        if (state == InjectionState.EFFECT) {
            if (effect.isEnd()) {
                effect = null;
                complete();
                VanillaPacketDispatcher.dispatchTEToNearbyPlayers(getWorld(), pos);
                getWorld().scheduleUpdate(getPos(), getBlockType(),
                        getBlockType().tickRate(getWorld()));
            }
        }
        super.update();
    }

    private void complete() {
        NetworkHandler.sendToNearby(getWorld(), getPos(), new NetworkInjectionEffectEnd(getPos()));
        if (!getWorld().isRemote) {
            EntityItem result = ItemUtil.dropItem(getWorld(), getPos().add(0, 8, 0), resultItem);
            result.setNoGravity(true);
            result.setVelocity(0, -0.2f, 0);
        }
        mana -= complateMane;
        state = InjectionState.NOT_WORKING;
    }

    private void sparkUpDate() {
        ISparkEntity spark = getAttachedSpark();
        if (spark != null) {
            List<ISparkEntity> sparkEntities = SparkHelper.getSparksAround(worldObj,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            for (ISparkEntity otherSpark : sparkEntities) {
                if (spark == otherSpark) {
                    continue;
                }

                if (otherSpark.getAttachedTile() != null
                        && otherSpark.getAttachedTile() instanceof IManaPool) {
                    otherSpark.registerTransfer(spark);
                }
            }
        }
    }

    /**
     * モードをアクティブにする。
     */
    public void active() {
        if (state == InjectionState.NOT_WORKING && isLaunchable()) {
            List<BlockPos> standList = WorldUtil.scanTileEnity(getWorld(),
                    MathUtil.getAxisAlignedPlane(getPos(), 3),
                    tile -> tile instanceof TileStand && !(tile instanceof TileInjection),
                    (TileStand stand) -> Optional.ofNullable(stand.getPos()));
            effect = new InjectionEffectManager(standList, getPos());
            state = InjectionState.CHARGE_MANA;
        }
    }

    /**
     * 稼働できるかの判定。
     * @return 稼働できるか。
     */
    public boolean isLaunchable() {
        if (getItem() == null) {
            return false;
        }
        List<String> itemNameList = WorldUtil.scanTileEnity(getWorld(),
                MathUtil.getAxisAlignedPlane(getPos(), 3),
                tile -> tile instanceof TileStand && !(tile instanceof TileInjection),
                (TileStand stand) -> Optional.ofNullable(stand.getItem()))
                .stream()
                .map(item -> item.getItem().getRegistryName().toString())
                .sorted()
                .collect(Collectors.toList());
        for (InjectionRecipe.Data data : InjectionRecipe.recipes) {
            List<String> recipe = data.getInput().getInjectionList().stream()
                    .map(item -> item.getItem().getRegistryName().toString())
                    .sorted().collect(Collectors.toList());
            if (itemNameList.equals(recipe)
                    && getItem().getItem() == data.getInput().getMain().getItem()) {
                resultItem = data.getOutput().copy();
                complateMane = data.getUseMana();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isFull() {
        return mana >= MAX_MANA;
    }

    @Override
    public void recieveMana(int mana) {
        this.mana += mana;

    }

    @Override
    public boolean canRecieveManaFromBursts() {
        return state == InjectionState.CHARGE_MANA;
    }

    @Override
    public int getCurrentMana() {
        return mana;
    }

    @Override
    public boolean canAttachSpark(ItemStack stack) {
        return true;
    }

    @Override
    public void attachSpark(ISparkEntity entity) {

    }

    @Override
    public int getAvailableSpaceForMana() {
        return Math.max(0, MAX_MANA - getCurrentMana());
    }

    @Override
    public ISparkEntity getAttachedSpark() {
        List<Entity> sparks = worldObj.getEntitiesWithinAABB(Entity.class,
                new AxisAlignedBB(pos.up(), pos.up().add(1, 1, 1)),
                Predicates.instanceOf(ISparkEntity.class));
        if (sparks.size() == 1) {
            Entity e = sparks.get(0);
            return (ISparkEntity) e;
        }

        return null;
    }

    @Override
    public boolean areIncomingTranfersDone() {
        return !(state == InjectionState.CHARGE_MANA);
    }
}
