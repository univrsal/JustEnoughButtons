package de.universallp.justenoughbuttons.client;

import de.universallp.justenoughbuttons.JEIButtons;
import de.universallp.justenoughbuttons.client.handlers.EventHandlers;
import de.universallp.justenoughbuttons.client.handlers.InventorySaveHandler;
import de.universallp.justenoughbuttons.client.handlers.ModSubsetButtonHandler;
import de.universallp.justenoughbuttons.client.handlers.SaveFileHandler;
import de.universallp.justenoughbuttons.core.*;
import de.universallp.justenoughbuttons.core.handlers.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.FileNotFoundException;

/**
 * Created by universal on 11.08.2016 16:02.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
public class ClientProxy extends CommonProxy {
    public static KeyBinding makeCopyKey = new KeyBinding(Localization.KEY_MAKECOPY, KeyConflictContext.GUI, Keyboard.KEY_C, Localization.KEY_CATEGORY);
    public static KeyBinding hideAll = new KeyBinding(Localization.KEY_HIDE_OVERLAY, KeyConflictContext.GUI, KeyModifier.CONTROL, Keyboard.KEY_H, Localization.KEY_CATEGORY);

    public static KeyBinding mobOverlay;
    public static KeyBinding chunkOverlay;

    public static Minecraft mc;
    public static EntityPlayerSP player;
    public static RenderManager renderManager;
    public static SaveFileHandler saveHandler;

    public static final String[] GUI_TOP = new String[] { "s", "field_147009_r", "guiTop" };

    private static void versionCheck() {
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setString("curseProjectName", "just-enough-buttons");
        compound.setString("curseFilenameParser", "justenoughbuttons-" + ForgeVersion.mcVersion + "-[].jar");
        FMLInterModComms.sendRuntimeMessage(JEIButtons.MODID, "VersionChecker", "addCurseCheck", compound);
    }

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        versionCheck();
        super.preInit(e);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new EventHandlers());
        mc = Minecraft.getMinecraft();
        renderManager = mc.getRenderManager();
        InventorySaveHandler.init();
        saveHandler = new SaveFileHandler().init();

        super.init(e);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        try {
            ClientProxy.saveHandler.loadForPlayer();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        JEIButtons.setUpPositions();
        if (Loader.isModLoaded(JEIButtons.MOD_JEI) || Loader.isModLoaded(JEIButtons.MOD_JEI.toUpperCase())) {
            JEIButtons.logInfo("JEI is installed Mod subsets are enabled!");
            ModSubsetButtonHandler.ENABLE_SUBSETS = true;
            ModSubsetButtonHandler.setupModList();
        }
    }

    @Override
    public int getMouseX() {
        ScaledResolution resolution = new ScaledResolution(ClientProxy.mc);
        int mX = Mouse.getX() * resolution.getScaledWidth() / ClientProxy.mc.displayWidth;
        return mX + 1;
    }

    @Override
    public int getMouseY() {
        ScaledResolution resolution = new ScaledResolution(ClientProxy.mc);
        int mY = resolution.getScaledHeight() - Mouse.getY() * resolution.getScaledHeight() / ClientProxy.mc.displayHeight - 1;
        return mY;
    }

    public static int getGuiTop(GuiContainer g) {
        int i =  ReflectionHelper.getPrivateValue(GuiContainer.class, g, GUI_TOP);
        return ReflectionHelper.getPrivateValue(GuiContainer.class, g, GUI_TOP);
    }

    @Override
    public int getScreenWidth() {
        ScaledResolution resolution = new ScaledResolution(ClientProxy.mc);
        return resolution.getScaledWidth();
    }

    @Override
    public int getScreenHeight() {
        ScaledResolution resolution = new ScaledResolution(ClientProxy.mc);
        return resolution.getScaledHeight();
    }

    @Override
    public void registerKeyBind() {
        ClientRegistry.registerKeyBinding(makeCopyKey);
        ClientRegistry.registerKeyBinding(hideAll);

        if (!Loader.isModLoaded(JEIButtons.MOD_MOREOVERLAYS) && !Loader.isModLoaded(JEIButtons.MOD_DYN_SOURROUND) && ConfigHandler.registerUtilKeybinds) {
            mobOverlay = new KeyBinding(Localization.KEY_MOBOVERLAY, KeyConflictContext.IN_GAME, Keyboard.KEY_F7, Localization.KEY_CATEGORY);
            chunkOverlay = new KeyBinding(Localization.KEY_CHUNKOVERLAY, KeyConflictContext.IN_GAME, Keyboard.KEY_F4, Localization.KEY_CATEGORY);

            ClientRegistry.registerKeyBinding(chunkOverlay);
            ClientRegistry.registerKeyBinding(mobOverlay);
        } else {
            JEIButtons.enableOverlays = false;
            JEIButtons.logInfo("MoreOverlays is loaded. Disabling Lightlevel and Chunk Overlay!");
        }
    }

    @Override
    public void playClick() {
        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

}
