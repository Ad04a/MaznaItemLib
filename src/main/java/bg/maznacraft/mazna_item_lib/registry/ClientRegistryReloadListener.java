package bg.maznacraft.mazna_item_lib.registry;

import bg.maznacraft.mazna_item_lib.MaznaItemLib;
import bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes.DynamicItemAttributes;
import bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes.ItemAttributesDatapackRegistry;
import bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes.ItemAttributesEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = MaznaItemLib.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientRegistryReloadListener {


    private static Registry<ItemAttributesEntry> lastRegistry = null;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        var connection = Minecraft.getInstance().getConnection();
        if (connection == null) return;

        Registry<ItemAttributesEntry> registry = connection.registryAccess()
                .registryOrThrow(ItemAttributesDatapackRegistry.ITEM_ATTRIBUTE_REGISTRY_KEY);

        // Only rebuild cache if the registry object itself changed
        if (registry != lastRegistry) {
            lastRegistry = registry;
            ItemAttributesDatapackRegistry.reloadCache(registry);
            MaznaItemLib.LOGGER.info("["+MaznaItemLib.MOD_ID+"] Client cache reloaded (registry object changed)");
        }
    }
}