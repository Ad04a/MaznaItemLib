package bg.maznacraft.mazna_item_lib.attributes.reload;

import bg.maznacraft.mazna_item_lib.MaznaItemLib;
import bg.maznacraft.mazna_item_lib.ModNetworking;
import bg.maznacraft.mazna_item_lib.attributes.data.ItemAttributesDataCache;
import bg.maznacraft.mazna_item_lib.attributes.data.ItemAttributesEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;

public class ItemAttributesReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public ItemAttributesReloadListener() {
        super(GSON, MaznaItemLib.ATTRIBUTE_PATH);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, ItemAttributesEntry> parsed = new HashMap<>();

        for (var entry : jsonMap.entrySet()) {
            try {
                ItemAttributesEntry data = ItemAttributesEntry.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
                        .getOrThrow(false, err -> {});
                parsed.put(entry.getKey(), data);
            } catch (Exception e) {
                MaznaItemLib.LOGGER.error("Failed to parse {}", entry.getKey(), e);
            }
        }

        ItemAttributesDataCache.putAll(parsed);
        MaznaItemLib.LOGGER.info("Loaded {} item attribute entries------------------------------------------------------------", parsed.size());
        for(ResourceLocation RL : parsed.keySet())
        {
            MaznaItemLib.LOGGER.info(" - " + RL.toString() + ":   " + parsed.get(RL).toString());
        }

        MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            var packet = new SyncItemAttributesPacket(ItemAttributesDataCache.all());
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                ModNetworking.ATTRIBUTE_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
            }
        }
    }
}
