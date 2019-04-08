package com.github.highd120.block;

import java.util.List;
import java.util.Optional;

import org.lwjgl.opengl.GL11;

import com.github.highd120.Lexicon;
import com.github.highd120.list.FluidList;
import com.github.highd120.list.SoundList;
import com.github.highd120.network.NetworkCreateItemEffect;
import com.github.highd120.network.NetworkHandler;
import com.github.highd120.util.CollectionUtil;
import com.github.highd120.util.MathUtil;
import com.github.highd120.util.NbtTagUtil;
import com.github.highd120.util.WorldUtil;
import com.github.highd120.util.subtile.SubTileRegister;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.sound.BotaniaSoundEvents;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.network.PacketBotaniaEffect;

/**
 * マナのコンバーター。
 * @author hdgam
 */
@SubTileRegister(name = SubTileCreateManaFluid.NAME)
public class SubTileCreateManaFluid extends SubTileFunctional {
    public static final String NAME = "createmanafluid";
    private static final int RANGE = 1;

    private static enum State {
        Create, Generate
    }

    State state;

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (mana < getMaxMana()) {
            return;
        }
        List<EntityItem> items = getWorld().getEntitiesWithinAABB(EntityItem.class,
                MathUtil.getAxisAlignedCube(getPos(), RANGE));
        if (state == State.Create) {
            Optional<EntityItem> waterBucket = CollectionUtil.findIf(items,
                    item -> item.getEntityItem().getItem() == Items.WATER_BUCKET);
            waterBucket.ifPresent(item -> {
                if (!getWorld().isRemote) {
                    ItemStack stack = UniversalBucket.getFilledBucket(
                            ForgeModContainer.getInstance().universalBucket,
                            FluidList.manaFluid);
                    EntityItem result = new EntityItem(getWorld(), getPos().getX() + 0.5,
                            getPos().getY() + 1, getPos().getZ() + 0.5, stack);
                    getWorld().spawnEntityInWorld(result);
                    result.setNoGravity(true);
                    result.setVelocity(0, 0, 0);
                    getStaging();
                    mana = 0;
                    item.setDead();
                }
            });
        } else {

            Optional<EntityItem> manaBucket = CollectionUtil.findIf(items, item -> {
                String name = NbtTagUtil.getString("FluidName", item.getEntityItem()).orElse("");
                return name.equals("ex_mana_fluid");
            });
            manaBucket.ifPresent(item -> {
                if (sendMana()) {
                    if (!getWorld().isRemote) {
                        ItemStack stack = new ItemStack(Items.WATER_BUCKET);
                        EntityItem result = new EntityItem(getWorld(), getPos().getX(),
                                getPos().getY(), getPos().getZ(), stack);
                        getWorld().spawnEntityInWorld(result);
                        item.setDead();
                    }
                }
            });
        }
    }

    private void sendStaging(TileEntity tileAt) {
        if (getWorld().isRemote) {
            return;
        }
        WorldUtil.playBotaniaEffect(getWorld(), PacketBotaniaEffect.EffectType.BLACK_LOTUS_DISSOLVE,
                tileAt.getPos(), new Vec3d(0.5, 0.5, 0.5));
        SoundList.playSoundBlock(getWorld(), BotaniaSoundEvents.blackLotus, getPos());
    }

    private void getStaging() {
        NetworkHandler.sendToNearby(getWorld(), getPos().add(0, 1, 0),
                new NetworkCreateItemEffect(getPos().add(0, 1, 0)));
        if (!getWorld().isRemote) {
            SoundList.playSoundBlock(getWorld(), BotaniaSoundEvents.blackLotus, getPos());
        }
    }

    /**
     * マナの送信。
     * @return 送信に成功したか。
     */
    private boolean sendMana() {
        for (EnumFacing dir : EnumFacing.HORIZONTALS) {
            TileEntity tileAt = getWorld().getTileEntity(getPos().offset(dir));
            if (tileAt != null && tileAt instanceof IManaPool && !tileAt.isInvalid()) {
                IManaReceiver receiver = (IManaReceiver) tileAt;
                if (!receiver.isFull()) {
                    if (!getWorld().isRemote) {
                        receiver.recieveMana(getMaxMana());
                    }
                    sendStaging(tileAt);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getMaxMana() {
        return 100000;
    }

    @Override
    public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(toBlockPos(), RANGE);
    }

    @Override
    public boolean onWanded(EntityPlayer player, ItemStack wand) {
        if (player == null) {
            return false;
        }
        if (player.isSneaking()) {
            state = state == State.Create ? State.Generate : State.Create;
            sync();
            return true;
        }
        return super.onWanded(player, wand);
    }

    @Override
    public void readFromPacketNBT(NBTTagCompound cmp) {
        super.readFromPacketNBT(cmp);
        state = cmp.getBoolean(NAME) ? State.Create : State.Generate;
    }

    @Override
    public void writeToPacketNBT(NBTTagCompound cmp) {
        super.writeToPacketNBT(cmp);
        cmp.setBoolean(NAME, state == State.Create);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUD(Minecraft mc, ScaledResolution res) {
        super.renderHUD(mc, res);

        int color = getColor();
        String filter = state == State.Create ? "Create" : "Generate";
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int x = res.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(filter) / 2;
        int y = res.getScaledHeight() / 2 + 30;

        mc.fontRendererObj.drawStringWithShadow(filter, x, y, color);
        GlStateManager.disableBlend();
    }

    @Override
    public LexiconEntry getEntry() {
        return Lexicon.createManaFlowerEntry;
    }
}
