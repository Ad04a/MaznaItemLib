package bg.maznacraft.mazna_item_lib.registry;

import bg.maznacraft.mazna_item_lib.MaznaItemLib;
import bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes.ItemAttributesDatapackRegistry;
import bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes.ItemAttributesEntry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;


@Mod.EventBusSubscriber(modid = MaznaItemLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistries {

    @SubscribeEvent
    public static void onNewDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                ItemAttributesDatapackRegistry.ITEM_ATTRIBUTE_REGISTRY_KEY,
                ItemAttributesEntry.CODEC,   // codec for reading from JSON
                ItemAttributesEntry.CODEC    // codec for network sync to clients
        );
    }
}