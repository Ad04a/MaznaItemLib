package bg.maznacraft.mazna_item_lib.attributes.data;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ItemAttributesDataCache {
    private static final Map<ResourceLocation, ItemAttributesEntry> DATA = new HashMap<>();

    public static void clear() {
        DATA.clear();
    }

    public static void putAll(Map<ResourceLocation, ItemAttributesEntry> newData) {
        DATA.clear();
        DATA.putAll(newData);
    }

    public static Map<ResourceLocation, ItemAttributesEntry> all() {
        return DATA;
    }

    public static ItemAttributesEntry get(ResourceLocation id) {
        return DATA.get(id);
    }
}
