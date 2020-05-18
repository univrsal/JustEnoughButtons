package de.univrsal.justenoughbuttons.core.handlers;

import de.univrsal.justenoughbuttons.JEIButtons;
import de.univrsal.justenoughbuttons.client.EnumButtonCommands;
import de.univrsal.justenoughbuttons.client.handlers.CommandHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static net.minecraftforge.common.ForgeConfigSpec.*;

/**
 * Created by universal on 05.04.2017.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JEI Buttons
 */
public final class ConfigHandler {

    public static class DefaultBooleanValidator implements Predicate<Boolean> {
        @Override
        public boolean test(Boolean t) {
            return true;
        }
    }

    public static class Common {
        public final BooleanValue enableAdventureMode;
        public final BooleanValue enableSpectatoreMode;
        public final BooleanValue enableSaves;
        public final IntValue magnetRadius;

        public final BooleanValue enableClearInventory;
        public final BooleanValue enableSubsets;

        public final BooleanValue enableGamemode;
        public final BooleanValue enableDelete;
        public final BooleanValue enableTime;
        public final BooleanValue enableWeather;
        public final BooleanValue enableKillMobs;
        public final BooleanValue enableTimeFreeze;
        public final BooleanValue enableMagnet;

        public final ConfigValue<List<? extends Boolean>> enableCustom;
        public final ConfigValue<List<? extends String>> customCommand;
        public final ConfigValue<List<? extends String>> customName;
        public final ConfigValue<List<? extends String>> spongeServers;
        private static List<? extends Boolean> defaultCommandState = Arrays.asList(false, false, false, false);
        private static List<String> defaultCommands = Arrays.asList("help", "help", "help", "help");
        private static List<String> defaultNames = Arrays.asList("Print help", "Print help", "Print help", "Print help");

        public final BooleanValue magnetRequiresOP;
        public final BooleanValue saveRequireOP;
        public final BooleanValue timeRequiresOP;
        public final BooleanValue gamemodeRequiresOP;
        public final BooleanValue weatherRequiresOP;
        public final BooleanValue killMobsRequiresOP;
        public final BooleanValue timeFreezeRequiresOP;
        public final BooleanValue deleteRequiresOP;
        public final BooleanValue useCheats;

        public final IntValue yOffset;
        public final IntValue xOffset;
        public final BooleanValue showButtons;
        public final BooleanValue registerUtilKeybinds;

        public Common(Builder builder) {
            enableAdventureMode = builder.comment("When false the gamemode button won't allow you to switch to" +
                    "adventure mode").define("enableAdventureMode", true);
            enableSpectatoreMode = builder.comment("When false the gamemode button won't allow you to switch to" +
                    "spectator mode").define("enableSpectatoreMode",  true);
            enableGamemode = builder.comment("When false the gamemode button will be disabled").define(
                    "enableGamemode", true);
            enableDelete = builder.comment("When false the delete button will be disabled").define(
                    "enableDelete", true);
            showButtons = builder.comment("When false no button will be shown").define("showButtons",true);
            enableWeather = builder.comment("When false the weather buttons will be disabled").define("enableWeather", true);
            enableTime = builder.comment("When false the time buttons will be disabled").define("enableTime", true);
            enableKillMobs = builder.comment("When false the kill entities button will be disabled").define("enableKillMobs", true);
            enableTimeFreeze = builder.comment("When false the freeze time button will be disabled").define("enableTimeFreeze",true);
            enableMagnet = builder.comment("When false the magnet mode button will be disabled").define("enableMagnet", true);
            enableSubsets = builder.comment("When true the subsets button will be shown to get quick access to all items" +
                    "from all mods (Requires JEI)").define("enableSubsets", true);
            enableSaves = builder.comment("When false the four save slots will be disabled").define("enableSaves", true);
            magnetRadius = builder.comment("The radius in which the magnet mode attracts items").defineInRange("magnetRadius",  12, 1, 32);
            enableClearInventory = builder.comment("When true shift clicking the delete buttonwill clear your inventory").define("enableClearInventory",  false);
            registerUtilKeybinds = builder.comment("When false the show light level and chunk boundaries keybinds won't be" +
                    " registered").define("registerUtilKeybinds", true);
            // Permissions
            magnetRequiresOP = builder.comment("When false the magnet mode can be used on servers without op (When JEB is installed on the server)").define("magnetRequiresOP", true);
            saveRequireOP = builder.comment("When false the magnet mode can be used on servers without op (When JEB is installed on the server)").define("savesRequireOP", true);
            weatherRequiresOP = builder.comment("When false the magnet mode can be used on servers without op (When JEB is installed on the server)").define("weatherRequiresOP", true);
            killMobsRequiresOP = builder.comment("When false the magnet mode can be used on servers without op (When JEB is installed on the server)").define("killMobsRequiresOP", true);
            timeFreezeRequiresOP = builder.comment("When false the magnet mode can be used on servers without op (When JEB is installed on the server)").define("timeFreezeRequiresOP", true);
            deleteRequiresOP = builder.comment("When false the magnet mode can be used on servers without op (When JEB is installed on the server)").define("deleteRequiresOP", true);
            timeRequiresOP = builder.comment("When false the magnet mode can be used on servers without op (When JEB is installed on the server)").define("timeRequiresOP", true);
            gamemodeRequiresOP = builder.comment("When false the magnet mode can be used on servers without op (When JEB is installed on the server)").define("gamemodeRequiresOP", true);

            yOffset = builder.comment("Y offset of the buttons").defineInRange("yOffset", 5, 0, 1024);
            xOffset = builder.comment("X offset of the buttons").defineInRange("xOffset", 5, 0, 1024);

            spongeServers = builder.comment("Server adresses with servers that use spongeforge, to adjust the commands so the fit the sponge syntax").defineList("spongeServers", new ArrayList<String>(), null);
            enableCustom = builder.comment("Set which custom command buttons you want to use")
                    .defineList("enableCustom", () -> defaultCommandState, o -> true);
            customCommand = builder.comment("The commands to run for the command buttons").defineList("commands", () -> defaultCommands, o -> true);
            customName = builder.comment("The names for the command buttons").defineList("commands", () -> defaultNames, o -> true);

            useCheats = builder.comment("When true JEB will require cheats to be enabled in singleplayer").define("useCheats", false);
        }

        public void load()
        {
          CommandHelper.useCheats = useCheats.get();

          EnumButtonCommands.ADVENTURE.setEnabled(enableAdventureMode.get());
          EnumButtonCommands.SPECTATE.setEnabled(enableSpectatoreMode.get());
          JEIButtons.btnGameMode.setVisible(enableGamemode.get());
          JEIButtons.btnDay.setVisible(enableTime.get());
          JEIButtons.btnNight.setVisible(enableTime.get());
          JEIButtons.btnTrash.setVisible(enableDelete.get());
          JEIButtons.btnNoMobs.setVisible(enableKillMobs.get());
          JEIButtons.btnFreeze.setVisible(enableTimeFreeze.get());
          JEIButtons.btnRain.setVisible(enableWeather.get());
          JEIButtons.btnSun.setVisible(enableWeather.get());
          JEIButtons.btnMagnet.setVisible(enableMagnet.get());

          for (int i = 0; i < JEIButtons.btnCustom.length; i++)
            JEIButtons.btnCustom[i].setVisible(enableCustom.get().get(i));
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}
