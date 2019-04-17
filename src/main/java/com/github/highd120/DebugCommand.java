package com.github.highd120;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;

/**
 * デバック用のコマンド。
 * @author hdgam
 *
 */
public class DebugCommand implements ICommand {
    /**
     * デバック出力。
     * @param obj データ。
     */
    public static void debug(Object obj) {
        if (obj != null) {
            Minecraft.getMinecraft().thePlayer
                    .addChatComponentMessage(new TextComponentString(obj.toString()));
        }
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "debugEx";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/debugEx";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
            throws CommandException {
        if (!(sender instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) sender;
        ItemStack stack = player.getHeldItemMainhand();
        if (stack != null && stack.getTagCompound() != null) {
            printInfo(stack.getTagCompound(), player);
        } else {
            RayTraceResult result = player.rayTrace(10, server.getTickCounter());
            if (result.getBlockPos() == null) {
                return;
            }
            TileEntity tile = server.getEntityWorld().getTileEntity(result.getBlockPos());
            if (tile == null) {
                return;
            }
            NBTTagCompound tag = new NBTTagCompound();
            tile.writeToNBT(tag);
            printInfo(tag, player);
        }
    }

    private static void printInfo(NBTTagCompound tag, EntityPlayer player) {
        String code = tag.toString()
                .replaceAll("\\[", "{")
                .replaceAll("\\]", "}")
                .replaceAll("(\\{|,)([a-zA-Z0-9]+):", "$1\"$2\":")
                .replaceAll("([0-9]+)[a-zA-Z]", "$1");
        player.addChatMessage(new TextComponentString(code));
        Toolkit kit = Toolkit.getDefaultToolkit();
        Clipboard clip = kit.getSystemClipboard();
        StringSelection ss = new StringSelection(code);
        clip.setContents(ss, null);

    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if (sender instanceof EntityPlayer) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender,
            String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

}
