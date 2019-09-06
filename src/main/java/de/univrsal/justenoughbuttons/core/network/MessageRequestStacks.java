package de.univrsal.justenoughbuttons.core.network;

import de.univrsal.justenoughbuttons.client.Localization;
import de.univrsal.justenoughbuttons.core.handlers.ConfigHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Created by universal on 16.09.16 16:12.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
public class MessageRequestStacks implements IMessage {

    private static final int MAX_REQUEST_SIZE = 32767;
    private CompoundNBT[] mainInventory;
    private CompoundNBT[] armorInventory;
    private CompoundNBT offHand;


    public MessageRequestStacks() { }

    public MessageRequestStacks(CompoundNBT[] main, CompoundNBT[] armor, CompoundNBT off) {
        this.mainInventory = main;
        this.armorInventory = armor;
        this.offHand = off;
    }

    @Override
    public boolean receive(NetworkEvent.Context context) {
        ServerPlayerEntity p = context.getSender();

        if (p != null) {
            boolean isOP = MessageExecuteButton.checkPermissions(p, p.server);

            if (ConfigHandler.saveRequireOP && !isOP) {
                ITextComponent msg = new TranslationTextComponent(Localization.NO_PERMISSIONS);
                msg.setStyle(msg.getStyle().setColor(TextFormatting.RED));
                p.sendMessage(msg);
                return false;
            }

            p.inventory.clear();
            if (mainInventory != null)
                for (int i = 0; i < mainInventory.length; i++) {
                    if (mainInventory[i] != null) {
                        p.inventory.mainInventory.set(i, ItemStack.read(mainInventory[i]));
                    }
                }

            if (armorInventory != null)
                for (int i = 0; i < armorInventory.length; i++) {
                    if (armorInventory[i] != null) {
                        p.inventory.armorInventory.set(i, ItemStack.read(armorInventory[i]));
                    }
                }

            if (offHand != null) {
                p.inventory.offHandInventory.set(0, ItemStack.read(offHand));
            }
        }

        p.inventory.markDirty();
        p.inventory.tick();
        return true;
    }
}
