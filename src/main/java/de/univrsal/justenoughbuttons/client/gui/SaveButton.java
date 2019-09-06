package de.univrsal.justenoughbuttons.client.gui;

import net.minecraft.client.gui.widget.button.AbstractButton;

public class SaveButton extends AbstractButton {
    public int id;

    public SaveButton(int id, int x, int y, int w, int h, String txt) {
        super(x, y, w, h, txt);
        this.id = id;
    }

    @Override
    public void onPress() {

    }
}
