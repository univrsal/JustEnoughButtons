package de.univrsal.justenoughbuttons.client.gui;

import de.univrsal.justenoughbuttons.JEIButtons;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by universal on 12.08.2016 14:45.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
public class GuiJEBConfig extends Screen {

    private static final List<String> buttonorder = new ArrayList<>();

    protected GuiJEBConfig(ITextComponent p_i51108_1_) {
        super(p_i51108_1_);
    }

//    public static List<IConfigElement> getElements() {
//        buttonorder.clear();
//        for (int i = 0; i < JEIButtons.btnCustom.length; i++) {
//            buttonorder.add("enableCustomButton." + i);
//            buttonorder.add("customName." + i);
//            buttonorder.add("customCommand." + i);
//        }
//
//        List<IConfigElement> entries = new ArrayList<IConfigElement>();

//        for (String name : ConfigHandler.config.getCategoryNames())
//            if (name.equals(ConfigHandler.CATEGORY_CUSTOM))
//                entries.add(new ConfigElement(ConfigHandler.config.getCategory(name).setPropertyOrder(buttonorder)));
//            else
//                entries.add(new ConfigElement(ConfigHandler.config.getCategory(name)));


//        return entries;
//    }
}
