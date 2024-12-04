package melonslise.locks.api.loot;

import melonslise.locks.api.loot.condition.EnchantmentValueCondition;
import melonslise.locks.common.data.EnchantmentValueReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

public class EnchantmentValues {
    public static int getValue(Enchantment ench, int level) {
        int defaultValue = EnchantmentValueReloadListener.enchValues().getOrDefault(BuiltInRegistries.ENCHANTMENT.getKey(ench), 0);
        List<EnchantmentValueCondition> conditions = EnchantmentValueConditions.conditions();

        int value = defaultValue;
        if (!conditions.isEmpty()) {
            conditions.sort((a, b) -> b.priority() - a.priority());
            for (EnchantmentValueCondition condition : conditions) {
                if (ench == condition.enchantment()) {
                    value = condition.condition().apply(ench, level, value);
                }
            }
        }

        return value;
    }

    public static int getValue(ResourceLocation ench, int level) {
        return getValue(BuiltInRegistries.ENCHANTMENT.get(ench), level);
    }

    public static int getValue(String ench, int level) {
        return getValue(new ResourceLocation(ench), level);
    }
}
