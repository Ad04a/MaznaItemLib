package bg.maznacraft.mazna_item_lib.registry;

import bg.maznacraft.mazna_item_lib.MaznaItemLib;
import bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes.ItemAttributesDatapackRegistry;
import bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes.ItemAttributesEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = MaznaItemLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DatapackSyncHandler {

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        Registry<ItemAttributesEntry> registry = server.registryAccess()
                .registryOrThrow(ItemAttributesDatapackRegistry.ITEM_ATTRIBUTE_REGISTRY_KEY);
        ItemAttributesDatapackRegistry.reloadCache(registry);
        MaznaItemLib.LOGGER.info("["+MaznaItemLib.MOD_ID+"] Server cache initialized at startup");
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        MinecraftServer server = event.getPlayer() != null ? event.getPlayer().server : event.getPlayerList().getServer();

        // Access the registry from the server's registry access
        Registry<ItemAttributesEntry> registry = server.registryAccess().registryOrThrow(ItemAttributesDatapackRegistry.ITEM_ATTRIBUTE_REGISTRY_KEY);

        // Rebuild your cache
        ItemAttributesDatapackRegistry.reloadCache(registry);
        MaznaItemLib.LOGGER.info("["+MaznaItemLib.MOD_ID+"] Server cache updated after player join");
    }

    /*@SubscribeEvent
    public static void onClientPlayerJoin(net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingIn event) {
        Registry<ItemAttributesEntry> registry = Minecraft.getInstance()
                .getConnection()
                .registryAccess()
                .registryOrThrow(ItemAttributesDatapackRegistry.ITEM_ATTRIBUTE_REGISTRY_KEY);

        ItemAttributesDatapackRegistry.reloadCache(registry);  // client-side cache
    }*/
}
