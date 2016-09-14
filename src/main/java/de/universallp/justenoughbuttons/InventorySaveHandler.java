package de.universallp.justenoughbuttons;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by universal on 12.09.16.
 */
public class InventorySaveHandler {

    private static GuiButton[] saveButtons = new GuiButton[4];
    private static InventorySnapshot[] saves = new InventorySnapshot[4];
    private static final String replaceCommand = "/replaceitem entity @p %s %s %s %s";

    public static void init() {
        for (int i = 0; i < saveButtons.length; i++) {
            saveButtons[i] = new GuiButton(i, 5, 110 + 22 * i, 50, 20, "Save " + (i + 1));
        }
    }

    public static boolean click(int mouseX, int mouseY, boolean rightMouse) {
        boolean flag = false;
        if (!rightMouse) {
            for (int i = 0; i < saveButtons.length; i++)
                if (saveButtons[i].mousePressed(ClientProxy.mc, mouseX, mouseY)) {
                    JEIButtons.proxy.playClick();
                    if (saves[i] == null) {
                        saves[i] = new InventorySnapshot(ClientProxy.player.inventory);
                        saveButtons[i].displayString = "Load " + (i + 1);
                    } else
                        saves[i].giveToPlayer();
                    flag = true;
                    break;
                }
        } else {
            for (int i = 0; i < saveButtons.length; i++)
                if (saveButtons[i].mousePressed(ClientProxy.mc, mouseX, mouseY)) {
                    JEIButtons.proxy.playClick();
                    saves[i] = null;
                    saveButtons[i].displayString = "Save " + (i + 1);
                    flag = true;
                    break;
                }
        }

        return flag;
    }

    public static void drawButtons(int mouseX, int mouseY) {
        for (GuiButton s : saveButtons)
            s.drawButton(ClientProxy.mc, mouseX, mouseY);
    }

    private static class InventorySnapshot {
        public ItemStack[] mainInventory;
        public ItemStack[] armorInventory;
        public ItemStack[] offHandInventory;

        public InventorySnapshot(InventoryPlayer inv) {
            mainInventory = new ItemStack[inv.mainInventory.length];
            for (int i = 0; i < mainInventory.length; i++) {
                if (mainInventory[i] != null)
                    mainInventory[i] = inv.mainInventory[i].copy();
            }
        }

        public void giveToPlayer() {
            for (int i = 0; i < mainInventory.length; i++) {
                if (mainInventory[i] == null)
                    continue;

                String itemName = mainInventory[i].getItem().getRegistryName().getResourcePath();
                ClientProxy.player.sendChatMessage(String.format(replaceCommand, "slot.inventory." + i, itemName, mainInventory[i].stackSize, mainInventory[i].getItemDamage()));
            }
        }
    }

}
