package de.univrsal.justenoughbuttons.core;

import de.univrsal.justenoughbuttons.core.handlers.ClientNotifyHandler;
import de.univrsal.justenoughbuttons.core.handlers.MagnetModeHandler;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

/**
 * Created by universal on 11.08.2016 16:02.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
public class CommonProxy implements IProxy {

    //TODO: config
//    public void preInit(FMLPreInitializationEvent e) {
//        de.univrsal.justenoughbuttons.core.handlers.ConfigHandler.loadConfig(e.getSuggestedConfigurationFile());
//    }

    @Override
    public void commonSetup(FMLCommonSetupEvent e) {
        // TODO: network
//        INSTANCE.registerMessage(MessageNotifyClient.class, MessageNotifyClient.class, 0, Side.CLIENT);
//        INSTANCE.registerMessage(MessageRequestStacks.class, MessageRequestStacks.class, 1, Side.SERVER);
//        INSTANCE.registerMessage(MessageMagnetMode.class, MessageMagnetMode.class, 2, Side.SERVER);
//        INSTANCE.registerMessage(MessageExecuteButton.class, MessageExecuteButton.class, 3, Side.SERVER);
        MinecraftForge.EVENT_BUS.register(new MagnetModeHandler());
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientNotifyHandler());
    }

    @Override
    public World getClientWorld() {
        throw new IllegalStateException("Method is client side only");
    }
}
