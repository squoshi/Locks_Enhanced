package melonslise.locks.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import melonslise.locks.Locks;
import melonslise.locks.common.util.Lock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LockTierOrderReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, Integer>> {
    private static final Gson GSON = (new GsonBuilder()).create();
    private static final List<LockTier> tierOrder = new ArrayList<>();

    public static void register(AddReloadListenerEvent event) {
        event.addListener(new LockTierOrderReloadListener());
    }

    @Override
    protected void apply(Map<ResourceLocation, Integer> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        tierOrder.clear();
        object.entrySet().stream().map(entry -> new LockTier(entry.getKey(), entry.getValue())).sorted().forEach(tierOrder::add);
    }

    @Override
    protected Map<ResourceLocation, Integer> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, Integer> values = new HashMap<>();
        Resource resource = resourceManager.getResource(new ResourceLocation("locks:locks/lock_tiers.json"))
                .orElseThrow(() -> new IllegalStateException("Missing locks:locks/lock_tiers.json file"));
        try (Reader reader = resource.openAsReader()) {
            JsonObject element = GSON.fromJson(reader, JsonObject.class);
            for (Map.Entry<String, JsonElement> entry : element.entrySet()) {
                values.put(new ResourceLocation(entry.getKey()), entry.getValue().getAsInt());
            }
        } catch (IOException e) {
            Locks.LOGGER.error("Couldn't parse lock_tiers.json", e);
        }
        return values;
    }

    public static List<LockTier> getLockTierOrder() {
        return tierOrder;
    }

    public record LockTier(ResourceLocation id, int value) implements Comparable<LockTier> {
        @Override
        public int compareTo(@NotNull LockTierOrderReloadListener.LockTier o) {
            int result = Integer.compare(this.value, o.value);
            return result == 0 ? this.id.compareTo(o.id) : result;
        }
    }
}