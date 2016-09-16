package de.universallp.justenoughbuttons.core;

import de.universallp.justenoughbuttons.JEIButtons;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.Level;

/**
 * Created by universallp on 16.09.16 16:17.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/UniversalLP/JustEnoughButtons
 */
public class MessageNotifyClient implements IMessage, IMessageHandler<MessageNotifyClient, IMessage> {

    public MessageNotifyClient() { }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public IMessage onMessage(MessageNotifyClient message, MessageContext ctx) {
        JEIButtons.isServerSidePresent = true;
        FMLLog.log(JEIButtons.MODID, Level.INFO, "JustEnoughButtons is present on server side. Allowing inventory snapshot whith complex NBT");
        return null;
    }
}
