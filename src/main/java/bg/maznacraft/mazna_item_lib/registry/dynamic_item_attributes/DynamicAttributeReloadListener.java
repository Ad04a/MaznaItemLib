package bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes;

import bg.maznacraft.mazna_item_lib.MaznaItemLib;
import bg.maznacraft.mazna_item_lib.attributes.data.ItemAttributesEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;


public class DynamicAttributeReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().create();
    private static Map<ResourceLocation, ItemAttributesEntry> dataMap = Map.of();

    public DynamicAttributeReloadListener() {
        super(GSON, "dynamic_item_attributes");
    }

    @Override
    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        return super.prepare(resourceManager, profiler);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> prepared, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, ItemAttributesEntry> tempMap = new HashMap<>();

        prepared.forEach((id, jsonElement) -> {
            // Use your ItemAttributesEntry.CODEC to parse the JSON
            ItemAttributesEntry.CODEC.decode(JsonOps.INSTANCE, jsonElement)
                    .resultOrPartial(error -> MaznaItemLib.LOGGER.error("Failed to parse custom attribute JSON for {}: {}", id, error))
                    .ifPresent(data -> tempMap.put(id, data.getFirst()));
        });

        dataMap = tempMap;
        MaznaItemLib.LOGGER.info("Reloaded {} custom attribute data entries.", dataMap.size());
    }

    public static Map<ResourceLocation, ItemAttributesEntry> getDataMap() {
        return dataMap;
    }
}