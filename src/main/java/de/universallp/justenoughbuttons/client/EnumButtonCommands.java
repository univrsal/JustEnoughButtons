package de.universallp.justenoughbuttons.client;

import de.universallp.justenoughbuttons.JEIButtons;
import de.universallp.justenoughbuttons.core.handlers.ConfigHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

/**
 * Created by universallp on 05.04.2017.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/UniversalLP/JEI Buttons
 */
public enum EnumButtonCommands {
    CREATIVE("gamemode 1", 5, 5),
    ADVENTURE("gamemode 2", 5, 5),
    SURVIVAL("gamemode 0", 5, 5),
    SPECTATE("gamemode 3", 5, 5),
    DELETE("clear ", 65, 5),
    RAIN("weather rain", 25, 5),
    SUN("weather clear", 45, 5),
    DAY("time set day", 5, 26),
    NIGHT("time set night", 25, 26),
    FREEZETIME("gamerule doDaylightCycle", 25, 47),
    NOMOBS("kill @e[type=!Player]", 5, 47),
    MAGNET("tp", 25, 47),
    CUSTOM1("", 5, 68, 0),
    CUSTOM2("", 25, 68, 1),
    CUSTOM3("", 5, 89, 2),
    CUSTOM4("", 25, 89, 3);

    public boolean isEnabled = true;
    public boolean isVisible = true;

    String command;

    JEIButtons.EnumButtonState state = JEIButtons.EnumButtonState.DISABLED;

    public static final int width = 18;
    public static final int height = 19;
    public int xPos;
    public int yPos;
    public byte id;

    EnumButtonCommands(String commandToExecute, int x, int y) {
        this.command = commandToExecute;
        this.xPos = x;
        this.yPos = y;
    }

    EnumButtonCommands(String commandToExecute, int x, int y, int id) {
        this.id = (byte) id;
        this.command = commandToExecute;
        this.xPos = x;
        this.yPos = y;
    }

    static final ResourceLocation icons = new ResourceLocation(JEIButtons.MODID, "textures/icons.png");

    public void setEnabled(boolean b) {
        isEnabled = b;
    }

    public void setPosition(int x, int y) {
        xPos = x;
        yPos = y;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public EnumButtonCommands cycle() {
        if (ordinal() == 0)
            return ADVENTURE.isEnabled ? ADVENTURE : SURVIVAL;
        else if (ordinal() == 1)
            return SURVIVAL;
        else if (ordinal() == 2)
            return SPECTATE.isEnabled ? SPECTATE : CREATIVE;
        else if (ordinal() == 3)
            return CREATIVE;
        else
            return this; // Other buttons don't cycle
    }

    public void draw() {
        if (!isVisible || getCommand().equals(""))
            return;

        int mouseX = JEIButtons.proxy.getMouseX();
        int mouseY = JEIButtons.proxy.getMouseY();

        if (isEnabled) {
            if (mouseX >= xPos && mouseX <= xPos + width && mouseY >= yPos && mouseY <= yPos + height) {
                state = JEIButtons.EnumButtonState.HOVERED;
                JEIButtons.hoveredButton = this;
                JEIButtons.isAnyButtonHovered = true;
            } else
                state = JEIButtons.EnumButtonState.ENABLED;
        } else {
            if (mouseX >= xPos && mouseX <= xPos + width && mouseY >= yPos && mouseY <= yPos + height) {
                JEIButtons.hoveredButton = this;
                JEIButtons.isAnyButtonHovered = true;
            }
            state = JEIButtons.EnumButtonState.DISABLED;
        }
        ClientProxy.mc.renderEngine.bindTexture(icons);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        GuiUtils.drawTexturedModalRect(xPos, yPos, width * iconID(), height * state.ordinal(), width, height, 1);
        RenderHelper.disableStandardItemLighting();
    }

    public String getCommand() {
        return this.ordinal() > MAGNET.ordinal() ? ConfigHandler.customCommand[id] : command;
    }

    public int iconID() {
        if (this == MAGNET)
            return 12;
        return this.ordinal() > MAGNET.ordinal() ? 11 : this.ordinal();
    }
}
