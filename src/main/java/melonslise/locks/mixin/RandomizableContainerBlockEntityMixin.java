package melonslise.locks.mixin;

import melonslise.locks.api.loot.LootValues;
import melonslise.locks.common.config.LocksConfig;
import melonslise.locks.common.data.LockTierOrderReloadListener;
import melonslise.locks.common.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

import static melonslise.locks.common.config.LocksConfig.canEnchant;

@Mixin(RandomizableContainerBlockEntity.class)
public class RandomizableContainerBlockEntityMixin {
    @Inject(method = "getItem(I)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "RETURN"), cancellable = true)
    private void lockRandomizableContainerBlockEntity(int slot, CallbackInfoReturnable<ItemStack> cir) {
        BlockPos pos = ((BaseContainerBlockEntity) (Object) this).getBlockPos();
        Level level = ((BaseContainerBlockEntity) (Object) this).getLevel();
        if (level == null) return;
        if (level.isClientSide) return;
        if (LocksUtil.locked(level, pos)){
            cir.setReturnValue(Items.AIR.getDefaultInstance());
        }
    }

    @Inject(method = "setLootTable(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/resources/ResourceLocation;)V", at = @At(value = "RETURN"))
    private static void lockBlocks(BlockGetter level, RandomSource randomSource, BlockPos pos, ResourceLocation lootTable, CallbackInfo ci) {
        if (!(level instanceof WorldGenRegion wgr)) return;
        MinecraftServer l = wgr.getServer();
        if (l == null || !(level.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity rbe)) return;

        List<Lockable> lockables = ((ILockableProvider) wgr.getChunk(pos.getX() >> 4, pos.getZ() >> 4)).getLockables();

        lockables.replaceAll(lock -> {
            boolean contains = false;
            for (BlockPos lockedPos : lock.bb.getContainedPos()) {
                if (pos.equals(lockedPos)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) return lock;

            if (LocksConfig.BLACKLIST_LOOT_TABLES.get().contains(lootTable.toString())) return null;

            NonNullList<ItemStack> items = LocksUtil.simulateLootTable(lootTable, rbe, wgr.getLevel(), pos);
            List<LockTierOrderReloadListener.LockTier> locks = LockTierOrderReloadListener.getLockTierOrder();

            int totalValue = 0;

            for (ItemStack item : items) {
                totalValue += LootValues.getValue(item);
            }

            ResourceLocation newLock = null;
            for (LockTierOrderReloadListener.LockTier tier : locks) {
                if (totalValue >= tier.value()) {
                    newLock = tier.id();
                } else {
                    break;
                }
            }

            if (newLock == null) return null;

            ItemStack stack = LocksUtil.getDefaultStack(newLock, l);
            if (canEnchant(randomSource)) EnchantmentHelper.enchantItem(randomSource, stack, 5 + randomSource.nextInt(30), false);
            return new Lockable(lock.bb, Lock.from(stack), lock.tr, stack, lock.id);
        });
        lockables.removeIf(Objects::isNull);
    }
}
