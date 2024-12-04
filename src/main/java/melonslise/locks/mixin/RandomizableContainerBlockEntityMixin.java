package melonslise.locks.mixin;

import melonslise.locks.common.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

        LocksUtil.applyLockTier(wgr, pos, lootTable, randomSource, l, rbe);
    }
}
