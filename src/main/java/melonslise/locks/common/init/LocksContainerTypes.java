package melonslise.locks.common.init;

import melonslise.locks.Locks;
import melonslise.locks.common.container.KeyRingContainer;
import melonslise.locks.common.container.LockPickingContainer;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class LocksContainerTypes
{
	public static final DeferredRegister<MenuType<?>>
			CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Locks.ID);

	public static final RegistryObject<MenuType<LockPickingContainer>>
		LOCK_PICKING = add("lock_picking", new MenuType<>(LockPickingContainer.FACTORY, FeatureFlags.DEFAULT_FLAGS));

	public static final RegistryObject<MenuType<KeyRingContainer>>
		KEY_RING = add("key_ring", new MenuType<>(KeyRingContainer.FACTORY, FeatureFlags.DEFAULT_FLAGS));

	private LocksContainerTypes() {}

	public static void register()
	{
		CONTAINER_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> add(String name, MenuType<T> type)
	{
		return CONTAINER_TYPES.register(name, () -> type);
	}
}