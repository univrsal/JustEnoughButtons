package de.universallp.justenoughbuttons.core;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by universal on 16.09.16 16:12.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/UniversalLP/JustEnoughButtons
 */
public class MessageRequestStacks implements IMessage, IMessageHandler<MessageRequestStacks, IMessage> {

    public NBTTagCompound[] mainInventory;
    public NBTTagCompound[] armorInventory;
    public NBTTagCompound offHand;


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
            for (NBTTagCompound tag : mainInventory)
                ByteBufUtils.writeTag(buf, tag);
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
        EntityPlayer p = ctx.getServerHandler().playerEntity;

        if (p != null) {
            p.inventory.clear();
            if (message.mainInventory != null)
                for (int i = 0; i < message.mainInventory.length; i++) {
                    if (message.mainInventory[i] != null) {
                        p.inventory.mainInventory[i] = ItemStack.loadItemStackFromNBT(message.mainInventory[i]);
                    }
                }

            if (message.armorInventory != null)
                for (int i = 0; i < message.armorInventory.length; i++) {
                    if (message.armorInventory[i] != null) {
                        p.inventory.armorInventory[i] = ItemStack.loadItemStackFromNBT(message.armorInventory[i]);
                    }
                }

            if (message.offHand != null) {
                p.inventory.offHandInventory[0] = ItemStack.loadItemStackFromNBT(message.offHand);
            }
        }

        p.inventory.markDirty();
        p.inventoryContainer.detectAndSendChanges();
        return null;
    }
}
