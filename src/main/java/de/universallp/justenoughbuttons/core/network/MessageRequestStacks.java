package de.universallp.justenoughbuttons.core.network;

import de.universallp.justenoughbuttons.JEIButtons;
import de.universallp.justenoughbuttons.client.Localization;
import de.universallp.justenoughbuttons.core.handlers.ConfigHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by universal on 16.09.16 16:12.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
public class MessageRequestStacks implements IMessage, IMessageHandler<MessageRequestStacks, IMessage> {

    private static final int MAX_REQUEST_SIZE = 32767;
    private NBTTagCompound[] mainInventory;
    private NBTTagCompound[] armorInventory;
    private NBTTagCompound offHand;


    public MessageRequestStacks() { }

    public MessageRequestStacks(NBTTagCompound[] main, NBTTagCompound[] armor, NBTTagCompound off) {
        this.mainInventory = main;
        this.armorInventory = armor;
        this.offHand = off;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        if (buf.readBoolean()) {
            mainInventory = new NBTTagCompound[buf.readByte()];
            for (int i = 0; i < mainInventory.length; i++)
                mainInventory[i] = ByteBufUtils.readTag(buf);
        }

        if (buf.readBoolean()) {
            armorInventory = new NBTTagCompound[buf.readByte()];
            for (int i = 0; i < armorInventory.length; i++)
                armorInventory[i] = ByteBufUtils.readTag(buf);
        }

        if (buf.readBoolean())
            offHand = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        boolean flag = mainInventory != null && mainInventory.length > 0;
        buf.writeBoolean(flag);

        if (flag) {
            buf.writeByte(mainInventory.length);
            for (NBTTagCompound tag : mainInventory) {
                if (tag.toString().getBytes().length < MAX_REQUEST_SIZE)
                    ByteBufUtils.writeTag(buf, tag);
                else {
                    JEIButtons.logInfo("That NBT tag was just too long.");
                    ByteBufUtils.writeTag(buf, null);
                }
            }
        }

        flag = armorInventory != null && armorInventory.length > 0;
        buf.writeBoolean(flag);

        if (flag) {
            buf.writeByte(armorInventory.length);
            for (NBTTagCompound tag : armorInventory)
                ByteBufUtils.writeTag(buf, tag);
        }

        flag = offHand != null;
        buf.writeBoolean(flag);

        if (flag)
            ByteBufUtils.writeTag(buf, offHand);
    }

    @Override
    public IMessage onMessage(MessageRequestStacks message, MessageContext ctx) {
        EntityPlayerMP p = ctx.getServerHandler().playerEntity;

        if (p != null) {
            boolean isOP = MessageExecuteButton.checkPermissions(p, p.mcServer);

            if (ConfigHandler.saveRequireOP && !isOP) {
                ITextComponent msg = new TextComponentTranslation(Localization.NO_PERMISSIONS);
                msg.setStyle(msg.getStyle().setColor(TextFormatting.RED));
                p.sendMessage(msg);
                return null;
            }

            p.inventory.clear();
            if (message.mainInventory != null)
                for (int i = 0; i < message.mainInventory.length; i++) {
                    if (message.mainInventory[i] != null) {
                        p.inventory.mainInventory.set(i, new ItemStack(message.mainInventory[i]));
                    }
                }

            if (message.armorInventory != null)
                for (int i = 0; i < message.armorInventory.length; i++) {
                    if (message.armorInventory[i] != null) {
                        p.inventory.armorInventory.set(i, new ItemStack(message.armorInventory[i]));
                    }
                }

            if (message.offHand != null) {
                p.inventory.offHandInventory.set(0, new ItemStack(message.offHand));
            }
        }

        p.inventory.markDirty();
        p.inventoryContainer.detectAndSendChanges();
        return null;
    }
}
