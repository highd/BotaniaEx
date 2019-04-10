package com.github.highd120.item;

import com.github.highd120.util.NbtTagUtil;
import com.github.highd120.util.item.ItemRegister;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

@ItemRegister(name = "extra")
public class ItemExtra extends Item {
    public static enum Type {
        WIND("wind"), FIRE("fire"), DIRT("dirt"), WATER("water");

        private String name;

        Type(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }

    public ItemExtra() {
        addPropertyOverride(new ResourceLocation("botaniaex", "type"),
                (stack, worldIn, entityIn) -> getTypeData(stack));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn,
            EntityPlayer playerIn, EnumHand hand) {
        int data = NbtTagUtil.getInterger("type", itemStackIn).orElse(0);

        NbtTagUtil.setInterger("type", itemStackIn, (data + 1) % 16);
        playerIn.addChatMessage(new TextComponentString(Integer.toString(data)));

        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    /**
     * 属性を持っているか。
     * @param stack アイテムスタック。
     * @param type 属性。
     * @return  属性を持っているか。
     */
    public boolean isHasType(ItemStack stack, Type type) {
        int mask = 1 << type.ordinal();
        int data = NbtTagUtil.getInterger("type", stack).orElse(0);
        return (data & mask) >> type.ordinal() == 1;
    }

    public int getTypeData(ItemStack stack) {
        return NbtTagUtil.getInterger("type", stack).orElse(0);
    }
}
