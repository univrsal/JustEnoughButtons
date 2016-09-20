package de.universallp.justenoughbuttons.core;

import de.universallp.justenoughbuttons.JEIButtons;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by universallp on 20.09.2016 14:33.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENSE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/UniversalLP/JEI Buttons
 */
public class MagnetModeHandler {
    private List<EntityPlayerMP> players = new ArrayList<EntityPlayerMP>();

    public void addPlayer(EntityPlayerMP p) {
        players.add(p);
    }

    public void removePlayer(EntityPlayer p) {
        players.remove(p);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent e) {
        int r = JEIButtons.ConfigHandler.magnetRadius;
        for (EntityPlayerMP p : players) {
            double x = p.posX;
            double y = p.posY;
            double z = p.posZ;

            List<EntityItem> items = p.worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x - r, y - r, z - r, x + r, y + r, z + r));

            int pulled = 0;

            for(EntityItem i : items) {
                i.setPosition(x, y, z);
                if(pulled > 200)
                    break;
                pulled++;
            }
        }
    }
}
