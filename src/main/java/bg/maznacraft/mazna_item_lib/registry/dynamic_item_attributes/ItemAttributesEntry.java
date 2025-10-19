package bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public class ItemAttributesEntry {
    private final ResourceLocation item;
    private final DynamicItemAttributes attributes;

    public ItemAttributesEntry(ResourceLocation item, DynamicItemAttributes attributes) {
        this.item = item;
        this.attributes = attributes;
    }

    public ResourceLocation getItem() {
        return item;
    }

    public DynamicItemAttributes getAttributes() {
        return attributes;
    }

    public static final Codec<ItemAttributesEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("item").forGetter(ItemAttributesEntry::getItem),
            DynamicItemAttributes.CODEC.fieldOf("attributes").forGetter(ItemAttributesEntry::getAttributes)
    ).apply(instance, ItemAttributesEntry::new));
}