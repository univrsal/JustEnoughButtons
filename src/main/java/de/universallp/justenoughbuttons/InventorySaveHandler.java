package de.universallp.justenoughbuttons;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by universal on 12.09.16.
 */
public class InventorySaveHandler {

    private static GuiButton[] saveButtons = new GuiButton[4];
    private static ItemStack[] save1;

    public static void init() {
        for (int i = 0; i < saveButtons.length; i++) {
            saveButtons[i] = new GuiButton(i, 5, 110 + 22 * i, 50, 20, "Save " + (i + 1));
        }
    }

    public static void click(int mouseX, int mouseY, int button) {

    }

    public static void drawButtons(int mouseX, int mouseY) {
        for (GuiButton s : saveButtons)
            s.drawButton(ClientProxy.mc, mouseX, mouseY);
    }
}
