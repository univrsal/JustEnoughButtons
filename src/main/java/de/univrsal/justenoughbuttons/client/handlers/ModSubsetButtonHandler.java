package de.univrsal.justenoughbuttons.client.handlers;

import de.univrsal.justenoughbuttons.JEIButtons;
import de.univrsal.justenoughbuttons.client.ClientProxy;
import de.univrsal.justenoughbuttons.client.ClientUtil;
import de.univrsal.justenoughbuttons.client.Localization;
import de.univrsal.justenoughbuttons.client.gui.GuiButtonJEB;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by universal on 12.09.16 15:02.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
public class ModSubsetButtonHandler {
    public static boolean ENABLE_SUBSETS = false;
    static boolean isListShown = false;
    private static GuiButton subsetButton = new GuiButtonJEB(0, 0, 2, 150, 20, I18n.format(Localization.MODS));
    private static List<String> mod_names; // Contains of all mod_names which add blocks/items
    private static List<String> mod_ids; // Contains of all mod_names which add blocks/items
    private static final int ITEM_HEIGHT = 11;
    private static int scrollOffset = 0;
    private static int selectedItem = -1;
    private static String longestModName;

    static void click(int mouseX, int mouseY) {
        boolean flag = false;

        if (subsetButton.mouseClicked(mouseX, mouseY, ClientUtil.mouseButton())) {
            //JEIButtons.proxy.playClick();
            isListShown = !isListShown;
            flag = true;
            if (!isListShown)
                EventHandlers.skipModClickCount = 0;
        }

        if (selectedItem > -1 && selectedItem < mod_names.size()) {
            //JEIButtons.proxy.playClick();
            isListShown = !isListShown;
            //JEIPlugin.setJEIText("@" + mod_ids.get(selectedItem));
            flag = true;
            EventHandlers.skipModClickCount = 2;
        }

        if (!flag && isListShown)
            isListShown = false;
    }

    static void scroll(double i) {
        if (i < 0) {
            int maxItems = (ClientUtil.getScreenHeight() - (22 + subsetButton.y)) / ITEM_HEIGHT - 1;
            if (scrollOffset + 1 <= mod_names.size() - maxItems)
                scrollOffset++;
        } else if (i > 0) {
            if (scrollOffset - 1 >= 0)
                scrollOffset--;
        }
    }

    static void drawButtons(int mouseX, int mouseY, int guiTop) {

        if (ItemGroup.GROUPS.length > 12 && guiTop < 72 && ClientProxy.mc.currentScreen instanceof GuiContainerCreative) {
            subsetButton.setWidth(45);
            subsetButton.y = guiTop - 50;
            subsetButton.x = ClientUtil.getScreenWidth() / 2 - 74;

            if (guiTop > 21)
                subsetButton.drawButtonForegroundLayer(mouseX, mouseY);
        } else {
            subsetButton.setWidth(150);
            subsetButton.y = 2;
            subsetButton.x = ClientUtil.getScreenWidth() / 2 - 75;

            if (guiTop > 21)
                subsetButton.drawButtonForegroundLayer(mouseX, mouseY);
        }
    }

    static void drawSubsetList(int mouseX, int mouseY) {
        boolean anyButtonHovered = false;

        if (isListShown) {

            FontRenderer f = ClientProxy.mc.fontRenderer;

            int maxItems = (ClientUtil.getScreenHeight() - (22 + subsetButton.y)) / ITEM_HEIGHT - 1;
            int maximumIndex = Math.min(maxItems + scrollOffset, mod_names.size());

            if (scrollOffset > mod_names.size() - maxItems)
                scrollOffset = mod_names.size() - maximumIndex;

            String mod;
            int x, y = (20 + subsetButton.y), w, h;
            int itemsLeft = mod_ids.size() - scrollOffset - maxItems;

            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.depthMask(false);

            w = f.getStringWidth(longestModName) + 3;
            x = subsetButton.x;
            h = f.FONT_HEIGHT + 2;
            GuiUtils.drawGradientRect(0, x, y, x + w, y + 3 + h * ((maxItems < mod_ids.size() ? maxItems : mod_ids.size()) + (itemsLeft > 0 ? 1 : 0)), 0xCC000000, 0xCC000000);
            for (int i = scrollOffset; i < maximumIndex; i++) {
                mod = mod_names.get(i);
                y = (23 + subsetButton.y) + h * (i - scrollOffset);

                if (mouseX >= x && mouseX <= x + w && mouseY > y && mouseY < y + h) {
                    f.drawString(mod, x + 2, y + 1, 16777120);
                    selectedItem = i;
                    anyButtonHovered = true;
                } else {
                    f.drawString(mod, x + 2, y + 1, 0xFFFFFF);
                }
            }

            if (mod_ids.size() > maxItems && itemsLeft > 0)
                f.drawString(itemsLeft + " " + I18n.format(Localization.MORE) + "...", x + 2, y + h + 1, 0xFFFFFF);

            GlStateManager.depthMask(true);
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
            if (!tempMods.contains(l.getNamespace())) {
                tempMods.add(l.getNamespace());
            }
        }

        for (ResourceLocation l : ForgeRegistries.BLOCKS.getKeys()) {
            if (!tempMods.contains(l.getNamespace())) {
                tempMods.add(l.getNamespace());
            }
        }

        int longesModN = 0;

        for (ModInfo mod : FMLLoader.getLoadingModList().getMods()) {
            if (tempMods.contains(mod.getModId())) {
                mod_names.add(mod.getDisplayName());
                mod_ids.add(mod.getModId());
                if (mod.getDisplayName().length() > longesModN) {
                    longestModName = mod.getDisplayName();
                    longesModN = mod.getDisplayName().length();
                }
            }
        }
    }
}
