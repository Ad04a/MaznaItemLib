package bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes;

import bg.maznacraft.mazna_item_lib.MaznaItemLib;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ItemAttributesDatapackRegistry {

    public static final ResourceKey<Registry<ItemAttributesEntry>> ITEM_ATTRIBUTE_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MaznaItemLib.MOD_ID, "dynamic_item_attributes"));

    private static final Map<ResourceLocation, ItemAttributesEntry> CACHE = new HashMap<>();

    public static void reloadCache(Registry<ItemAttributesEntry> registry) {
        CACHE.clear();
        for (ItemAttributesEntry entry : registry) {
            CACHE.put(entry.getItem(), entry);
        }

        MaznaItemLib.LOGGER.error("___________________________________________________________MAZNICHKA______________________________________");
    }

}
