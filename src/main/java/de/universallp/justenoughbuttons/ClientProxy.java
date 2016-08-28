package de.universallp.justenoughbuttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

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


    public static KeyBinding makeCopyKey = new KeyBinding(KEY_MAKECOPY, Keyboard.KEY_C, KEY_CATEGORY);
    public static KeyBinding mobOverlay = new KeyBinding(KEY_MOBOVERLAY, Keyboard.KEY_F7, KEY_CATEGORY);

    public static Minecraft mc;
    public static EntityPlayerSP player;

    @Override
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new EventHandlers());
        mc = Minecraft.getMinecraft();
        super.init(e);
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

    @Override
    public void registerKeyBind() {
        ClientRegistry.registerKeyBinding(makeCopyKey);
        ClientRegistry.registerKeyBinding(mobOverlay);
    }
}
