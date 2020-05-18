package de.univrsal.justenoughbuttons.core.network;

import de.univrsal.justenoughbuttons.client.Localization;
import de.univrsal.justenoughbuttons.core.CommonProxy;
import de.univrsal.justenoughbuttons.core.handlers.ConfigHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Created by universal on 20.09.2016 14:31.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENSE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JEI Buttons
 */
public class MessageMagnetMode implements IMessage {

    public boolean removePlayer;

    public MessageMagnetMode() { }

    public MessageMagnetMode(boolean remove) {
        removePlayer = remove;
    }

    @Override
    public boolean receive(NetworkEvent.Context context) {
        ServerPlayerEntity p = context.getSender();
        MinecraftServer s = p.getServer();
        if (s == null)
            return false;

        boolean isOP = MessageExecuteButton.checkPermissions(p, s);
        ITextComponent msg = new TranslationTextComponent(Localization.NO_PERMISSIONS);
        msg.setStyle(msg.getStyle().setColor(TextFormatting.RED));

        if (!isOP && ConfigHandler.COMMON.magnetRequiresOP.get()) {
            p.sendMessage(msg);
            return false;
        }

        if (removePlayer)
            CommonProxy.MAGNET_MODE_HANDLER.removePlayer(p);
        else
            CommonProxy.MAGNET_MODE_HANDLER.addPlayer(p);

        return true;
    }
}
