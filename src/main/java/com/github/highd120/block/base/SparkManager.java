package com.github.highd120.block.base;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkHelper;

public class SparkManager implements ISparkAttachable {
    public interface IOverride {
        boolean canRecieveMana();

        BlockPos getPostion();

        World getWorldObj();
    }

    private int mana = 0;
    private int maxMana;
    private IOverride tileEntity;

    /**
     * コンストラクター。
     * @param maxMana マナの容量。
     * @param tileEntity タイルエンティティ。
     */
    public SparkManager(int maxMana, IOverride tileEntity) {
        this.maxMana = maxMana;
        this.tileEntity = tileEntity;
    }

    void readNbt(NBTTagCompound compound) {
        mana = compound.getInteger("mana");
    }

    void writeNbt(NBTTagCompound compound) {
        compound.setInteger("mana", mana);
    }

    /**
     * スパークの更新。
     */
    public void sparkUpDate() {
        ISparkEntity spark = getAttachedSpark();
        if (spark == null) {
            return;
        }
        List<ISparkEntity> sparkEntities = SparkHelper.getSparksAround(tileEntity.getWorldObj(),
                tileEntity.getPostion().getX() + 0.5,
                tileEntity.getPostion().getY() + 0.5,
                tileEntity.getPostion().getZ() + 0.5);
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

    public void useMana(int useMana) {
        this.mana -= useMana;
    }

    @Override
    public boolean isFull() {
        return mana >= maxMana;
    }

    @Override
    public void recieveMana(int mana) {
        this.mana += mana;

    }

    @Override
    public boolean canRecieveManaFromBursts() {
        return tileEntity.canRecieveMana();
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
        return Math.max(0, maxMana - getCurrentMana());
    }

    @Override
    public ISparkEntity getAttachedSpark() {
        List<Entity> sparks = tileEntity.getWorldObj().getEntitiesWithinAABB(Entity.class,
                new AxisAlignedBB(tileEntity.getPostion().up(),
                        tileEntity.getPostion().up().add(1, 1, 1)),
                Predicates.instanceOf(ISparkEntity.class));
        if (sparks.size() == 1) {
            Entity e = sparks.get(0);
            return (ISparkEntity) e;
        }

        return null;
    }

    @Override
    public boolean areIncomingTranfersDone() {
        return !(tileEntity.canRecieveMana());
    }

}
