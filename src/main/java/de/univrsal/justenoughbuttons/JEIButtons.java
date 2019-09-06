package de.univrsal.justenoughbuttons;


import de.univrsal.justenoughbuttons.client.ClientProxy;
import de.univrsal.justenoughbuttons.client.EnumButtonCommands;
import de.univrsal.justenoughbuttons.client.Localization;
import de.univrsal.justenoughbuttons.client.handlers.InventorySaveHandler;
import de.univrsal.justenoughbuttons.core.CommonProxy;
import de.univrsal.justenoughbuttons.core.IProxy;
import de.univrsal.justenoughbuttons.core.handlers.ConfigHandler;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by universal on 09.08.2016 16:07.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
@Mod(JEIButtons.MODID)
public class JEIButtons {

    public static final String MODID = "justenoughbuttons";
    static final String VERSION = "1.12-1.2";
    public static final String MOD_MOREOVERLAYS = "moreoverlays";
    public static final String MOD_DYN_SOURROUND = "dsurround";
    public static final String MOD_JEI = "jei";
    public static boolean isServerSidePresent = false;
    public static boolean isSpongePresent = false;
    public static boolean enableOverlays = true;
    public static final Logger LOGGER = LogManager.getLogger();
    public static JEIButtons instance;

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new CommonProxy());

    public static EnumButtonCommands btnGameMode = EnumButtonCommands.SURVIVAL;
    public static EnumButtonCommands btnTrash = EnumButtonCommands.DELETE;
    public static EnumButtonCommands btnSun = EnumButtonCommands.SUN;
    public static EnumButtonCommands btnRain = EnumButtonCommands.RAIN;
    public static EnumButtonCommands btnDay = EnumButtonCommands.DAY;
    public static EnumButtonCommands btnNight = EnumButtonCommands.NIGHT;
    public static EnumButtonCommands btnNoMobs = EnumButtonCommands.NOMOBS;
    public static EnumButtonCommands btnFreeze = EnumButtonCommands.FREEZETIME;
    public static EnumButtonCommands btnMagnet = EnumButtonCommands.MAGNET;

    public static EnumButtonCommands[] btnCustom = new EnumButtonCommands[]{EnumButtonCommands.CUSTOM1, EnumButtonCommands.CUSTOM2,
            EnumButtonCommands.CUSTOM3, EnumButtonCommands.CUSTOM4};

    public static boolean configHasChanged = false;

    public static EnumButtonCommands hoveredButton;
    public static boolean isAnyButtonHovered;

    /* Init stuff */
    public JEIButtons() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setup(FMLCommonSetupEvent event) {
        proxy.commonSetup(event);
    }

    public void clientSetup(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    public enum EnumButtonState {
        DISABLED,
        ENABLED,
        HOVERED
    }

    public static void setUpPositions() {
        EnumButtonCommands[] btns = new EnumButtonCommands[]{btnGameMode, btnRain, btnSun, btnTrash, btnDay, btnNight,
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

        if (cmd.length() <= 256)
            ClientProxy.mc.player.sendChatMessage(cmd);
        else
            ClientProxy.mc.ingameGUI.getChatGUI().printChatMessage(new TranslationTextComponent(Localization.NBT_TOO_LONG));
    }

    public static void logInfo(String s, Object... format) {
        LOGGER.info("[" + MODID + "]" + s, format);
    }

    public static boolean isModLoaded(String id) {
        List<ModInfo> mods = FMLLoader.getLoadingModList().getMods();

        for (ModInfo m : mods) {
            if (m.getModId().equals(id))
                return true;
        }
        return false;
    }
}
