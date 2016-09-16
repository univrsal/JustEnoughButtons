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

    public NBTTagCompound[] compounds;
    public byte[] slots;

    public MessageRequestStacks() { }

    public MessageRequestStacks(NBTTagCompound[] c, byte[] s) {
        this.compounds = c;
        this.slots = s;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        compounds = new NBTTagCompound[buf.readByte()];
        for (int i = 0; i < compounds.length; i++)
            compounds[i] = ByteBufUtils.readTag(buf);

        slots = new byte[buf.readByte()];
        for (int i = 0; i < slots.length; i++)
            slots[i] = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(compounds.length);
        for (NBTTagCompound tag : compounds)
            ByteBufUtils.writeTag(buf, tag);

        buf.writeByte(slots.length);
        for (byte b : slots)
            buf.writeByte(b);
    }

    @Override
    public IMessage onMessage(MessageRequestStacks message, MessageContext ctx) {
        EntityPlayer p = ctx.getServerHandler().playerEntity;

        if (p != null) {
            p.inventory.clear();
            for (int i = 0; i < message.compounds.length; i++) {
                if (message.compounds[i] != null)
                    p.inventory.setInventorySlotContents(message.slots[i], ItemStack.loadItemStackFromNBT(message.compounds[i]));
            }
        }
        p.inventory.markDirty();

        return null;
    }
}
