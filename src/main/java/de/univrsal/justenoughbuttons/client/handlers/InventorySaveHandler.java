package de.univrsal.justenoughbuttons.client.handlers;

import com.mojang.blaze3d.systems.RenderSystem;
import de.univrsal.justenoughbuttons.JEIButtons;
import de.univrsal.justenoughbuttons.client.ClientProxy;
import de.univrsal.justenoughbuttons.client.ClientUtil;
import de.univrsal.justenoughbuttons.client.Localization;
import de.univrsal.justenoughbuttons.client.gui.SaveButton;
import de.univrsal.justenoughbuttons.core.CommonProxy;
import de.univrsal.justenoughbuttons.core.handlers.ConfigHandler;
import de.univrsal.justenoughbuttons.core.network.MessageRequestStacks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;


/**
 * Created by universal on 12.09.16 15:02.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
public class InventorySaveHandler {

    private static SaveButton[] saveButtons = new SaveButton[4];
    static InventorySnapshot[] saves = new InventorySnapshot[4];
    private static final String replaceCommand = "replaceitem entity @p %s %s%s %s";

    public static void init() {
        String load = I18n.format(Localization.LOAD) + " ";
        String save = I18n.format(Localization.SAVE) + " ";

        for (int i = 0; i < saveButtons.length; i++) {
            saveButtons[i] = new SaveButton(i, ConfigHandler.COMMON.xOffset.get(),
                    110 + ConfigHandler.COMMON.yOffset.get() + 22 * i,
                    50, 20, (saves[i] == null ? save : load) + (i + 1), (btn, mousebtn) -> {
                if (mousebtn == 0) {
                    if (saves[btn.id] == null) {
                        saves[btn.id] = new InventorySnapshot(ClientProxy.player.inventory);

                        saveButtons[btn.id].setMessage(load + (btn.id + 1));
                    } else {
                        if (!ClientProxy.player.inventory.getItemStack().isEmpty()) {
                            saves[btn.id].icon = ClientProxy.player.inventory.getItemStack().copy();
                        } else {
                            saves[btn.id].giveToPlayer();
                        }
                    }
                } else {
                    saves[btn.id] = null;
                    saveButtons[btn.id].setMessage(save + (btn.id + 1));
                }
            });
        }
    }

    static void click(int mouseX, int mouseY, int mousebutton) {
        if (mousebutton == 0) {
            for (int i = 0; i < saveButtons.length; i++)
                if (saveButtons[i].mouseClicked(mouseX, mouseY, mousebutton)) {
                    if (saves[i] == null) {
                        saves[i] = new InventorySnapshot(ClientProxy.player.inventory);
                        String load = I18n.format(Localization.LOAD) + " ";
                        saveButtons[i].setMessage(load + (i + 1));
                    } else {
                        if (!ClientProxy.player.inventory.getItemStack().isEmpty()) {
                            saves[i].icon = ClientProxy.player.inventory.getItemStack().copy();

                        } else {
                            saves[i].giveToPlayer();
                        }
                    }
                    break;
                }
        } else if (mousebutton == 1) {
            for (int i = 0; i < saveButtons.length; i++) {
                if (saveButtons[i].mouseClicked(mouseX, mouseY, mousebutton)) {
                    saves[i] = null;
                    String save = I18n.format(Localization.SAVE) + " ";
                    saveButtons[i].setMessage(save + (i + 1));
                    break;
                }
            }
        }
    }

    static void drawButtons(int mouseX, int mouseY) {
        for (SaveButton s : saveButtons) {
            s.render(mouseX, mouseY, 0.f);
            s.mouseMoved(ClientUtil.mc.mouseHelper.getMouseX(), ClientUtil.mc.mouseHelper.getMouseY());

            if (saves[s.id] != null && saves[s.id].icon != null) {
                RenderHelper.func_227780_a_();
                RenderHelper.func_227783_c_();
                ClientProxy.mc.getItemRenderer().renderItemAndEffectIntoGUI(saves[s.id].icon, s.x + s.getWidth() + 2, s.y + 2);
                RenderHelper.func_227784_d_();
            }
        }
    }

    static class InventorySnapshot {
        ItemStack icon;
        CompoundNBT[] mainInventory;
        CompoundNBT[] armorInventory;
        CompoundNBT offHandInventory;

        InventorySnapshot(CompoundNBT icon, CompoundNBT[] mainInventory, CompoundNBT[] armorInventory, CompoundNBT offHandInventory) {
            this.icon = ItemStack.read(icon);
            this.mainInventory = mainInventory;
            this.armorInventory = armorInventory;
            this.offHandInventory = offHandInventory;
        }

        InventorySnapshot(PlayerInventory inv) {
            this.mainInventory = new CompoundNBT[inv.mainInventory.size()];
            this.armorInventory = new CompoundNBT[inv.armorInventory.size()];
            this.offHandInventory = new CompoundNBT();

            for (int i = 0; i < inv.mainInventory.size(); i++) {
                CompoundNBT nbt = new CompoundNBT();
                inv.mainInventory.get(i).write(nbt);
                this.mainInventory[i] = nbt;
            }

            for (int i = 0; i < inv.armorInventory.size(); i++){
                CompoundNBT nbt = new CompoundNBT();
                inv.armorInventory.get(i).write(nbt);
                this.armorInventory[i] = nbt;
            }


            CompoundNBT nbt = new CompoundNBT();
            inv.offHandInventory.get(0).write(nbt);
            this.offHandInventory = nbt;

        }

        void giveToPlayer() {
            if (!JEIButtons.isServerSidePresent) {
                JEIButtons.sendCommand("clear");
                String nbt;
                String cmd;

                for (int i = 0; i < mainInventory.length; i++) {
                    if (mainInventory[i] == null)
                        continue;
                    ItemStack s = ItemStack.read(mainInventory[i]);

                    if (s.isEmpty())
                        continue;

                    nbt = s.hasTag() ? s.getTag().toString() : "";
                    if (i < 9)
                        cmd = String.format(replaceCommand, "hotbar." + i, s.getItem().getRegistryName(), nbt, s.getCount());
                    else
                        cmd = String.format(replaceCommand, "inventory." + (i - 9), s.getItem().getRegistryName(), nbt, s.getCount());
                    if (checkCommandLength(cmd))
                        JEIButtons.sendCommand(cmd);
                }

                for (int i = 0; i < armorInventory.length; i++) {
                    if (armorInventory[i] == null)
                        continue;
                    ItemStack s = ItemStack.read(armorInventory[i]);

                    if (s.isEmpty())
                        continue;

                    nbt = s.hasTag() ? s.getTag().toString() : "";
                    cmd = String.format(replaceCommand, "armor." + idToSlot(i), s.getItem().getRegistryName(), nbt, s.getCount());
                    if (checkCommandLength(cmd))
                        JEIButtons.sendCommand(cmd);
                }

                if (offHandInventory != null) {
                    ItemStack s = ItemStack.read(offHandInventory);

                    if (!s.isEmpty()) {
                        nbt = s.hasTag() ? s.getTag().toString() : "";
                        cmd = String.format(replaceCommand, "weapon.offhand", s.getItem().getRegistryName(), nbt, s.getCount());
                        if (checkCommandLength(cmd))
                            JEIButtons.sendCommand(cmd);
                    }
                }
            } else {
                CommonProxy.network.sendToServer(new MessageRequestStacks(mainInventory, armorInventory, offHandInventory));
            }

            ClientProxy.player.inventory.markDirty();
        }

        boolean checkCommandLength(String cmd) {
            if (cmd.length() > 100) {
                ClientProxy.player.sendMessage(new TranslationTextComponent(Localization.NBT_TOO_LONG));
                return false;
            }
            return true;
        }

        String idToSlot(int i) {
            switch (i) {
                case 0:
                    return "feet";
                case 1:
                    return "legs";
                case 2:
                    return "chest";
                case 3:
                    return "head";
            }
            return "head";
        }
    }
}
