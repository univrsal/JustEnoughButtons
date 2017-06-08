package de.universallp.justenoughbuttons.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import de.universallp.justenoughbuttons.core.handlers.ConfigHandler;
import de.universallp.justenoughbuttons.JEIButtons;
import de.universallp.justenoughbuttons.core.handlers.MagnetModeHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import scala.tools.nsc.backend.icode.Members;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by universal on 20.01.2017.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JEI Buttons
 */
public class Localization {

    private static final String PREFIX = JEIButtons.MODID + ".";

    public static final String SWITCH_TO = PREFIX + "switchto";
    public static final String TIME_DAY = PREFIX + "timeday";
    public static final String TIME_NIGHT = PREFIX + "timenight";
    public static final String FREEZE_TIME = PREFIX + "freezetime";
    public static final String UNFREEZE_TIME = PREFIX + "unfreezetime";
    public static final String NO_MOBS = PREFIX + "nomobs";
    public static final String DELETE_ALL = PREFIX + "deleteall";
    public static final String DELETE_SINGLE = PREFIX + "deletesingle";
    public static final String IGNORE_META = PREFIX + "ignoringmeta";
    public static final String DRAG_ITEMS_HERE = PREFIX + "dragitemshere";
    public static final String HOLD_SHIFT = PREFIX + "holdshift";
    public static final String CLEAR_INVENTORY = PREFIX + "clearinventory";
    public static final String CUSTOM_COMMAND = PREFIX + "customcommand";
    public static final String SAVE = PREFIX + "save";
    public static final String LOAD = PREFIX + "load";
    public static final String NBT_TOO_LONG = PREFIX + "nbttoolong";
    public static final String MAGNET_ON = PREFIX + "magnet.on";
    public static final String MAGNET_OFF = PREFIX + "magnet.off";
    public static final String MAGNET = PREFIX + "magnetitems";
    public static final String MODS = PREFIX + "mods";
    public static final String NO_PERMISSIONS = "commands.generic.permission";
    public static final String MORE = PREFIX + "more";
    public static final String CMD_NO_RAIN = "commands.weather.clear";
    public static final String CMD_RAIN = "commands.weather.rain";
    public static final String KEY_CATEGORY = "key.category.justenoughbuttons";
    public static final String KEY_MAKECOPY = "justenoughbuttons.key.makecopy";
    public static final String KEY_MOBOVERLAY = "justenoughbuttons.key.moboverlay";
    public static final String KEY_CHUNKOVERLAY = "justenoughbuttons.key.chunkoverlay";
    public static final String KEY_HIDE_OVERLAY = "justenoughbuttons.key.hideall";

    public static List<String> getTooltip(EnumButtonCommands btn) {
        ArrayList<String> list = new ArrayList<String>();
        if (btn == null)
            return null;

        switch (btn) {
            case ADVENTURE:
                list.add(I18n.format(Localization.SWITCH_TO, I18n.format("gameMode.adventure")));
                break;
            case CREATIVE:
                list.add(I18n.format(Localization.SWITCH_TO, I18n.format("gameMode.creative")));
                break;
            case SPECTATE:
                list.add(I18n.format(Localization.SWITCH_TO, I18n.format("gameMode.spectator")));
                break;
            case SURVIVAL:
                list.add(I18n.format(Localization.SWITCH_TO, I18n.format("gameMode.survival")));
                break;
            case DAY:
                list.add(I18n.format(Localization.SWITCH_TO, I18n.format(Localization.TIME_DAY)));
                break;
            case NIGHT:
                list.add(I18n.format(Localization.SWITCH_TO, I18n.format(Localization.TIME_NIGHT)));
                break;
            case DELETE:
                ItemStack draggedStack = ClientProxy.player.inventory.getItemStack();
                if (!draggedStack.isEmpty()) {
                    if (JEIButtons.isServerSidePresent) {
                        list.add(I18n.format(Localization.DELETE_SINGLE, I18n.format(draggedStack.getUnlocalizedName() + ".name")));
                    } else {
                        list.add(I18n.format(Localization.DELETE_ALL, I18n.format(draggedStack.getUnlocalizedName() + ".name")));
                        if (GuiScreen.isShiftKeyDown())
                            list.add(ChatFormatting.GRAY + I18n.format(Localization.IGNORE_META));
                    }

                } else {
                    list.add(I18n.format(Localization.DRAG_ITEMS_HERE));
                    if (!JEIButtons.isServerSidePresent)
                        list.add(ChatFormatting.GRAY + I18n.format(Localization.HOLD_SHIFT));
                    if (ConfigHandler.enableClearInventory)
                        list.add(ChatFormatting.GRAY + I18n.format(Localization.CLEAR_INVENTORY));
                }
                break;
            case FREEZETIME:
                boolean gameRuleDayCycle = ClientProxy.mc.world.getGameRules().getBoolean("doDaylightCycle");
                if (gameRuleDayCycle)
                    list.add(I18n.format(Localization.FREEZE_TIME));
                else
                    list.add(I18n.format(Localization.UNFREEZE_TIME));
                break;
            case NOMOBS:
                list.add(I18n.format(Localization.NO_MOBS));
                break;
            case RAIN:
                list.add(I18n.format(Localization.CMD_RAIN));
                break;
            case SUN:
                list.add(I18n.format(Localization.CMD_NO_RAIN));
                break;
            case MAGNET:
                if (JEIButtons.isServerSidePresent) {
                    if (MagnetModeHandler.state)
                        list.add(I18n.format(Localization.MAGNET_OFF));
                    else
                        list.add(I18n.format(Localization.MAGNET_ON));
                } else
                    list.add(I18n.format(Localization.MAGNET));

                break;
            case CUSTOM1:
            case CUSTOM2:
            case CUSTOM3:
            case CUSTOM4:
                if (ConfigHandler.customName[btn.id].equals(""))
                    list.add(I18n.format(Localization.CUSTOM_COMMAND, "/" + ConfigHandler.customCommand[btn.id]));
                else
                    list.add(ConfigHandler.customName[btn.id]);
                break;
        }

        return list;
    }
}
