package de.univrsal.justenoughbuttons.client.gui;

import net.minecraft.client.gui.GuiButton;

public class GuiButtonJEB extends GuiButton {
    public GuiButtonJEB(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public GuiButtonJEB(int buttonId, int x, int y, int w, int h, String text) {
        super(buttonId, x, y, w, h, text);
    }
}
