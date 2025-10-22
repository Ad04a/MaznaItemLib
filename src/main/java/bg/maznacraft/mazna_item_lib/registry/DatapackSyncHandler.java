package bg.maznacraft.mazna_item_lib.registry;

import bg.maznacraft.mazna_item_lib.MaznaItemLib;
import bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes.DynamicAttributeReloadListener;
import bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes.ItemAttributesDatapackRegistry;
import bg.maznacraft.mazna_item_lib.attributes.data.ItemAttributesEntry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import java.util.Map;


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
        MaznaItemLib.LOGGER.info("["+MaznaItemLib.MOD_ID+"] Server cache updated after player join - " +  registry.toString() + "["+registry.size()+"]");


        /*Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> mp = new EnumMap<>(EquipmentSlot.class);
        Multimap<Attribute, AttributeModifier> headModifiers = HashMultimap.create();
        headModifiers.put(Attributes.ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "Armor modifier",
                5.0,
                AttributeModifier.Operation.ADDITION)
        );
        mp.put(EquipmentSlot.OFFHAND, headModifiers);

        Multimap<Attribute, AttributeModifier> feetModifiers = HashMultimap.create();
        feetModifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "Armor modifier",
                0.5,
                AttributeModifier.Operation.ADDITION)
        );
        mp.put(EquipmentSlot.FEET, feetModifiers);

        ItemAttributesEntry exampleEntry = new ItemAttributesEntry(ResourceLocation.fromNamespaceAndPath("minecraft", "iron_boots"), mp);

        JsonElement json = ItemAttributesEntry.CODEC.encodeStart(JsonOps.INSTANCE, exampleEntry)
                .getOrThrow(false, e -> {
                    throw new RuntimeException("Failed to encode ItemAttributesEntry: " + e);
                });

        MaznaItemLib.LOGGER.error(json.toString());*/

        for (Map.Entry<ResourceKey<ItemAttributesEntry>, ItemAttributesEntry> entry : registry.entrySet()) {
            MaznaItemLib.LOGGER.info(" - {}", entry.getValue().item().toString());

            for(EquipmentSlot slot : EquipmentSlot.values())
            {
                for(Attribute atr : entry.getValue().attributes().get(slot).keySet())
                {
                    MaznaItemLib.LOGGER.error("----" + atr.toString());
                    for (AttributeModifier modifier : entry.getValue().attributes().get(slot).get(atr))
                    {
                        MaznaItemLib.LOGGER.error("     -" + modifier.toString());
                    }
                }
            }
        }
    }

    /*@SubscribeEvent
    public static void onClientPlayerJoin(net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingIn event) {
        Registry<ItemAttributesEntry> registry = Minecraft.getInstance()
                .getConnection()
                .registryAccess()
                .registryOrThrow(ItemAttributesDatapackRegistry.ITEM_ATTRIBUTE_REGISTRY_KEY);

        ItemAttributesDatapackRegistry.reloadCache(registry);  // client-side cache
    }*/

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        DynamicAttributeReloadListener DynamicAttributeListener = new DynamicAttributeReloadListener();
        event.addListener(DynamicAttributeListener);
    }
}
