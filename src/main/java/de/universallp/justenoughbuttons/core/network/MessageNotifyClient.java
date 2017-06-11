package de.universallp.justenoughbuttons.core.network;

import de.universallp.justenoughbuttons.JEIButtons;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by universal on 16.09.16 16:17.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
public class MessageNotifyClient implements IMessage, IMessageHandler<MessageNotifyClient, IMessage> {

    private boolean isSpongePresent;

    public MessageNotifyClient() { }

    public MessageNotifyClient(boolean isSpongePresent) {
        this.isSpongePresent = isSpongePresent;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.isSpongePresent = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isSpongePresent);
    }

    @Override
    public IMessage onMessage(MessageNotifyClient message, MessageContext ctx) {
        JEIButtons.isServerSidePresent = true;
        if (message.isSpongePresent)
            JEIButtons.logInfo("Sponge support is enabled for this server!");
        JEIButtons.isSpongePresent = message.isSpongePresent;
        JEIButtons.logInfo("JustEnoughButtons is present on server side. Allowing inventory snapshot whith complex NBT");
        return null;
    }
}
