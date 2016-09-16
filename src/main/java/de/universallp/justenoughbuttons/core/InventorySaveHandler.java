package de.universallp.justenoughbuttons.core;

import de.universallp.justenoughbuttons.JEIButtons;
import de.universallp.justenoughbuttons.client.ClientProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;


/**
 * Created by universallp on 12.09.16 15:02.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/UniversalLP/JustEnoughButtons
 */
public class InventorySaveHandler {

    private static GuiButton[] saveButtons = new GuiButton[4];
    private static InventorySnapshot[] saves = new InventorySnapshot[4];
    private static final String replaceCommand = "/replaceitem entity @p %s %s %s %s %s";
    public static boolean skipClick = false;

    public static void init() {
        String load = I18n.format("justenoughbuttons.load") + " ";
        String save = I18n.format("justenoughbuttons.save") + " ";

        for (int i = 0; i < saveButtons.length; i++) {
            saveButtons[i] = new GuiButton(i, 5, 110 + 22 * i, 50, 20, (saves[i] == null ? save : load) + (i + 1));
        }
    }

    static boolean click(int mouseX, int mouseY, boolean rightMouse) {
        boolean flag = false;

        if (!rightMouse) {
            for (int i = 0; i < saveButtons.length; i++)
                if (saveButtons[i].mousePressed(ClientProxy.mc, mouseX, mouseY)) {
                    JEIButtons.proxy.playClick();

                    if (saves[i] == null) {
                        saves[i] = new InventorySnapshot(ClientProxy.player.inventory);
                        String load = I18n.format("justenoughbuttons.load") + " ";
                        saveButtons[i].displayString = load + (i + 1);
                    } else {
                        if (ClientProxy.player.inventory.getItemStack() != null) {
                            saves[i].icon = ClientProxy.player.inventory.getItemStack();
                            skipClick = true;
                        } else
                            saves[i].giveToPlayer();
                    }

                    flag = true;
                    break;
                }
        } else {
            for (int i = 0; i < saveButtons.length; i++)
                if (saveButtons[i].mousePressed(ClientProxy.mc, mouseX, mouseY)) {
                    JEIButtons.proxy.playClick();
                    saves[i] = null;
                    String save = I18n.format("justenoughbuttons.save") + " ";
                    saveButtons[i].displayString = save + (i + 1);
                    flag = true;
                    break;
                }
        }

        return flag;
    }

    static void drawButtons(int mouseX, int mouseY) {
        for (GuiButton s : saveButtons) {
            s.drawButton(ClientProxy.mc, mouseX, mouseY);
            if (saves[s.id] != null) {
                RenderHelper.enableStandardItemLighting();
                RenderHelper.enableGUIStandardItemLighting();
                ClientProxy.mc.getRenderItem().renderItemAndEffectIntoGUI(saves[s.id].icon, s.xPosition + s.width + 2, s.yPosition + 2);
                RenderHelper.disableStandardItemLighting();

            }
        }
    }

    private static class InventorySnapshot {
        public ItemStack icon;
        NBTTagCompound[] mainInventory;
        public ItemStack[] armorInventory;
        public ItemStack[] offHandInventory;

        InventorySnapshot(InventoryPlayer inv) {
            this.mainInventory = new NBTTagCompound[inv.mainInventory.length];

            for (int i = 0; i < inv.mainInventory.length; i++)
                if (inv.mainInventory[i] != null) {
                    NBTTagCompound nbt = new NBTTagCompound();
                    inv.mainInventory[i].writeToNBT(nbt);
                    this.mainInventory[i] = nbt;
                }
        }


        void giveToPlayer() {
            if (!JEIButtons.isServerSidePresent) {
                ClientProxy.player.sendChatMessage("/clear");

                for (int i = 0; i < mainInventory.length; i++) {
                    if (mainInventory[i] == null)
                        continue;
                    ItemStack s = ItemStack.loadItemStackFromNBT(mainInventory[i]);
                    String nbt = s.hasTagCompound() ? s.getTagCompound().toString() : "";
                    ClientProxy.player.sendChatMessage(String.format(replaceCommand, "slot.inventory." + i,  s.getItem().getRegistryName(), s.stackSize, s.getItemDamage(), nbt));
                }
            } else {
                byte[] b = new byte[mainInventory.length];
                for (byte i = 0; i < b.length; i++) b[i] = i;

                CommonProxy.INSTANCE.sendToServer(new MessageRequestStacks(mainInventory, b));
            }

            ClientProxy.player.inventory.markDirty();
        }
    }

    private static class ItemReference {
        String itemName;
        int itemDamage;
        byte stackSize;
        NBTTagCompound nbt;

        public ItemReference(ItemStack s) {
            this.itemName = s.getItem().getRegistryName().getResourcePath();
            this.stackSize = (byte) s.stackSize;
            this.itemDamage = s.getItemDamage();
            if (s.hasTagCompound())
                this.nbt = s.getTagCompound();
            else
                nbt = null;
        }
    }

}
