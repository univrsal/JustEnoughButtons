package de.univrsal.justenoughbuttons.core;

import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public interface IProxy {
    World getClientWorld();

    void commonSetup(FMLCommonSetupEvent e);

    void serverStarting(FMLServerStartingEvent event);
}
