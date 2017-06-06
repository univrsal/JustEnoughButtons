package de.universallp.justenoughbuttons;

import de.universallp.justenoughbuttons.client.ClientProxy;
import de.universallp.justenoughbuttons.client.EnumButtonCommands;
import de.universallp.justenoughbuttons.client.Localization;
import de.universallp.justenoughbuttons.client.handlers.InventorySaveHandler;
import de.universallp.justenoughbuttons.core.CommonProxy;
import de.universallp.justenoughbuttons.core.handlers.ConfigHandler;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

/**
 * Created by universallp on 09.08.2016 16:07.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/UniversalLP/JustEnoughButtons
 */
@Mod(modid = JEIButtons.MODID, version = JEIButtons.VERSION, guiFactory = "de.universallp.justenoughbuttons.client.gui.GuiFactory")
public class JEIButtons {

    public static final String MODID = "justenoughbuttons";
    static final String VERSION = "1.11.2-1.4.5";
    public static final String MOD_MOREOVERLAYS = "moreoverlays";
    public static final String MOD_DYN_SOURROUND = "dsurround";
    public static final String MOD_JEI = "jei";
    public static boolean isServerSidePresent = false;
    public static boolean isSpongePresent = false;
    public static boolean enableOverlays = true;

    @Mod.Instance
    public static JEIButtons instance;

    @SidedProxy(clientSide = "de.universallp.justenoughbuttons.client.ClientProxy", serverSide = "de.universallp.justenoughbuttons.core.CommonProxy")
    public static CommonProxy proxy;

    public static EnumButtonCommands btnGameMode = EnumButtonCommands.SURVIVAL;
    public static EnumButtonCommands btnTrash    = EnumButtonCommands.DELETE;
    public static EnumButtonCommands btnSun      = EnumButtonCommands.SUN;
    public static EnumButtonCommands btnRain     = EnumButtonCommands.RAIN;
    public static EnumButtonCommands btnDay      = EnumButtonCommands.DAY;
    public static EnumButtonCommands btnNight    = EnumButtonCommands.NIGHT;
    public static EnumButtonCommands btnNoMobs   = EnumButtonCommands.NOMOBS;
    public static EnumButtonCommands btnFreeze   = EnumButtonCommands.FREEZETIME;
    public static EnumButtonCommands btnMagnet   = EnumButtonCommands.MAGNET;

    public static EnumButtonCommands[] btnCustom   = new EnumButtonCommands[] { EnumButtonCommands.CUSTOM1, EnumButtonCommands.CUSTOM2,
                                                                                EnumButtonCommands.CUSTOM3, EnumButtonCommands.CUSTOM4 };

    public static boolean configHasChanged = false;

    public static EnumButtonCommands hoveredButton;
    public static boolean isAnyButtonHovered;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
       proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        proxy.registerKeyBind();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public enum EnumButtonState {
        DISABLED,
        ENABLED,
        HOVERED
    }

    public static void setUpPositions() {
        EnumButtonCommands[] btns = new EnumButtonCommands[] { btnGameMode, btnRain, btnSun, btnTrash, btnDay, btnNight,
                                                               btnNoMobs, btnFreeze, btnMagnet, btnCustom[0], btnCustom[1], btnCustom[2], btnCustom[3],};

        int x = 0, y = 0;
        for (EnumButtonCommands b : btns) {
            if (!b.isVisible)
                continue;

            b.setPosition((EnumButtonCommands.width + 2) * x + ConfigHandler.xOffset, (EnumButtonCommands.height + 2) * y + ConfigHandler.yOffset);
            x++;

            if (y == 0 && x == 4) {
                y++;
                x = 0;
            } else if (y == 1 && x == 3) {
                y++;
                x = 0;
            } else if (x % 2 == 0 && y > 1) {
                x = 0;
                y++;
            }
        }
        EnumButtonCommands.CREATIVE.setPosition(btnGameMode.xPos, btnGameMode.yPos);
        EnumButtonCommands.SPECTATE.setPosition(btnGameMode.xPos, btnGameMode.yPos);
        EnumButtonCommands.ADVENTURE.setPosition(btnGameMode.xPos, btnGameMode.yPos);
        InventorySaveHandler.init();
    }

    public static void sendCommand(String cmd) {
        if (!isSpongePresent)
            cmd = "/" + cmd;
        else
           cmd = "/minecraft:" + cmd;

        if (cmd.length()  <= 256)
            ClientProxy.player.sendChatMessage(cmd);
        else
            ClientProxy.mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(Localization.NBT_TOO_LONG));
    }

    public static void logInfo(String s, Object ... format) {
        FMLLog.log(MODID, Level.INFO, s, format);
    }

}
