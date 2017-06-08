package de.universallp.justenoughbuttons.client.handlers;

import de.universallp.justenoughbuttons.JEIButtons;
import de.universallp.justenoughbuttons.client.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.io.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by universal on 17.09.16 15:22.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
public class SaveFileHandler {

    public static boolean SAVE_SNAPSHOTS = true;
    private static boolean HAS_LOADED = false;
    private static String savePath;

    public SaveFileHandler init() {
        savePath = ClientProxy.mc.mcDataDir.toString() + "/mods/JustEnoughButtons";

        File saveFolder = new File(savePath);

        if (!saveFolder.exists()) {
            JEIButtons.logInfo( "No save folder for inventory snapshots found. Creating it under %s", savePath);
            if (!saveFolder.mkdir()) {
                JEIButtons.logInfo( "Couldn't create folder. Saving of inventory snapshots is disabled!");
                SAVE_SNAPSHOTS = false;
            }
        }

        return this;
    }

    public void loadForPlayer() throws FileNotFoundException {
        if (HAS_LOADED)
            return;
        HAS_LOADED = true;

        UUID uuid = EntityPlayer.getUUID(Minecraft.getMinecraft().getSession().getProfile());
        File saveFolder = new File(savePath);
        File saveFile = null;

        if (uuid != null) {
            for (File f : saveFolder.listFiles()) {
                if (f.getName().contains(uuid.toString())) {
                    saveFile = f;
                    break;
                }
            }
        }

        if (saveFile != null) {
            JEIButtons.logInfo("Found savefile for user!");
            try {
                BufferedReader br = new BufferedReader(new FileReader(saveFile));
                NBTTagCompound[] mainInventory = new NBTTagCompound[36];
                NBTTagCompound[] armorInventory = new NBTTagCompound[4];

                NBTTagCompound offHand = new NBTTagCompound();
                NBTTagCompound icon = new NBTTagCompound();

                int invIndex = 0;
                int armorIndex = 0;
                int saveIndex  = 0;

                for (String line; (line = br.readLine()) != null;) {
                    if (line.startsWith("MainInv:")) {
                        mainInventory[invIndex] = JsonToNBT.getTagFromJson(line.substring(8));
                        invIndex++;
                    } else if (line.startsWith("NullInv")) {
                        invIndex++;
                    } else if (line.startsWith("ArmoInv:")) {
                        armorInventory[armorIndex] = JsonToNBT.getTagFromJson(line.substring(8));
                        armorIndex++;
                    } else if (line.startsWith("NullArm")) {
                        armorIndex++;
                    } else if (line.startsWith("OffHand:")) {
                        offHand = JsonToNBT.getTagFromJson(line.substring(8));
                    } else if (line.startsWith("IconSta:")) {
                        icon = JsonToNBT.getTagFromJson(line.substring(8));
                    } else if (line.startsWith("EMPTY SAVE")) {
                        saveIndex++;
                    } else if (line.startsWith("END SAVE")) {

                        InventorySaveHandler.saves[saveIndex] = new InventorySaveHandler.InventorySnapshot(icon.copy(), mainInventory.clone(), armorInventory.clone(), offHand.copy());
                        saveIndex++;

                        invIndex = 0;
                        armorIndex = 0;
                        mainInventory = new NBTTagCompound[36];
                        armorInventory = new NBTTagCompound[4];

                        offHand = new NBTTagCompound();
                        icon = new NBTTagCompound();
                    } else if (line.startsWith("EOF")) {
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NBTException e) {
                e.printStackTrace();
            }
        } else {
            JEIButtons.logInfo( "No save file available.");
        }
    }

    public void saveForPlayer() throws FileNotFoundException, UnsupportedEncodingException {
        if (FMLClientHandler.instance().getClientPlayerEntity() == null) {
            JEIButtons.logInfo("Error when saving inventory saves player instance is null!");
            return;
        }

        UUID uuid = EntityPlayer.getUUID(FMLClientHandler.instance().getClientPlayerEntity().getGameProfile());
        File oldFile = new File(savePath + "/" + uuid + ".jebs");

        if (oldFile.exists() && !oldFile.delete()) {
            JEIButtons.logInfo("Error deleting old snapshot save. Saving of inventory snapshots will not be available!");
            SaveFileHandler.SAVE_SNAPSHOTS = false;
        }

        File saveFile = new File(savePath + "/" + uuid + ".jebs");

        PrintWriter writer = new PrintWriter(saveFile, "UTF-8");
        writer.println("# JustEnoughButtons Save file for " + ClientProxy.player.getDisplayNameString() + ", " + uuid.toString());
        writer.println("# Created " + new Date(System.currentTimeMillis()));
        writer.println("# WARNING: Modifying this file might result in crashes!");

        for (int i = 0; i < 4; i++) {
            InventorySaveHandler.InventorySnapshot snapshot = InventorySaveHandler.saves[i];

            if (snapshot != null) {
                for (NBTTagCompound nbt : snapshot.mainInventory) {
                    if (nbt != null)
                        writer.println("MainInv:" + nbt.toString());
                    else
                        writer.println("NullInv");
                }

                for (NBTTagCompound c : snapshot.armorInventory) {
                    if (c != null)
                        writer.println("ArmoInv:" + c.toString());
                    else
                        writer.println("NullArm");
                }

                if (snapshot.icon != null) {
                    NBTTagCompound icon = new NBTTagCompound();
                    snapshot.icon.writeToNBT(icon);
                    if (!icon.hasNoTags())
                        writer.println("IconSta:" + icon.toString());
                }

                if (snapshot.offHandInventory != null && !snapshot.offHandInventory.hasNoTags())
                    writer.println("OffHand:" + snapshot.offHandInventory.toString());
                writer.println("END SAVE");
            } else
                writer.println("EMPTY SAVE");

        }

        writer.println("EOF");
        writer.close();
    }
}
