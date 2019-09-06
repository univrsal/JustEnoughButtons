package de.univrsal.justenoughbuttons.core.handlers;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by universal on 20.09.2016 14:33.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENSE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JEI Buttons
 */
public class MagnetModeHandler {

    public static boolean state = false;
    private List<PlayerEntity> players = new ArrayList<PlayerEntity>();
    private int r;

    public MagnetModeHandler() {
        r = ConfigHandler.magnetRadius;
    }

    public void addPlayer(PlayerEntity p) {
        players.add(p);
    }

    public void removePlayer(PlayerEntity p) {
        players.remove(p);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent e) {
        if (System.currentTimeMillis() % 5 == 0 && players.size() > 0) {
            for (PlayerEntity p : players) {
                double x = p.posX;
                double y = p.posY + 1.5;
                double z = p.posZ;

                List<ItemEntity> items = p.world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(x - r, y - r, z - r, x + r, y + r, z + r));

                int pulled = 0;

                for (ItemEntity i : items) {
                    i.setPosition(x, y, z);

                    if(pulled > 200)
                        break;
                    pulled++;
                }
            }
        }
    }
}
