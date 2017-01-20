package de.universallp.justenoughbuttons.core;

import de.universallp.justenoughbuttons.JEIButtons;
import de.universallp.justenoughbuttons.client.ClientProxy;
import de.universallp.justenoughbuttons.client.Localization;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by universallp on 12.09.16 15:02.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/UniversalLP/JustEnoughButtons
 */
public class ModSubsetButtonHandler {
    public static boolean ENABLE_SUBSETS = false;
    static boolean isListShown = false;
    private static GuiButton subsetButton = new GuiButton(0, 0, 2, 150, 20, "Mods");
    private static List<String> mod_names; // Contains of all mod_names which add blocks/items
    private static List<String> mod_ids; // Contains of all mod_names which add blocks/items
    private static final int ITEM_HEIGHT = 11;
    private static int scrollOffset = 0;
    private static int selectedItem = -1;
    private static String longestModName;

    static void click(int mouseX, int mouseY) {
        boolean flag = false;

        if (subsetButton.mousePressed(ClientProxy.mc, mouseX, mouseY)) {
            JEIButtons.proxy.playClick();
            isListShown = !isListShown;
            flag = true;
            if (!isListShown)
                EventHandlers.skipModClickCount = 0;
        }

        if (selectedItem > -1 && selectedItem < mod_names.size()) {
            JEIButtons.proxy.playClick();
            isListShown = !isListShown;
            JEIPlugin.setJEIText("@" + mod_ids.get(selectedItem));
            flag = true;
            EventHandlers.skipModClickCount = 2;
        }

        if (!flag && isListShown)
            isListShown = false;

    }

    public static void scroll(int i) {
        if (i < 0) {
            int maxItems = (JEIButtons.proxy.getScreenHeight() - (22 + subsetButton.yPosition)) / ITEM_HEIGHT - 1;
            if (scrollOffset + 1 <= mod_names.size() - maxItems)
                scrollOffset++;
        } else if (i > 0) {

            if (scrollOffset - 1 >= 0)
                scrollOffset--;
        }
    }

    static void drawButtons(int mouseX, int mouseY, int guiTop) {
        if (guiTop < 72 && ClientProxy.mc.currentScreen instanceof GuiContainerCreative) {
            subsetButton.setWidth(45);
            subsetButton.yPosition = guiTop - 50;
            subsetButton.xPosition = JEIButtons.proxy.getScreenWidth() / 2 - 74;

            if (guiTop > 21)
                subsetButton.drawButton(ClientProxy.mc, mouseX, mouseY);
        } else {
            subsetButton.setWidth(150);
            subsetButton.yPosition = 2;
            subsetButton.xPosition = JEIButtons.proxy.getScreenWidth() / 2 - 75;

            if (guiTop > 21)
                subsetButton.drawButton(ClientProxy.mc, mouseX, mouseY);
        }
    }

    public static void drawSubsetList(int mouseX, int mouseY) {
        boolean anyButtonHovered = false;

        if (isListShown) {

            FontRenderer f = ClientProxy.mc.fontRendererObj;

            int maxItems = (JEIButtons.proxy.getScreenHeight() - (22 + subsetButton.yPosition)) / ITEM_HEIGHT - 1;
            int maximumIndex = Math.min(maxItems + scrollOffset, mod_names.size());

            if (scrollOffset > mod_names.size() - maxItems)
                scrollOffset = mod_names.size() - maximumIndex;

            String mod;
            int x, y = (20 + subsetButton.yPosition), w, h;
            int itemsLeft = mod_ids.size() - scrollOffset - maxItems;

            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableDepth();

            w = f.getStringWidth(longestModName) + 3;
            x = subsetButton.xPosition;
            h = f.FONT_HEIGHT + 2;
            GuiUtils.drawGradientRect(0, x, y, x + w, y + 3 + h * ((maxItems < mod_ids.size() ? maxItems : mod_ids.size()) + (itemsLeft > 0 ? 1 : 0)), 0xCC000000, 0xCC000000);
            for (int i = scrollOffset; i < maximumIndex; i++) {
                mod = mod_names.get(i);
                y = (23 + subsetButton.yPosition) + h * (i - scrollOffset);


                if (mouseX >= x && mouseX <= x + w && mouseY > y && mouseY < y + h) {
                    f.drawString(mod, x + 2, y + 1, 16777120);
                    selectedItem = i;
                    anyButtonHovered = true;
                } else {
                    f.drawString(mod, x + 2, y + 1, 0xFFFFFF);
                }
            }

            if (mod_ids.size() > maxItems && itemsLeft > 0)
                f.drawString(itemsLeft + I18n.format(Localization.MORE) + "...", x + 2, y + h + 1, 0xFFFFFF);

            GlStateManager.enableDepth();
            GlStateManager.enableLighting();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
        }

        if (!anyButtonHovered) {
            selectedItem = -1;
        }

    }

    public static void setupModList() {
        mod_names = new ArrayList<String>();
        mod_ids = new ArrayList<String>();
        List<String> tempMods = new ArrayList<String>();

        for (ResourceLocation l : ForgeRegistries.ITEMS.getKeys()) {
            if (!tempMods.contains(l.getResourceDomain())) {
                tempMods.add(l.getResourceDomain());
            }
        }

        for (ResourceLocation l : ForgeRegistries.BLOCKS.getKeys()) {
            if (!tempMods.contains(l.getResourceDomain())) {
                tempMods.add(l.getResourceDomain());
            }
        }

        int longesModN = 0;

        for (ModContainer mod : Loader.instance().getModList()) {
            if (tempMods.contains(mod.getModId())) {
                mod_names.add(mod.getName());
                mod_ids.add(mod.getModId());
                if (mod.getName().length() > longesModN) {
                    longestModName = mod.getName();
                    longesModN = mod.getName().length();
                }
            }
        }

    }
}
