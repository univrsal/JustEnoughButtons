package de.universallp.justenoughbuttons.client.handlers;

import de.universallp.justenoughbuttons.core.handlers.ConfigHandler;
import de.universallp.justenoughbuttons.client.EnumButtonCommands;
import de.universallp.justenoughbuttons.JEIButtons;
import de.universallp.justenoughbuttons.client.ClientProxy;
import de.universallp.justenoughbuttons.client.Localization;
import de.universallp.justenoughbuttons.client.MobOverlayRenderer;
import de.universallp.justenoughbuttons.core.CommonProxy;
import de.universallp.justenoughbuttons.core.network.MessageNotifyClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static de.universallp.justenoughbuttons.JEIButtons.*;

/**
 * Created by universal on 11.08.2016 16:07.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
public class EventHandlers {

    private boolean isLMBDown = false;
    private boolean isRMBDown = false;
    private static BlockPos lastPlayerPos = null;

    private boolean drawMobOverlay   = false;

    static int skipSaveClickCount = 0;
    static int skipModClickCount = 0;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent e) {
        if (ConfigHandler.showButtons && e.getGui() != null && e.getGui() instanceof GuiContainer) {
            int mouseY = JEIButtons.proxy.getMouseY();
            int mouseX = JEIButtons.proxy.getMouseX();

            if (JEIButtons.isAnyButtonHovered) {
               List<String> tip = Localization.getTooltip(JEIButtons.hoveredButton);
                if (tip != null) {
                    GuiUtils.drawHoveringText(tip, mouseX, mouseY < 17 ? 17 : mouseY, ClientProxy.mc.displayWidth, ClientProxy.mc.displayHeight, -1, ClientProxy.mc.fontRenderer);
                    RenderHelper.disableStandardItemLighting();
                }
            }


            if (ConfigHandler.enableSubsets)
                ModSubsetButtonHandler.drawSubsetList(mouseX, mouseY);
        }


        if (e.getGui() instanceof GuiConfig) {
            GuiConfigEntries eL = ((GuiConfig) e.getGui()).entryList;
            GuiConfig cfg = (GuiConfig) e.getGui();
            if (cfg.titleLine2 != null && cfg.titleLine2.equals(ConfigHandler.CATEGORY_POSITION)) {
                int y = getInt(1, eL);
                int x = getInt(0, eL);
                GuiUtils.drawGradientRect(10, x, y, x + 75, y + 75, 0x77888888, 0x77888888);
                ClientProxy.mc.fontRenderer.drawString("[Buttons]", x + 14, y + 10, 0xFFFFFF);
            }
        }
    }

    private static int getInt(int i, GuiConfigEntries eL) {
        if (i < eL.getSize() && eL.getListEntry(i) != null && String.valueOf(eL.getListEntry(i).getCurrentValue()).length() > 0 && String.valueOf(eL.getListEntry(i).getCurrentValue()).length() < 5
                && !String.valueOf(eL.getListEntry(i).getCurrentValue()).equals("-"))
            return Integer.valueOf(String.valueOf(eL.getListEntry(i).getCurrentValue()));
        return -1;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDrawBackgroundEventPost(GuiScreenEvent.BackgroundDrawnEvent e) {
        if (JEIButtons.configHasChanged) {
            JEIButtons.configHasChanged = false;
            setUpPositions();
        }

        if (JEIButtons.isServerSidePresent && e.getGui() instanceof GuiMainMenu) {
            JEIButtons.isServerSidePresent = false;
            JEIButtons.isSpongePresent = false;
        } else if (ConfigHandler.showButtons && e.getGui() != null && e.getGui() instanceof GuiContainer) {
            int mouseY = JEIButtons.proxy.getMouseY();
            int mouseX = JEIButtons.proxy.getMouseX();
            GuiContainer g = (GuiContainer) e.getGui();
            EntityPlayerSP pl = ClientProxy.player;

            if (btnGameMode == EnumButtonCommands.SPECTATE && !ConfigHandler.enableSpectatoreMode || btnGameMode == EnumButtonCommands.ADVENTURE && !ConfigHandler.enableAdventureMode)
                btnGameMode = btnGameMode.cycle();

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

            if (ConfigHandler.enableSaves)
                InventorySaveHandler.drawButtons(mouseX, mouseY);

            if (ModSubsetButtonHandler.ENABLE_SUBSETS && ConfigHandler.enableSubsets)
                ModSubsetButtonHandler.drawButtons(mouseX, mouseY, ClientProxy.getGuiTop((GuiContainer) e.getGui()));

            for (EnumButtonCommands btn : btnCustom)
                btn.draw();

            adjustGamemode();

            if (Mouse.isButtonDown(0) && !isLMBDown) {
                isLMBDown = true;

                if (JEIButtons.isAnyButtonHovered && JEIButtons.hoveredButton.isEnabled) { // Utility Buttons
                    CommandHelper.handleClick(JEIButtons.hoveredButton);
                    JEIButtons.proxy.playClick();
                } else { // Save buttons & Mod subsets
                    if (ConfigHandler.enableSaves)
                        InventorySaveHandler.click(mouseX, mouseY, false);

                    ModSubsetButtonHandler.click(mouseX, mouseY);
                }
            } else if (!Mouse.isButtonDown(0)) {
                isLMBDown = false;
            }

            if (Mouse.isButtonDown(1) && !isRMBDown) {
                isRMBDown = true;
                InventorySaveHandler.click(mouseX, mouseY, true);
            } else if (!Mouse.isButtonDown(1))
                isRMBDown = false;
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
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            InventorySaveHandler.init();
            if (e.getEntity() instanceof EntityPlayer) {
                ClientProxy.player = FMLClientHandler.instance().getClientPlayerEntity();
                if (((EntityPlayer) e.getEntity()).capabilities.isCreativeMode) {
                    JEIButtons.btnGameMode = btnGameMode.cycle();
                } else {
                    JEIButtons.btnGameMode = EnumButtonCommands.CREATIVE;
                }
            }
        } else {
            if (e.getEntity() != null && e.getEntity() instanceof EntityPlayerMP)
                CommonProxy.INSTANCE.sendTo(new MessageNotifyClient(), (EntityPlayerMP) e.getEntity());
        }
    }

    @SubscribeEvent
    public void onWorldLeave(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (SaveFileHandler.SAVE_SNAPSHOTS)
            try {
                ClientProxy.saveHandler.saveForPlayer();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        JEIButtons.isServerSidePresent = false;
    }

    @SubscribeEvent
    public void handleKeyInputEvent(GuiScreenEvent.KeyboardInputEvent.Post e) {
        GuiScreen gui = ClientProxy.mc.currentScreen;

        if (gui != null && gui instanceof GuiContainer) {
            int keyCode = Keyboard.getEventKey();

            if (Keyboard.KEY_ESCAPE == keyCode) {
                skipModClickCount = 0;
                skipSaveClickCount = 0;
            }

            if (ClientProxy.makeCopyKey.isActiveAndMatches(keyCode)) {
                Slot hovered = ((GuiContainer) gui).getSlotUnderMouse();

                if (hovered != null && ClientProxy.player.inventory.getItemStack().isEmpty() && !hovered.getStack().isEmpty() && hovered.getHasStack()) {

                    ItemStack stack = hovered.getStack().copy();
                    stack.setCount(1);
                    NBTTagCompound t = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
                    t.setBoolean("JEI_Ghost", true);
                    stack.setTagCompound(t);
                    ClientProxy.player.inventory.setItemStack(stack);
                }
            } else if (ClientProxy.hideAll.isActiveAndMatches(keyCode) && Keyboard.getEventKeyState()) {
                ConfigHandler.showButtons = !ConfigHandler.showButtons;
            }
        }

    }

    @SubscribeEvent
    public void onMouseEvent(GuiScreenEvent.MouseInputEvent event) {
        if (Mouse.getEventButton() == 0) {
            if (JEIButtons.isAnyButtonHovered && JEIButtons.hoveredButton == EnumButtonCommands.DELETE && !ClientProxy.player.inventory.getItemStack().isEmpty()) {
                event.setResult(Event.Result.DENY);
                if (event.isCancelable())
                    event.setCanceled(true);
            }

            if (skipSaveClickCount > 0) {
                if (event.isCancelable())
                    event.setCanceled(true);
                event.setResult(Event.Result.DENY);
                skipSaveClickCount--;
            }

            if (skipModClickCount > 0) {
                if (event.isCancelable())
                    event.setCanceled(true);
                event.setResult(Event.Result.DENY);
                skipModClickCount--;
            }
        }

        if (Mouse.getDWheel() != 0 && ModSubsetButtonHandler.isListShown) {
            ModSubsetButtonHandler.scroll(Mouse.getEventDWheel());
        }
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (enableOverlays) {
            int kC = Keyboard.getEventKey();

            if (Keyboard.getEventKeyState()) {
                if (ClientProxy.mobOverlay.isActiveAndMatches(kC)) {
                    drawMobOverlay = !drawMobOverlay;
                    if (!drawMobOverlay) {
                        MobOverlayRenderer.clearCache();
                        lastPlayerPos = null;
                    }
                }

                if (ClientProxy.chunkOverlay.isActiveAndMatches(kC)) {
                    ClientProxy.mc.debugRenderer.toggleChunkBorders();
                }
            }

        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
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
            skipSaveClickCount = 0;
            ModSubsetButtonHandler.isListShown = false;
        }
    }

    @SubscribeEvent
    public void onJoinServer(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        for (String ip : ConfigHandler.spongeServers) {
            if (Minecraft.getMinecraft().getCurrentServerData().serverIP.contains(ip)) {
                JEIButtons.isSpongePresent = true;
                JEIButtons.logInfo("Sponge support is enabled for this server!");
                break;
            }
        }
    }

}
