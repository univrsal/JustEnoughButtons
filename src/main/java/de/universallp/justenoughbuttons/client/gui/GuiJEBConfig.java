package de.universallp.justenoughbuttons.client.gui;

import de.universallp.justenoughbuttons.core.handlers.ConfigHandler;
import de.universallp.justenoughbuttons.JEIButtons;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by universallp on 12.08.2016 14:45.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/UniversalLP/JustEnoughButtons
 */
public class GuiJEBConfig extends GuiConfig {

    private static final List buttonorder = new ArrayList();

    public GuiJEBConfig(GuiScreen parentScreen) {
        super(parentScreen, getElements(), JEIButtons.MODID, false, false, GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
    }

    public static List<IConfigElement> getElements() {
        buttonorder.clear();
        for (int i = 0; i < JEIButtons.btnCustom.length; i++) {
            buttonorder.add("enableCustomButton." + i);
            buttonorder.add("customName." + i);
            buttonorder.add("customCommand." + i);
        }

        List<IConfigElement> entries = new ArrayList<IConfigElement>();

        for (String name : ConfigHandler.config.getCategoryNames())
            if (name.equals(ConfigHandler.CATEGORY_CUSTOM))
                entries.add(new ConfigElement(ConfigHandler.config.getCategory(name).setPropertyOrder(buttonorder)));
            else
                entries.add(new ConfigElement(ConfigHandler.config.getCategory(name)));


        return entries;
    }
}
