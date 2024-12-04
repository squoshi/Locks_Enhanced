package melonslise.locks.api.loot;

import melonslise.locks.api.loot.condition.EnchantmentValueCondition;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;

@SuppressWarnings("unused")
public class EnchantmentValueConditions {
    private static List<EnchantmentValueCondition> conditions = List.of();

    /**
     * Add a condition to the list of conditions that determine the value of the enchantment. The condition's arguments are the enchantment, the enchantment level, and the enchantment's original value.
     * @param condition The condition to add. The condition should return the int value of the enchantment, or null if the condition does not apply.
     * @param enchantment The enchantment to add the condition to.
     * @param priority The priority of the condition. Conditions with higher priority are checked first.
     */
    public static void addCondition(TriFunction<Enchantment, Integer, Integer, Integer> condition, Enchantment enchantment, int priority) {
        conditions.add(new EnchantmentValueCondition(condition, enchantment, priority));
    }

    public static List<EnchantmentValueCondition> conditions() {
        return conditions;
    }
}
