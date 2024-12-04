package melonslise.locks.api.loot;

import melonslise.locks.api.loot.condition.GlobalLootValueCondition;
import melonslise.locks.api.loot.condition.LootValueCondition;
import melonslise.locks.common.data.LootValueReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;
import java.util.Map;

public class LootValues {
    public static int getValue(ItemStack stack) {
        int defaultValue = LootValueReloadListener.itemValues().getOrDefault(BuiltInRegistries.ITEM.getKey(stack.getItem()), 0);
        List<LootValueCondition> conditions = LootValueConditions.conditions();
        List<GlobalLootValueCondition> globalConditions = LootValueConditions.globalConditions();

        int value = defaultValue;
        if (!globalConditions.isEmpty()) {
            globalConditions.sort((a, b) -> b.priority() - a.priority());
            for (GlobalLootValueCondition condition : globalConditions) {
                value = condition.condition().apply(stack, value);
            }
        }
        if (!conditions.isEmpty()) {
            conditions.sort((a, b) -> b.priority() - a.priority());
            for (LootValueCondition condition : conditions) {
                if (stack.is(condition.item())) {
                    value = condition.condition().apply(stack, value);
                }
            }
        }

        value *= stack.getCount();

        if (stack.isEnchanted()) {
            Map<Enchantment, Integer> enchantments = stack.getAllEnchantments();
            for (Enchantment enchantment : enchantments.keySet()) {
                value += EnchantmentValues.getValue(enchantment, enchantments.get(enchantment));
            }
        }

        return value;
    }
}