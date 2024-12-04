package melonslise.locks.api.loot.condition;

import net.minecraft.world.item.ItemStack;

import java.util.function.BiFunction;

public record GlobalLootValueCondition(BiFunction<ItemStack, Integer, Integer> condition, int priority) {
    public GlobalLootValueCondition {
        if (priority < 0) throw new IllegalArgumentException("Priority must be non-negative");
    }
}
