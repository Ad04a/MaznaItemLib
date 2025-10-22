package bg.maznacraft.mazna_item_lib.attributes.reload;

import bg.maznacraft.mazna_item_lib.attributes.data.ItemAttributesDataCache;
import bg.maznacraft.mazna_item_lib.attributes.data.ItemAttributesEntry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class SyncItemAttributesPacket {
    private final Map<ResourceLocation, ItemAttributesEntry> data;

    public SyncItemAttributesPacket(Map<ResourceLocation, ItemAttributesEntry> data) {
        this.data = data;
    }

    public SyncItemAttributesPacket(FriendlyByteBuf buf) {
        this.data = buf.readMap(
                FriendlyByteBuf::readResourceLocation,
                b -> {
                    CompoundTag tag = b.readNbt();
                    if (tag == null) {
                        throw new IllegalStateException("Missing NBT for item attributes entry");
                    }
                    return ItemAttributesEntry.CODEC
                            .parse(NbtOps.INSTANCE, tag)
                            .result()
                            .orElseThrow(() -> new IllegalStateException("Failed to decode entry from NBT"));
                }
        );
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeMap(
                data,
                FriendlyByteBuf::writeResourceLocation,
                (b, entry) -> {
                    Tag tag = ItemAttributesEntry.CODEC
                            .encodeStart(NbtOps.INSTANCE, entry)
                            .result()
                            .orElseThrow(() -> new IllegalStateException("Failed to encode entry " + entry));
                    if (!(tag instanceof CompoundTag compound)) {
                        throw new IllegalStateException("Expected CompoundTag but got " + tag.getClass());
                    }
                    b.writeNbt(compound);
                }
        );
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ItemAttributesDataCache.putAll(data));
        ctx.get().setPacketHandled(true);
    }
}