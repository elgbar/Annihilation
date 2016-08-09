package net.techcable.npclib.versions.v1_8_R3;

import net.minecraft.server.v1_8_R3.EnumProtocolDirection;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.techcable.npclib.util.ReflectUtil;

import java.lang.reflect.Field;

public class NPCNetworkManager extends NetworkManager
{

	public NPCNetworkManager()
	{
		super(EnumProtocolDirection.CLIENTBOUND);
		Field channel = ReflectUtil.makeField(NetworkManager.class, "channel");
		Field address = ReflectUtil.makeField(NetworkManager.class, "l");

		ReflectUtil.setField(channel, this, new NullChannel());
		ReflectUtil.setField(address, this, new NullSocketAddress());

	}

}
