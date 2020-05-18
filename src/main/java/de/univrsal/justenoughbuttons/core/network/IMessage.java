package de.univrsal.justenoughbuttons.core.network;

import net.minecraftforge.fml.network.NetworkEvent;

import java.io.Serializable;

public interface IMessage extends Serializable {
    boolean receive(NetworkEvent.Context context);
}
