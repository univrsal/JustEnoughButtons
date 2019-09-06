package de.univrsal.justenoughbuttons.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundEvents;

public class ClientUtil {

    public static Minecraft mc = Minecraft.getInstance();

    public static int getMouseX() {
        return (int) (mc.mouseHelper.getMouseX() * (double) mc.mainWindow.getScaledWidth() / (double) mc.mainWindow.getWidth());
    }

    public static int getMouseY() {
        return (int) (mc.mouseHelper.getMouseY() * (double) mc.mainWindow.getScaledHeight() / (double) mc.mainWindow.getHeight());
    }

    public static boolean lmbDown() {
        return mc.mouseHelper.isLeftDown();
    }

    public static boolean rmbDown() {
        return mc.mouseHelper.isRightDown();
    }

    public static int mouseButton() {
        return lmbDown() ? 0 : (rmbDown() ? 1 : -1);
    }

    public static int getScreenWidth() {
        if (mc.currentScreen != null)
            return mc.currentScreen.width;
        return -1;
    }

    public static int getScreenHeight() {
        if (mc.currentScreen != null)
            return mc.currentScreen.height;
        return -1;
    }

    public static void playClick() {
        mc.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
