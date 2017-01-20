package de.universallp.justenoughbuttons.client;

import de.universallp.justenoughbuttons.JEIButtons;
import de.universallp.justenoughbuttons.core.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
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
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.FileNotFoundException;

/**
 * Created by universallp on 11.08.2016 16:02.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/UniversalLP/JustEnoughButtons
 */
public class ClientProxy extends CommonProxy {

    private static final String KEY_CATEGORY = "key.category.justenoughbuttons";
    private static final String KEY_MAKECOPY = "justenoughbuttons.key.makecopy";
    private static final String KEY_MOBOVERLAY = "justenoughbuttons.key.mobOverlay";
    private static final String KEY_CHUNKOVERLAY = "justenoughbuttons.key.chunkOverlay";
    private static final String KEY_HIDE_OVERLAY = "justenoughbuttons.key.hideall";

    public static KeyBinding makeCopyKey = new KeyBinding(KEY_MAKECOPY, Keyboard.KEY_C, KEY_CATEGORY);
    public static KeyBinding hideall = new KeyBinding(KEY_HIDE_OVERLAY, KeyConflictContext.GUI, KeyModifier.CONTROL, Keyboard.KEY_H, KEY_CATEGORY);
    public static KeyBinding mobOverlay;
    public static KeyBinding chunkOverlay;
    public static Minecraft mc;
    public static EntityPlayerSP player;
    public static RenderManager renderManager;
    public static SaveFileHandler saveHandler;

    public static final String[] GUI_TOP = new String[] { "r", "field_147009_r", "guiTop" };
    public static final String[] SERVER_LIST_DATA = new String[] {};

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

        if (Loader.isModLoaded("jei")) {
            JEIButtons.logInfo("JEI is installed Mod subsets are enabled!");
            ModSubsetButtonHandler.ENABLE_SUBSETS = true;
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
        ClientRegistry.registerKeyBinding(hideall);

        if (!Loader.isModLoaded(JEIButtons.MOD_MOREOVERLAYS)) {
            mobOverlay = new KeyBinding(KEY_MOBOVERLAY, Keyboard.KEY_F7, KEY_CATEGORY);
            chunkOverlay = new KeyBinding(KEY_CHUNKOVERLAY, Keyboard.KEY_F4, KEY_CATEGORY);

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
