package melonslise.locks.common.init;

import melonslise.locks.Locks;
import melonslise.locks.common.item.KeyItem;
import melonslise.locks.common.item.KeyRingItem;
import melonslise.locks.common.item.LockItem;
import melonslise.locks.common.item.LockPickItem;
import melonslise.locks.common.item.MasterKeyItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public final class LocksItems {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Locks.ID);

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register(Locks.ID,
            () -> CreativeModeTab
                    .builder()
                    .icon(() -> new ItemStack(LocksItems.IRON_LOCK.get()))
                    .title(Component.translatable("itemGroup.locks"))
                    .displayItems((parameters, output) -> {
                        for (RegistryObject<Item> itemRegistryObject : LocksItems.ITEMS.getEntries()) {
                            output.accept(itemRegistryObject.get());
                        }
                    })
                    .build()
    );

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Locks.ID);

    public static final RegistryObject<Item>
            SPRING = add("spring", () -> new Item(new Item.Properties())),
            WOOD_LOCK_MECHANISM = add("wood_lock_mechanism", () -> new Item(new Item.Properties())),
            IRON_LOCK_MECHANISM = add("iron_lock_mechanism", () -> new Item(new Item.Properties())),
            STEEL_LOCK_MECHANISM = add("steel_lock_mechanism", () -> new Item(new Item.Properties())),

            KEY_BLANK = add("key_blank", () -> new Item(new Item.Properties())),

            PLACEHOLDER_LOCK = add("placeholder_lock", () -> new LockItem(1, 0, 0, new Item.Properties())),
            WOOD_LOCK = add("wood_lock", () -> new LockItem(5, 15, 4, new Item.Properties())),
            IRON_LOCK = add("iron_lock", () -> new LockItem(7, 14, 12, new Item.Properties())),
            STEEL_LOCK = add("steel_lock", () -> new LockItem(9, 12, 20, new Item.Properties())),
            GOLD_LOCK = add("gold_lock", () -> new LockItem(6, 22, 6, new Item.Properties())),
            DIAMOND_LOCK = add("diamond_lock", () -> new LockItem(11, 10, 100, new Item.Properties())),

            KEY = add("key", () -> new KeyItem(new Item.Properties())),
            MASTER_KEY = add("master_key", () -> new MasterKeyItem(new Item.Properties())),
            KEY_RING = add("key_ring", () -> new KeyRingItem(1, new Item.Properties())),

            WOOD_LOCK_PICK = add("wood_lock_pick", () -> new LockPickItem(0.2f, new Item.Properties())),
            IRON_LOCK_PICK = add("iron_lock_pick", () -> new LockPickItem(0.35f, new Item.Properties())),
            STEEL_LOCK_PICK = add("steel_lock_pick", () -> new LockPickItem(0.7f, new Item.Properties())),
            GOLD_LOCK_PICK = add("gold_lock_pick", () -> new LockPickItem(0.25f, new Item.Properties())),
            DIAMOND_LOCK_PICK = add("diamond_lock_pick", () -> new LockPickItem(0.85f, new Item.Properties()));

    private LocksItems() {
    }

    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TABS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static RegistryObject<Item> add(String name, Supplier<Item> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }
}