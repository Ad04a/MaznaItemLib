package bg.maznacraft.mazna_item_lib.attributes.reload;


import bg.maznacraft.mazna_item_lib.MaznaItemLib;
import bg.maznacraft.mazna_item_lib.ModNetworking;
import bg.maznacraft.mazna_item_lib.attributes.data.ItemAttributesDataCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = MaznaItemLib.MOD_ID)
public class ReloadSyncHandler {

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new ItemAttributesReloadListener());
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        ItemAttributesDataCache.clear();
    }


    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        if (player != null) {

            SyncItemAttributesPacket packet = new SyncItemAttributesPacket(ItemAttributesDataCache.all());
            ModNetworking.ATTRIBUTE_CHANNEL.send( PacketDistributor.PLAYER.with(() -> player), packet);

        }
    }
}
