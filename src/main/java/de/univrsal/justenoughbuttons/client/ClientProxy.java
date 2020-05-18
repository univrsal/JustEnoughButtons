package de.univrsal.justenoughbuttons.client;

import de.univrsal.justenoughbuttons.JEIButtons;
import de.univrsal.justenoughbuttons.client.handlers.EventHandlers;
import de.univrsal.justenoughbuttons.client.handlers.InventorySaveHandler;
import de.univrsal.justenoughbuttons.client.handlers.ModSubsetButtonHandler;
import de.univrsal.justenoughbuttons.client.handlers.SaveFileHandler;
import de.univrsal.justenoughbuttons.core.CommonProxy;
import de.univrsal.justenoughbuttons.core.handlers.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.io.FileNotFoundException;

/**
 * Created by universal on 11.08.2016 16:02.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
public class ClientProxy extends CommonProxy {
    public static KeyBinding makeCopyKey = new KeyBinding(Localization.KEY_MAKECOPY, KeyConflictContext.GUI,
            InputMappings.getInputByName("key.keyboard.c"), Localization.KEY_CATEGORY);
    public static KeyBinding hideAll = new KeyBinding(Localization.KEY_HIDE_OVERLAY, KeyConflictContext.GUI,
            KeyModifier.CONTROL, InputMappings.getInputByName("key.keyboard.h"), Localization.KEY_CATEGORY);

    public static KeyBinding mobOverlay;
    public static KeyBinding chunkOverlay;

    public static Minecraft mc;
    public static PlayerEntity player;
    public static EntityRendererManager renderManager;
    public static SaveFileHandler saveHandler;

    public static final String[] GUI_TOP = new String[] { "s", "field_147009_r", "guiTop" };

    private static void versionCheck() {
//        final NBTTagCompound compound = new NBTTagCompound();
//        compound.setString("curseProjectName", "just-enough-buttons");
//        compound.setString("curseFilenameParser", "justenoughbuttons-" + ForgeVersion.mcVersion + "-[].jar");
//        FMLInterModComms.sendRuntimeMessage(JEIButtons.MODID, "VersionChecker", "addCurseCheck", compound);
    }

    @Override
    public void commonSetup(FMLCommonSetupEvent e) {
        super.commonSetup(e);
        registerKeyBind();
        MinecraftForge.EVENT_BUS.register(new EventHandlers());
        mc = Minecraft.getInstance();
        renderManager = mc.getRenderManager();
        InventorySaveHandler.init();
        saveHandler = new SaveFileHandler().init();

        try {
            ClientProxy.saveHandler.loadForPlayer();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        JEIButtons.setUpPositions();
        if (JEIButtons.isModLoaded(JEIButtons.MOD_JEI) || JEIButtons.isModLoaded(JEIButtons.MOD_JEI.toUpperCase())) {
            JEIButtons.logInfo("JEI is installed Mod subsets are enabled!");
            ModSubsetButtonHandler.ENABLE_SUBSETS = true;
            ModSubsetButtonHandler.setupModList();
        }
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        /* NO OP */
    }

    public void registerKeyBind() {
        ClientRegistry.registerKeyBinding(makeCopyKey);
        ClientRegistry.registerKeyBinding(hideAll);

        if (!JEIButtons.isModLoaded(JEIButtons.MOD_MOREOVERLAYS) &&
                !JEIButtons.isModLoaded(JEIButtons.MOD_DYN_SOURROUND) && ConfigHandler.COMMON.registerUtilKeybinds.get()) {
            mobOverlay = new KeyBinding(Localization.KEY_MOBOVERLAY, KeyConflictContext.IN_GAME,
                    InputMappings.getInputByName("key.keyboard.f7"), Localization.KEY_CATEGORY);
            chunkOverlay = new KeyBinding(Localization.KEY_CHUNKOVERLAY, KeyConflictContext.IN_GAME,
                    InputMappings.getInputByName("key.keyboard.f4"), Localization.KEY_CATEGORY);

            ClientRegistry.registerKeyBinding(chunkOverlay);
            ClientRegistry.registerKeyBinding(mobOverlay);
        } else {
            JEIButtons.enableOverlays = false;
            JEIButtons.logInfo("MoreOverlays is loaded. Disabling Lightlevel and Chunk Overlay!");
        }
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }
}
