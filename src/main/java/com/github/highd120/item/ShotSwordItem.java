package com.github.highd120.item;

import java.util.List;

import com.github.highd120.achievement.AchievementsList;
import com.github.highd120.entity.EntitySword;
import com.github.highd120.util.Constant;
import com.github.highd120.util.EntityUtil;
import com.github.highd120.util.MathUtil;
import com.github.highd120.util.NbtTagUtil;
import com.github.highd120.util.item.ItemRegister;
import com.google.common.base.Predicate;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.achievement.ICraftAchievement;

/**
 * 剣を飛ばすアイテム。
 * @author hdgam
 */
@ItemRegister(name = "shot_sword")
public class ShotSwordItem extends ItemBase implements IManaUsingItem, ICraftAchievement {
    public static String HOMING_TAG = "HOMING";

    /**
     * コンストラクター。
     */
    public ShotSwordItem() {
        setMaxStackSize(1);
        setNoRepair();
        setCreativeTab(CreativeTabs.COMBAT);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world,
            EntityPlayer player, EnumHand hand) {
        if (!world.isRemote
                && ManaItemHandler.requestManaExactForTool(itemStack, player, 1000, true)) {
            NBTTagCompound child = NbtTagUtil.getCompound(Constant.SHOT_SWORD_TAG, itemStack);
            NBTTagCompound entityTag = new NBTTagCompound();
            EntitySword sword = new EntitySword(world, player);
            world.spawnEntityInWorld(sword);
            sword.writeEntityToNBT(entityTag);
            entityTag.setTag(Constant.SHOT_SWORD_TAG, child);
            sword.readEntityFromNBT(entityTag);
            if (itemStack.getTagCompound().hasKey(HOMING_TAG)) {
                homing(world, player, sword);
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
    }

    private void homing(World world, EntityPlayer player, EntitySword sword) {
        Predicate<Entity> filter = entity -> entity instanceof EntityLivingBase
                && entity instanceof IMob && !(entity instanceof EntityPlayer);

        List<Entity> entities = world.getEntitiesInAABBexcluding(player,
                MathUtil.getAxisAlignedCube(player.getPosition(), 50), filter);

        if (entities.size() > 0) {
            Entity target = entities.get(0);
            Vec3d targetPos = EntityUtil.getPositon(target);
            Vec3d playerPos = EntityUtil.getPositon(player);
            Vec3d vector = targetPos.subtract(playerPos);
            sword.setThrowableHeading(vector.xCoord, vector.yCoord, vector.zCoord, 30.0f,
                    1.0f);
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip,
            boolean advanced) {
        NBTTagCompound child = NbtTagUtil.getCompound(Constant.SHOT_SWORD_TAG, stack);
        ItemStack inner = ItemStack.loadItemStackFromNBT(child);
        if (inner != null) {
            tooltip.add(">" + inner.getDisplayName());
        }
        if (NbtTagUtil.getCompound(stack).hasKey(HOMING_TAG)) {
            tooltip.add("HORMING");
        }
    }

    @Override
    public boolean usesMana(ItemStack stack) {
        return true;
    }

    @Override
    public Achievement getAchievementOnCraft(ItemStack arg0, EntityPlayer arg1, IInventory arg2) {
        return AchievementsList.shotSword;
    }
}
