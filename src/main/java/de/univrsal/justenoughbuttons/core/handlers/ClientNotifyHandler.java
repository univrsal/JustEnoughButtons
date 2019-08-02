package de.univrsal.justenoughbuttons.core.handlers;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Created by universal on 17.09.16 20:37.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
public class ClientNotifyHandler {

    @SubscribeEvent
    public void onWorldJoin(EntityJoinWorldEvent e) {
//        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
//            if (e.getEntity() != null && e.getEntity() instanceof EntityPlayerMP)
//                CommonProxy.INSTANCE.sendTo(new MessageNotifyClient(Loader.isModLoaded("sponge")), (EntityPlayerMP) e.getEntity());
//        }
    }
}
