package bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes;

import bg.maznacraft.mazna_item_lib.MaznaItemLib;
import com.google.common.collect.Multimap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.HashMap;
import java.util.Map;

public class ItemAttributesDatapackRegistry {

    public static final ResourceKey<Registry<ItemAttributesEntry>> ITEM_ATTRIBUTE_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MaznaItemLib.MOD_ID, "dynamic_item_attributes"));

    private static final Map<ResourceLocation, ItemAttributesEntry> CACHE = new HashMap<>();

    public static void reloadCache(Registry<ItemAttributesEntry> registry) {
        CACHE.clear();
        for (ItemAttributesEntry entry : registry) {
            CACHE.put(entry.item(), entry);
        }

        MaznaItemLib.LOGGER.error("___________________________________________________________MAZNICHKA______________________________________");
    }

    public static ItemAttributesEntry GetAttributes(ResourceLocation itemResource)
    {
        return CACHE.get(itemResource);
    }

    public static Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> GetDynamicAttributes(ResourceLocation itemResource)
    {
        ItemAttributesEntry entry = GetAttributes(itemResource);
        return entry == null ? null: entry.attributes();
    }
}
