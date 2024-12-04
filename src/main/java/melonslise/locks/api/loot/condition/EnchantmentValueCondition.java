package melonslise.locks.api.loot.condition;

import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.commons.lang3.function.TriFunction;

public record EnchantmentValueCondition(TriFunction<Enchantment, Integer, Integer, Integer> condition, Enchantment enchantment, int priority) {
    public EnchantmentValueCondition {
        if (priority < 0) throw new IllegalArgumentException("Priority must be non-negative");
    }
}
