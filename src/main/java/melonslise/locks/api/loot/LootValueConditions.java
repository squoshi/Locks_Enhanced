package melonslise.locks.api.loot;

import melonslise.locks.api.loot.condition.GlobalLootValueCondition;
import melonslise.locks.api.loot.condition.LootValueCondition;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.BiFunction;

@SuppressWarnings("unused")
public class LootValueConditions {
    private static List<LootValueCondition> conditions = List.of();
    private static List<GlobalLootValueCondition> globalConditions = List.of();

    /**
     * Add a condition to the list of conditions that determine the value of the item. The condition's arguments are the item and the item's original value.
     * @param condition The condition to add. The condition should return the int value of the item, or null if the condition does not apply.
     * @param item The item to add the condition to.
     * @param priority The priority of the condition. Conditions with higher priority are checked first.
     */
    public static void addCondition(BiFunction<ItemStack, Integer, Integer> condition, Item item, int priority) {
        conditions.add(new LootValueCondition(condition, item, priority));
    }

    /**
     * Add a condition to every item that determines the value of the item. The condition's arguments are the item and the item's original value.
     * @param condition The condition to add. The condition should return the int value of the item, or null if the condition does not apply.
     * @param priority The priority of the condition. Conditions with higher priority are checked first.
     */
    public static void addGlobalCondition(BiFunction<ItemStack, Integer, Integer> condition, int priority) {
        globalConditions.add(new GlobalLootValueCondition(condition, priority));
    }

    public static List<LootValueCondition> conditions() {
        return conditions;
    }

    public static List<GlobalLootValueCondition> globalConditions() {
        return globalConditions;
    }
}
