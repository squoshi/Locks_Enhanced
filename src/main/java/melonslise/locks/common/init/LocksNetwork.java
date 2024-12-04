package melonslise.locks.common.init;

import melonslise.locks.Locks;
import melonslise.locks.common.network.toclient.*;
import melonslise.locks.common.network.toserver.TryPinPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class LocksNetwork
{
	public static final SimpleChannel MAIN = NetworkRegistry.newSimpleChannel(new ResourceLocation(Locks.ID, "main"), () -> "locks", a -> true, a -> true);

	private LocksNetwork() {}

	public static void register()
	{
		MAIN.registerMessage(0, AddLockablePacket.class, AddLockablePacket::encode, AddLockablePacket::decode, AddLockablePacket::handle);
		MAIN.registerMessage(1, AddLockableToChunkPacket.class, AddLockableToChunkPacket::encode, AddLockableToChunkPacket::decode, AddLockableToChunkPacket::handle);
		MAIN.registerMessage(2, RemoveLockablePacket.class, RemoveLockablePacket::encode, RemoveLockablePacket::decode, RemoveLockablePacket::handle);
		MAIN.registerMessage(3, UpdateLockablePacket.class, UpdateLockablePacket::encode, UpdateLockablePacket::decode, UpdateLockablePacket::handle);
		MAIN.registerMessage(4, TryPinPacket.class, TryPinPacket::encode, TryPinPacket::decode, TryPinPacket::handle);
		MAIN.registerMessage(5, TryPinResultPacket.class, TryPinResultPacket::encode, TryPinResultPacket::decode, TryPinResultPacket::handle);
	}
}