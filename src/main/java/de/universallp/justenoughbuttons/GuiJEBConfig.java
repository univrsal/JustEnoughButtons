package de.universallp.justenoughbuttons;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by universallp on 12.08.2016 14:45.
 * This file is part of VanillaAutomation which is licenced
 * under the MOZILLA PUBLIC LICENSE 1.1
 * github.com/UniversalLP/VanillaAutomation
 */
public class GuiJEBConfig extends GuiConfig {

    public GuiJEBConfig(GuiScreen parentScreen) {
        super(parentScreen, getElements(), JEIButtons.MODID, false, false, GuiConfig.getAbridgedConfigPath(JEIButtons.ConfigHandler.config.toString()));
    }

    public static List<IConfigElement> getElements() {
        List<IConfigElement> sliders = new ArrayList<IConfigElement>();

        for (String name : JEIButtons.ConfigHandler.config.getCategoryNames())
                sliders.add(new ConfigElement(JEIButtons.ConfigHandler.config.getCategory(name)));

        return sliders;
    }
}
