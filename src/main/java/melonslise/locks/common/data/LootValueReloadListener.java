package melonslise.locks.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import melonslise.locks.Locks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class LootValueReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, Integer>> {
    private static final Gson GSON = (new GsonBuilder()).create();
    private static final Map<ResourceLocation, Integer> itemValues = new HashMap<>();

    public static void register(AddReloadListenerEvent event) {
        event.addListener(new LootValueReloadListener());
    }

    @Override
    protected void apply(Map<ResourceLocation, Integer> objectIn, ResourceManager resourceManager, ProfilerFiller profiler) {
        itemValues.clear();
        itemValues.putAll(objectIn);
    }

    @Override
    protected Map<ResourceLocation, Integer> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, Resource> resources = resourceManager.listResources("locks/loot_values", s -> s.getPath().endsWith(".json"));
        Map<ResourceLocation, Integer> values = new HashMap<>();
        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            try (Reader resource = entry.getValue().openAsReader()) {
                JsonObject element = GSON.fromJson(resource, JsonObject.class);
                for (Map.Entry<String, JsonElement> entry1 : element.entrySet()) {
                    Integer duplicate = values.put(new ResourceLocation(entry1.getKey()), entry1.getValue().getAsInt());
                    if (duplicate != null) {
                        Locks.LOGGER.error("Duplicate loot value json: {}", entry.getKey());
                    }
                }
            } catch (IOException e) {
                Locks.LOGGER.error("Couldn't parse loot value json: {}", entry.getKey(), e);
            }
        }
        return values;
    }

    public static Map<ResourceLocation, Integer> itemValues() {
        return itemValues;
    }
}