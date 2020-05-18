package de.univrsal.justenoughbuttons.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundEvents;

public class ClientUtil {

    public static Minecraft mc = Minecraft.getInstance();

    public static int getMouseX() {
        return (int) (mc.mouseHelper.getMouseX() * (double) mc.func_228018_at_().getHeight() / (double) mc.func_228018_at_().getWidth());
    }

    public static int getMouseY() {
        return (int) (mc.mouseHelper.getMouseY() * (double) mc.func_228018_at_().getScaledHeight() / (double) mc.func_228018_at_().getHeight());
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
