package de.universallp.justenoughbuttons.core;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by universallp on 20.09.2016 14:31.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENSE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/UniversalLP/JEI Buttons
 */
public class MessageMagnetMode implements IMessage, IMessageHandler<MessageMagnetMode, IMessage> {

    public boolean removePlayer;

    public MessageMagnetMode() { }

    public MessageMagnetMode(boolean remove) {
        removePlayer = remove;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        removePlayer = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(removePlayer);
    }

    @Override
    public IMessage onMessage(MessageMagnetMode message, MessageContext ctx) {
        if (message.removePlayer)
            CommonProxy.MAGNET_MODE_HANDLER.removePlayer(ctx.getServerHandler().playerEntity);
        else
            CommonProxy.MAGNET_MODE_HANDLER.addPlayer(ctx.getServerHandler().playerEntity);

        return null;
    }
}
