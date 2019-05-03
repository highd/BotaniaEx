package com.github.highd120.item;

import javax.annotation.Nonnull;

import com.github.highd120.BotaniaExMain;
import com.github.highd120.gui.BasicInventory;
import com.github.highd120.util.item.ItemRegister;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@ItemRegister(name = "creative_editor")
public class ItemEditer extends ItemBase {

    private static final String TAG_ITEMS = "InvItems";

    public ItemEditer() {
        super();
        setMaxStackSize(1);
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound oldCapNbt) {
        return new BasicInventory(2);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (stack.getTagCompound() != null && stack.getTagCompound().hasKey(TAG_ITEMS)) {
            NBTTagList oldData = stack.getTagCompound().getTagList(TAG_ITEMS,
                    Constants.NBT.TAG_COMPOUND);
            IItemHandler newInv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                    null);

            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(newInv, null, oldData);

            stack.getTagCompound().removeTag(TAG_ITEMS);

            if (stack.getTagCompound().getSize() == 0) {
                stack.setTagCompound(null);
            }
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack stack, World world,
            EntityPlayer player, EnumHand hand) {
        player.openGui(BotaniaExMain.instance, 0, world, hand == EnumHand.OFF_HAND ? 1 : 0, 0, 0);
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

}
