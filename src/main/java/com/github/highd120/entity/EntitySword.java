package com.github.highd120.entity;

import com.github.highd120.util.Constant;
import com.google.common.base.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * 剣のエンティティ。
 * @author hdgam
 */
public class EntitySword extends EntityArrow {
    private static final DataParameter<Optional<ItemStack>> ITEM = EntityDataManager
            .createKey(EntityArrow.class, DataSerializers.OPTIONAL_ITEM_STACK);

    public EntitySword(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.getDataManager().register(ITEM, Optional.absent());
    }

    public EntitySword(World worldIn) {
        super(worldIn);
        this.getDataManager().register(ITEM, Optional.absent());
    }

    /**
     * コンストラクター。
     * @param worldIn ワールド。
     * @param shooter 射撃者。
     */
    public EntitySword(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
        this.getDataManager().register(ITEM, Optional.absent());
        setAim(this.shootingEntity, this.shootingEntity.getPitchYaw().x,
                this.shootingEntity.getPitchYaw().y, 0, 30.0f, 1.0f);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        if (this.shootingEntity == null) {
            return;
        }
        setAim(this.shootingEntity, this.shootingEntity.getPitchYaw().x,
                this.shootingEntity.getPitchYaw().y, 0, 10.0f, 1.0f);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        ItemStack itemStack = ItemStack
                .loadItemStackFromNBT(compound.getCompoundTag(Constant.SHOT_SWORD_TAG));
        this.getDataManager().set(ITEM, Optional.fromNullable(itemStack));
        this.getDataManager().setDirty(ITEM);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        ItemStack itemStack = getItem();
        if (itemStack != null) {
            NBTTagCompound child = new NBTTagCompound();
            itemStack.writeToNBT(child);
            compound.setTag(Constant.SHOT_SWORD_TAG, child);
        }
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        Entity target = raytraceResultIn.entityHit;
        if (this.shootingEntity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) this.shootingEntity;
            if (target != player && player != null && target instanceof EntityLivingBase) {
                getItem().hitEntity((EntityLivingBase) target, player);
                if (getItem().getItem() instanceof ItemSword) {
                    float damage = 3.5F + ((ItemSword) getItem().getItem()).getDamageVsEntity();
                    player.addChatComponentMessage(new TextComponentString(Float.toString(damage)));
                    target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
                }
                this.setDead();
            } else if (target == null) {
                this.setDead();
            }
        }
    }

    /**
     * 登録されたアイテムの取得。
     * @return 登録されたアイテム。
     */
    public ItemStack getItem() {
        ItemStack itemStack = (ItemStack) ((Optional<ItemStack>) this.getDataManager().get(ITEM))
                .orNull();
        if (itemStack == null) {
            return new ItemStack(Items.ARROW);
        }
        return itemStack;
    }

    @Override
    protected ItemStack getArrowStack() {
        return null;
    }

}
