package de.univrsal.justenoughbuttons.client.handlers;

import de.univrsal.justenoughbuttons.JEIButtons;
import de.univrsal.justenoughbuttons.client.*;
import de.univrsal.justenoughbuttons.core.CommonProxy;
import de.univrsal.justenoughbuttons.core.handlers.ConfigHandler;
import de.univrsal.justenoughbuttons.core.network.MessageNotifyClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static de.univrsal.justenoughbuttons.JEIButtons.*;

/**
 * Created by universal on 11.08.2016 16:07.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
public class EventHandlers {
    private static BlockPos lastPlayerPos = null;
    private boolean drawMobOverlay   = false;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent e) {
        if (ConfigHandler.COMMON.showButtons.get() && e.getGui() != null && e.getGui() instanceof ContainerScreen) {
            int mouseY = e.getMouseY();
            int mouseX = e.getMouseX();


            if (JEIButtons.isAnyButtonHovered) {
               List<String> tip = Localization.getTooltip(JEIButtons.hoveredButton);
                if (tip != null) {
                    GuiUtils.drawHoveringText(tip, mouseX, Math.max(mouseY, 17), ClientUtil.getScreenWidth(),
                            ClientUtil.getScreenHeight(), -1, ClientProxy.mc.fontRenderer);
                    RenderHelper.disableStandardItemLighting();
                }
            }

            if (ConfigHandler.COMMON.enableSubsets.get())
                ModSubsetButtonHandler.drawSubsetList(mouseX, mouseY);
        }


//        if (e.getGui() instanceof GuiJEBConfig) {
//            GuiConfigEntries eL = ((GuiConfig) e.getGui()).entryList;
//            GuiConfig cfg = (GuiConfig) e.getGui();
//            if (cfg.titleLine2 != null && cfg.titleLine2.equals(ConfigHandler.CATEGORY_POSITION)) {
//                int y = getInt(1, eL);
//                int x = getInt(0, eL);
//                GuiUtils.drawGradientRect(10, x, y, x + 75, y + 75, 0x77888888, 0x77888888);
//                ClientProxy.mc.fontRenderer.drawString("[Buttons]", x + 14, y + 10, 0xFFFFFF);
//            }
//        }
    }

//    private static int getInt(int i, GuiConfigEntries eL) {
//        if (i < eL.getSize() && eL.getListEntry(i) != null && String.valueOf(eL.getListEntry(i).getCurrentValue()).length() > 0 && String.valueOf(eL.getListEntry(i).getCurrentValue()).length() < 5
//                && !String.valueOf(eL.getListEntry(i).getCurrentValue()).equals("-"))
//            return Integer.valueOf(String.valueOf(eL.getListEntry(i).getCurrentValue()));
//        return -1;
//    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMousedown(GuiScreenEvent.MouseClickedEvent.Pre e) {
        if (!(e.getGui() instanceof ContainerScreen))
            return;

        int mouseY = ClientUtil.getMouseY();
        int mouseX = ClientUtil.getMouseX();

        if (e.getButton() == 0) {
            if (JEIButtons.isAnyButtonHovered && JEIButtons.hoveredButton.isEnabled) { // Utility Buttons
                CommandHelper.handleClick(JEIButtons.hoveredButton);
                ClientUtil.playClick();
            } else { // Save buttons & Mod subsets
                if (ConfigHandler.COMMON.enableSaves.get())
                    InventorySaveHandler.click(mouseX, mouseY, e.getButton());
                ModSubsetButtonHandler.click(mouseX, mouseY, e.getButton());
            }
        } else if (e.getButton() == 1) {
            InventorySaveHandler.click(mouseX, mouseY, e.getButton());
        }

        if (isAnyButtonHovered) {
            e.setCanceled(true);
            e.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDrawBackgroundEventPost(GuiScreenEvent.BackgroundDrawnEvent e) {
        if (JEIButtons.configHasChanged) {
            JEIButtons.configHasChanged = false;
            setUpPositions();
        }

        if (JEIButtons.isServerSidePresent && e.getGui() instanceof MainMenuScreen) {
            JEIButtons.isServerSidePresent = false;
            JEIButtons.isSpongePresent = false;
        } else if (ConfigHandler.COMMON.showButtons.get() && e.getGui() != null && e.getGui() instanceof ContainerScreen) {
            int mouseY = ClientUtil.getMouseY();
            int mouseX = ClientUtil.getMouseX();
            ContainerScreen g = (ContainerScreen) e.getGui();
            PlayerEntity pl = ClientProxy.player;

            if (btnGameMode == EnumButtonCommands.SPECTATE && !ConfigHandler.COMMON.enableSpectatoreMode.get() ||
                    btnGameMode == EnumButtonCommands.ADVENTURE && !ConfigHandler.COMMON.enableAdventureMode.get()) {
                btnGameMode = btnGameMode.cycle();
            }

            JEIButtons.isAnyButtonHovered = false;
            {
                btnGameMode.draw();
                btnTrash.draw();
                btnSun.draw();
                btnRain.draw();
                btnDay.draw();
                btnNight.draw();
                btnNoMobs.draw();
                btnFreeze.draw();
                btnMagnet.draw();
            }

            if (ConfigHandler.COMMON.enableSaves.get())
                InventorySaveHandler.drawButtons(mouseX, mouseY);

            if (ModSubsetButtonHandler.ENABLE_SUBSETS && ConfigHandler.COMMON.enableSubsets.get())
                ModSubsetButtonHandler.drawButtons(mouseX, mouseY, ((ContainerScreen) e.getGui()).getGuiTop());

            for (EnumButtonCommands btn : btnCustom)
                btn.draw();

            adjustGamemode();
        }

    }

    private void adjustGamemode() {
        GameType t = ClientProxy.mc.playerController.getCurrentGameType();
        boolean doSwitch = false;

        if (t == GameType.CREATIVE && btnGameMode == EnumButtonCommands.CREATIVE)
            doSwitch = true;
        else if (t == GameType.SURVIVAL && btnGameMode == EnumButtonCommands.SURVIVAL)
            doSwitch = true;
        else if (t == GameType.ADVENTURE && btnGameMode == EnumButtonCommands.ADVENTURE)
            doSwitch = true;

        else if (t == GameType.SPECTATOR && btnGameMode == EnumButtonCommands.SPECTATE)
            doSwitch = true;

        if (doSwitch)
            btnGameMode = btnGameMode.cycle();
    }

    @SubscribeEvent
    public void onWorldJoin(EntityJoinWorldEvent e) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            InventorySaveHandler.init();
            if (e.getEntity() instanceof PlayerEntity) {
                ClientProxy.player = Minecraft.getInstance().player;
                if (((PlayerEntity) e.getEntity()).isCreative()) {
                    JEIButtons.btnGameMode = btnGameMode.cycle();
                } else {
                    JEIButtons.btnGameMode = EnumButtonCommands.CREATIVE;
                }
            }
        }
        if (e.getEntity() != null && e.getEntity() instanceof ServerPlayerEntity)
            CommonProxy.network.sendToPlayer(new MessageNotifyClient(), (ServerPlayerEntity) e.getEntity());
    }

    @SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent event) {
        if (SaveFileHandler.SAVE_SNAPSHOTS)
            try {
                ClientProxy.saveHandler.saveForPlayer();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        JEIButtons.isServerSidePresent = false;
    }
    @SubscribeEvent
    public void handleKeyInputEvent(GuiScreenEvent.KeyboardKeyPressedEvent.Post e) {
        Screen gui = ClientProxy.mc.currentScreen;

        if (gui instanceof ContainerScreen) {
            int keyCode = e.getKeyCode();

            if (ClientProxy.makeCopyKey.isActiveAndMatches(InputMappings.getInputByCode(e.getKeyCode(),e.getScanCode()))) {
                Slot hovered = ((ContainerScreen) gui).getSlotUnderMouse();

                if (hovered != null && ClientProxy.player.inventory.getItemStack().isEmpty() && !hovered.getStack().isEmpty() && hovered.getHasStack()) {

                    ItemStack stack = hovered.getStack().copy();
                    stack.setCount(1);
                    CompoundNBT t = stack.hasTag() ? stack.getTag() : new CompoundNBT();
                    t.putBoolean("JEI_Ghost", true);
                    stack.setTag(t);
                    ClientProxy.player.inventory.setItemStack(stack);
                }
            } else if (ClientProxy.hideAll.isActiveAndMatches(InputMappings.getInputByCode(e.getKeyCode(), e.getScanCode()))) {
                ConfigHandler.COMMON.showButtons.set(!ConfigHandler.COMMON.showButtons.get());
                ConfigHandler.COMMON.showButtons.save();
            }
        }

    }

    @SubscribeEvent
    public void onMouseScrollEvent(GuiScreenEvent.MouseScrollEvent event) {
        if (event.getScrollDelta() != 0 && ModSubsetButtonHandler.isListShown) {
            ModSubsetButtonHandler.scroll(event.getScrollDelta());
        }
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (enableOverlays) {
            if (ClientProxy.mobOverlay.isPressed()) {
                drawMobOverlay = !drawMobOverlay;
                if (!drawMobOverlay) {
                    MobOverlayRenderer.clearCache();
                    lastPlayerPos = null;
                }
            }

            if (ClientProxy.chunkOverlay.isPressed()) {
                ClientProxy.mc.debugRenderer.toggleChunkBorders();
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            if (lastPlayerPos == null || !lastPlayerPos.equals(ClientProxy.player.getPosition())) {
                if (drawMobOverlay)
                    MobOverlayRenderer.cacheMobSpawns(ClientProxy.player);

                if (drawMobOverlay)
                    lastPlayerPos = ClientProxy.player.getPosition();
            }
        }
    }

    @SubscribeEvent
    public void onWorldDraw(RenderWorldLastEvent event) {
        if (drawMobOverlay)
            MobOverlayRenderer.renderMobSpawnOverlay();

        if (ClientProxy.mc.currentScreen == null) {
            ModSubsetButtonHandler.isListShown = false;
            isAnyButtonHovered = false;
        }
    }

// TODO: replacement event ?

//    @SubscribeEvent
//    public void onJoinServer(FMLNetworkEvent.ClientConnectedToServerEvent e) {
//        for (String ip : ConfigHandler.spongeServers) {
//            if (Minecraft.getMinecraft().getCurrentServerData().serverIP.contains(ip)) {
//                JEIButtons.isSpongePresent = true;
//                JEIButtons.logInfo("Sponge support is enabled for this server!");
//                break;
//            }
//        }
//    }

}
