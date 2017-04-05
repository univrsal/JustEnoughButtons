package de.universallp.justenoughbuttons.core.network;

/**
 * Created by universallp on 05.04.2017.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/UniversalLP/JEI Buttons
 */

import io.netty.buffer.ByteBuf;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandClearInventory;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandKill;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.server.FMLServerHandler;

/**
 * Class to allow JEB to use command buttons without cheats
 */
public class MessageExecuteButton  implements IMessage, IMessageHandler<MessageExecuteButton, IMessage> {

    public static final byte GM_CREATIVE  = 0;
    public static final byte GM_ADVENTURE = 1;
    public static final byte GM_SURVIVAL  = 2;
    public static final byte GM_SPECTATE  = 3;
    public static final byte DELETE       = 4;
    public static final byte RAIN         = 5;
    public static final byte SUN          = 6;
    public static final byte DAY     = 7;
    public static final byte NIGHT   = 8;
    public static final byte FREEZE       = 9;
    public static final byte KILL         = 10;

    private int commandOrdinal;
    private String[] cmd;

    public MessageExecuteButton() { }

    public MessageExecuteButton(int cmdId, String[] cmd) {
        this.commandOrdinal = cmdId;
        this.cmd = cmd != null ? cmd : new String[] { "" };
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.commandOrdinal = buf.readByte();
        int l = buf.readByte();
        this.cmd = new String[l];

        for (int i = 0; i < l; i++)
            this.cmd[i] = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(commandOrdinal);
        buf.writeByte(cmd.length);
        for (int i = 0; i < cmd.length; i++)
            ByteBufUtils.writeUTF8String(buf, cmd[i] != null ? cmd[i] : "");
    }

    @Override
    public IMessage onMessage(MessageExecuteButton message, MessageContext ctx) {
        EntityPlayerMP p = ctx.getServerHandler().playerEntity;

        if (p == null)
            return null;
        MinecraftServer s = ctx.getServerHandler().playerEntity.mcServer;
        World world = s.worlds[0];
        WorldInfo worldinfo = world.getWorldInfo();
        CommandBase cmd;
        switch (message.commandOrdinal) {
            case GM_ADVENTURE:
                p.setGameType(GameType.ADVENTURE);
                break;
            case GM_CREATIVE:
                p.setGameType(GameType.CREATIVE);
                break;
            case GM_SURVIVAL:
                p.setGameType(GameType.SURVIVAL);
                break;
            case GM_SPECTATE:
                p.setGameType(GameType.SPECTATOR);
                break;
            case DELETE:
                cmd = new CommandClearInventory();
                try {
                    cmd.execute(s, p, message.cmd);
                } catch (CommandException e) {
                    e.printStackTrace();
                }
                break;
            case RAIN:
                worldinfo.setCleanWeatherTime(1000000);
                worldinfo.setRainTime(0);
                worldinfo.setThunderTime(0);
                worldinfo.setRaining(false);
                worldinfo.setThundering(false);
                break;
            case SUN:
                worldinfo.setCleanWeatherTime(0);
                worldinfo.setRainTime(1000000);
                worldinfo.setThunderTime(1000000);
                worldinfo.setRaining(true);
                worldinfo.setThundering(false);
                break;
            case DAY:
                worldinfo.setWorldTime(1000);
                break;
            case NIGHT:
                world.setWorldTime(13000);
                break;
            case FREEZE:
                boolean origValue = worldinfo.getGameRulesInstance().getBoolean("doDaylightCycle");
                worldinfo.getGameRulesInstance().setOrCreateGameRule("doDaylightCycle", origValue ? "false" : "true");
                break;
            case KILL:
                cmd = new CommandKill();
                try {
                    cmd.execute(s, p, message.cmd);
                } catch (CommandException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }
}
