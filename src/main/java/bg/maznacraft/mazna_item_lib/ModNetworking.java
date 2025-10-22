package bg.maznacraft.mazna_item_lib;

import bg.maznacraft.mazna_item_lib.attributes.reload.SyncItemAttributesPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel ATTRIBUTE_CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(MaznaItemLib.MOD_ID, MaznaItemLib.ATTRIBUTE_NETWORK_PATH),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int id = 0;

    public static void register() {
        ATTRIBUTE_CHANNEL.registerMessage(id++,
                SyncItemAttributesPacket.class,
                SyncItemAttributesPacket::toBytes,
                SyncItemAttributesPacket::new,
                SyncItemAttributesPacket::handle
        );


    }

}
