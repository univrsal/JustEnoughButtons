package de.universallp.justenoughbuttons.core.handlers;

import de.universallp.justenoughbuttons.core.CommonProxy;
import de.universallp.justenoughbuttons.core.network.MessageNotifyClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by universal on 17.09.16 20:37.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/UniversalLP/JustEnoughButtons
 */
public class ClientNotifyHandler {

    @SubscribeEvent
    public void onWorldJoin(EntityJoinWorldEvent e) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            if (e.getEntity() != null && e.getEntity() instanceof EntityPlayerMP)
                CommonProxy.INSTANCE.sendTo(new MessageNotifyClient(Loader.isModLoaded("sponge")), (EntityPlayerMP) e.getEntity());
        }
    }
}
