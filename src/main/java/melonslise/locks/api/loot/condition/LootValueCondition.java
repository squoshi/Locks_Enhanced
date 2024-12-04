package melonslise.locks.api.loot.condition;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiFunction;

public record LootValueCondition(BiFunction<ItemStack, Integer, Integer> condition, Item item, int priority) {
    public LootValueCondition {
        if (priority < 0) throw new IllegalArgumentException("Priority must be non-negative");
    }
}
