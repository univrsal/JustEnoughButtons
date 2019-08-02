package de.univrsal.justenoughbuttons.core.handlers;

import de.univrsal.justenoughbuttons.client.EnumButtonCommands;
import de.univrsal.justenoughbuttons.JEIButtons;
import de.univrsal.justenoughbuttons.client.handlers.CommandHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;

import java.io.File;

/**
 * Created by universal on 05.04.2017.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JEI Buttons
 */
public class ConfigHandler {
    public static boolean enableAdventureMode = true;
    public static boolean enableSpectatoreMode = true;

    public static boolean enableSaves = true;
    public static int magnetRadius = 8;

    public static boolean enableClearInventory = false;
    public static boolean enableSubsets = true;

    static boolean enableGamemode = true;
    static boolean enableDelete = true;
    static boolean enableTime = true;
    static boolean enableWeather = true;
    static boolean enableKillMobs = true;
    static boolean enableTimeFreeze = true;
    static boolean enableMagnet = true;

    static boolean[] enableCustom = new boolean[]{false, false, false, false};

    public static String[] customCommand = new String[]{"help", "help", "help", "help"}; // Halp halp halp
    public static String[] customName = new String[]{"Print Help", "Print Help", "Print Help", "Print Help"};

    public static String[] spongeServers;

    public static final String CATEGORY = "buttons";
    public static final String CATEGORY_CUSTOM = "custombuttons";
    public static final String CATEGORY_POSITION = "position";
    public static final String CATEGORY_COMPAT = "compat";
    public static final String CATEGORY_SERVER_PERMISSIONS = "server_permissions";

    public static boolean magnetRequiresOP = true;
    public static boolean saveRequireOP = true;
    public static boolean timeRequiresOP = true;
    public static boolean gamemodeRequiresOP = true;
    public static boolean weatherRequiresOP = true;
    public static boolean killMobsRequiresOP = true;
    public static boolean timeFreezeRequiresOP = true;
    public static boolean deleteRequiresOP  = true;

    public static int yOffset;
    public static int xOffset;

    public static boolean showButtons = true;

    public static boolean registerUtilKeybinds = true;

    //public static Configuration config;

    static void load() {
//        spongeServers = config.getStringList("spongeServers", CATEGORY_COMPAT, new String[0], "Server adresses with servers that use spongeforge, to adjust the commands so the fit the sponge syntax");
//
//        showButtons = config.getBoolean("showButtons", CATEGORY, true, "When false no button will be shown");
//
//        enableAdventureMode = config.getBoolean("enableAdventureMode", CATEGORY, true, "When false the gamemode button won't allow you to switch to adventure mode");
//        enableSpectatoreMode = config.getBoolean("enableSpectatoreMode", CATEGORY, true, "When false the gamemode button won't allow you to switch to spectator mode");
//        enableGamemode = config.getBoolean("enableGamemode", CATEGORY, true, "When false the gamemode button will be disabled");
//        enableDelete = config.getBoolean("enableDelete", CATEGORY, true, "When false the delete button will be disabled");
//        enableWeather = config.getBoolean("enableWeather", CATEGORY, true, "When false the weather buttons will be disabled");
//        enableTime = config.getBoolean("enableTime", CATEGORY, true, "When false the time buttons will be disabled");
//        enableKillMobs = config.getBoolean("enableKillMobs", CATEGORY, true, "When false the kill entities button will be disabled");
//        enableTimeFreeze = config.getBoolean("enableTimeFreeze", CATEGORY, true, "When false the freeze time button will be disabled");
//        enableMagnet = config.getBoolean("enableMagnet", CATEGORY, true, "When false the magnet mode button will be disabled");
//        enableSubsets = config.getBoolean("enableSubsets", CATEGORY, true, "When true the subsets button will be shown to get quick access to all items from all mods (Requires JEI)");
//
//        yOffset = config.getInt("yOffset", CATEGORY_POSITION, 5, 0, 1024, "Y offset of the buttons");
//        xOffset = config.getInt("xOffset", CATEGORY_POSITION, 5, 0, 1024, "X offset of the buttons");
//
//        enableSaves = config.getBoolean("enableSaves", CATEGORY, true, "When false the four save slots will be disabled");
//
//        magnetRadius = config.getInt("magnetRadius", CATEGORY, 12, 1, 32, "The radius in which the magnet mode attracts items");
//
//        enableClearInventory = config.getBoolean("enableClearInventory", CATEGORY, false, "When true shift clicking the delete buttonwill clear your inventory");
//
//        registerUtilKeybinds = config.getBoolean("registerUtilKeybinds", CATEGORY_COMPAT, true, "When false the show light level and chunk boundaries keybinds won't be registered");
//
//        // Custom Buttons
//
//        for (int i = 0; i < enableCustom.length; i++) {
//            enableCustom[i] = config.getBoolean("enableCustomButton." + i, CATEGORY_CUSTOM, false, "When true you'll get a button, which executes a custom command");
//            customCommand[i] = config.getString("customCommand." + i, CATEGORY_CUSTOM, "help", "The command to be executed by the custom button");
//            customName[i] = config.getString("customName." + i, CATEGORY_CUSTOM, "Print Help", "The tooltip of the custom button");
//        }
//
//        // Permissions
//        magnetRequiresOP = config.getBoolean("magnetRequiresOP", CATEGORY_SERVER_PERMISSIONS, true, "When false the magnet mode can be used on servers without op (When JEB is installed on the server)");
//        saveRequireOP = config.getBoolean("savesRequireOP", CATEGORY_SERVER_PERMISSIONS, true, "When false the inventory saves can be used on servers without op (When JEB is installed on the server)");
//        weatherRequiresOP = config.getBoolean("weatherRequiresOP", CATEGORY_SERVER_PERMISSIONS, true, "When false weather buttons can be used on servers without op (When JEB is installed on the server)");
//        killMobsRequiresOP = config.getBoolean("killMobsRequiresOP", CATEGORY_SERVER_PERMISSIONS, true, "When false the kill mobs button can be used on servers without op (When JEB is installed on the server)");
//        timeFreezeRequiresOP = config.getBoolean("timeFreezeRequiresOP", CATEGORY_SERVER_PERMISSIONS, true, "When false the time can be (un)frozen on servers without op (When JEB is installed on the server)");
//        deleteRequiresOP = config.getBoolean("deleteRequiresOP", CATEGORY_SERVER_PERMISSIONS, true, "When false delete button can be used on servers without op (When JEB is installed on the server)");
//        timeRequiresOP = config.getBoolean("timeRequiresOP", CATEGORY_SERVER_PERMISSIONS, true, "When false the time can be (un)frozen on servers without op (When JEB is installed on the server)");
//        gamemodeRequiresOP = config.getBoolean("gamemodeRequiresOP", CATEGORY_SERVER_PERMISSIONS, true, "When false game mode button can be used on servers without op (When JEB is installed on the server)");
//
//        CommandHelper.useCheats = config.getBoolean("useCheats", CATEGORY_CUSTOM, false, "When true JEB will require cheats to be enabled in singleplayer");
//
//        EnumButtonCommands.ADVENTURE.setEnabled(enableAdventureMode);
//        EnumButtonCommands.SPECTATE.setEnabled(enableSpectatoreMode);
//
//        JEIButtons.btnGameMode.setVisible(enableGamemode);
//        JEIButtons.btnDay.setVisible(enableTime);
//        JEIButtons.btnNight.setVisible(enableTime);
//        JEIButtons.btnTrash.setVisible(enableDelete);
//        JEIButtons.btnNoMobs.setVisible(enableKillMobs);
//        JEIButtons.btnFreeze.setVisible(enableTimeFreeze);
//        JEIButtons.btnRain.setVisible(enableWeather);
//        JEIButtons.btnSun.setVisible(enableWeather);
//        JEIButtons.btnMagnet.setVisible(enableMagnet);
//
//        for (int i = 0; i < JEIButtons.btnCustom.length; i++)
//            JEIButtons.btnCustom[i].setVisible(enableCustom[i]);
//
//        if (config.hasChanged())
//            config.save();
    }

//    public static void loadPostInit() {
//        if (config.hasChanged()) config.save();
//    }

    public static void loadConfig(File configFile) {
//        if (config == null)
//            config = new Configuration(configFile);
//        load();
//        MinecraftForge.EVENT_BUS.register(new ConfigHandler());
    }

//    @SubscribeEvent
//    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
//        if (eventArgs.getModID().equals(JEIButtons.MODID)) {
//            config.save();
//            load();
//            JEIButtons.configHasChanged = true;
//        }
//    }
}
