package de.univrsal.justenoughbuttons.client.handlers;

import de.univrsal.justenoughbuttons.JEIButtons;
import de.univrsal.justenoughbuttons.client.ClientProxy;
import de.univrsal.justenoughbuttons.client.EnumButtonCommands;
import de.univrsal.justenoughbuttons.core.handlers.ConfigHandler;
import de.univrsal.justenoughbuttons.core.handlers.MagnetModeHandler;
import de.univrsal.justenoughbuttons.core.network.MessageExecuteButton;
import de.univrsal.justenoughbuttons.core.network.MessageMagnetMode;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;

/**
 * Created by universal on 06.04.2017.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JEI Buttons
 */
public class CommandHelper {

    public static boolean useCheats = true;

    public static void handleClick(EnumButtonCommands btn) {
        String[] command = null;
        switch (btn) {
            case CREATIVE:
                handleButton(MessageExecuteButton.GM_CREATIVE, btn.getCommand().split(" "));
                break;
            case ADVENTURE:
                handleButton(MessageExecuteButton.GM_ADVENTURE, btn.getCommand().split(" "));
                break;
            case SURVIVAL:
                handleButton(MessageExecuteButton.GM_SURVIVAL, btn.getCommand().split(" "));
                break;
            case SPECTATE:
                handleButton(MessageExecuteButton.GM_SPECTATE, btn.getCommand().split(" "));
                break;
            case DELETE:
                ItemStack draggedStack = ClientProxy.player.inventory.getItemStack();
                if (draggedStack.isEmpty()) {
                    if (Screen.hasShiftDown() && ConfigHandler.enableClearInventory)
                        command = new String[] { "clear" };
                } else {
                    String name  = draggedStack.getItem().getRegistryName().toString();
                    int data = draggedStack.getDamage();
//                    if (!Screen.hasShiftDown()) {
//                        command = new String[] { "clear", "@p", name, String.valueOf(data) };
//                    } else
                    command = new String[]{"clear"};
                    boolean ghost = draggedStack.hasTag() && draggedStack.getTag().getBoolean("JEI_Ghost");
                    if (ghost)
                        ClientProxy.player.inventory.setItemStack(ItemStack.EMPTY);
                }

                if (JEIButtons.isServerSidePresent) {
                    ClientProxy.player.inventory.setItemStack(ItemStack.EMPTY);
                    if (Screen.hasShiftDown() && ConfigHandler.enableClearInventory)
                        ClientProxy.player.inventory.clear();
                }

                if (Screen.hasShiftDown() && ConfigHandler.enableClearInventory)
                    handleButton(MessageExecuteButton.DELETE_ALL, command);
                else if (!draggedStack.equals(ItemStack.EMPTY))
                    handleButton(MessageExecuteButton.DELETE, command);
                break;
            case RAIN:
                handleButton(MessageExecuteButton.RAIN, btn.getCommand().split(" "));
                break;
            case SUN:
                handleButton(MessageExecuteButton.SUN, btn.getCommand().split(" "));
                break;
            case DAY:
                handleButton(MessageExecuteButton.DAY, btn.getCommand().split(" "));
                break;
            case NIGHT:
                handleButton(MessageExecuteButton.NIGHT, btn.getCommand().split(" "));
                break;
            case FREEZETIME:
                boolean gameRuleDayCycle = ClientProxy.mc.world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE);
                command = new String[] { "gamerule", "doDaylightCycle", (gameRuleDayCycle ? "false" : "true")};
                handleButton(MessageExecuteButton.FREEZE, command);
                break;
            case NOMOBS:
                handleButton(MessageExecuteButton.KILL, btn.getCommand().split(" "));
                break;
            case MAGNET:
                command = new String[]{"tp", "@e[type=minecraft:item,distance=.." + ConfigHandler.magnetRadius + "]", "@p"};
                    handleButton(MessageExecuteButton.MAGNET, command);
                break;
            case CUSTOM1:
            case CUSTOM2:
            case CUSTOM3:
            case CUSTOM4:
                JEIButtons.sendCommand(btn.getCommand());
                break;
        }
    }

    private static void sendCommand(String[] args) {
        if (args == null)
            return;
        StringBuilder cmd = new StringBuilder(args[0]);
        for (int i = 1; i < args.length; i++)
            cmd.append(" ").append(args[i]);
        JEIButtons.sendCommand(cmd.toString());
    }

    private static void handleButton(int msgId, String[] args) {
        if (JEIButtons.isServerSidePresent && !useCheats) { // Use direct server-client connection when enabled
            if (msgId != MessageExecuteButton.MAGNET)
                ClientProxy.network.sendToServer(new MessageExecuteButton(msgId, args));
            else {
                ClientProxy.network.sendToServer(new MessageMagnetMode(MagnetModeHandler.state));
                MagnetModeHandler.state = !MagnetModeHandler.state;
            }
        } else { // Otherwise use commands for servers without JEB or SP worlds with cheats
            sendCommand(args);
            if (msgId == MessageExecuteButton.MAGNET)
                MagnetModeHandler.state = !MagnetModeHandler.state;
        }
    }
}
