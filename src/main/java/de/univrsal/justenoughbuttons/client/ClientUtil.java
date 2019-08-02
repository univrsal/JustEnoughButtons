package de.univrsal.justenoughbuttons.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.SoundEvents;

public class ClientUtil {

    public static Minecraft mc = Minecraft.getInstance();

    public static int getMouseX() {
        return (int) mc.mouseHelper.getMouseX();
    }

    public static int getMouseY() {
        return (int) mc.mouseHelper.getMouseY();
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

    public static int getGuiTop(GuiContainer g) {
//        int i =  ReflectionHelper.getPrivateValue(GuiContainer.class, g, GUI_TOP);
//        return ReflectionHelper.getPrivateValue(GuiContainer.class, g, GUI_TOP);
        return 10; /* TODO: reflection */
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
        mc.getSoundHandler().play(SimpleSound.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
