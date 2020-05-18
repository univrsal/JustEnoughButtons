package de.univrsal.justenoughbuttons.client.gui;

import de.univrsal.justenoughbuttons.client.ClientUtil;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SaveButton extends AbstractButton {
    public int id;
    protected final SaveButton.IPressable pressable;

    public SaveButton(int id, int x, int y, int w, int h, String txt, SaveButton.IPressable pressable) {
        super(x, y, w, h, txt);
        this.id = id;
        this.pressable = pressable;
    }

    @Override
    protected boolean isValidClickButton(int p_isValidClickButton_1_) {
        return p_isValidClickButton_1_ == 0 || p_isValidClickButton_1_ == 1;
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        boolean result = super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
        if (result) {
            pressable.onPress(this, p_mouseClicked_5_);
        }
        return result;
    }

    @Override
    public void onPress() {
        ClientUtil.playClick();
    }

    @OnlyIn(Dist.CLIENT)
    public interface IPressable {
        void onPress(SaveButton btn, int mouse_button);
    }
}
