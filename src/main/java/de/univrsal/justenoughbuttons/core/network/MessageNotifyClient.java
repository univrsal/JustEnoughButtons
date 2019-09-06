package de.univrsal.justenoughbuttons.core.network;

import de.univrsal.justenoughbuttons.JEIButtons;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Created by universal on 16.09.16 16:17.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
public class MessageNotifyClient implements IMessage {

    private boolean isSpongePresent;

    public MessageNotifyClient() { }

    public MessageNotifyClient(boolean isSpongePresent) {
        this.isSpongePresent = isSpongePresent;
    }

    @Override
    public boolean receive(NetworkEvent.Context context) {
        JEIButtons.isServerSidePresent = true;
        if (isSpongePresent)
            JEIButtons.logInfo("Sponge support is enabled for this server!");
        JEIButtons.isSpongePresent = isSpongePresent;
        JEIButtons.logInfo("JustEnoughButtons is present on server side. Allowing inventory snapshot whith complex NBT");
        return true;
    }

}
