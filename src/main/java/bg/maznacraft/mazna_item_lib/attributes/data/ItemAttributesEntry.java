package bg.maznacraft.mazna_item_lib.attributes.data;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public record ItemAttributesEntry(Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> attributes) {

    public static final String ATTRIBUTE_FIELD = "attributes";

    public static final Map<String, AttributeModifier.Operation> OPERATION_MAP = Map.of(
            "add", AttributeModifier.Operation.ADDITION,
            "multiply_base", AttributeModifier.Operation.MULTIPLY_BASE,
            "multiply_total", AttributeModifier.Operation.MULTIPLY_TOTAL
    );

    public static final Map<AttributeModifier.Operation, String> OPERATION_MAP_REVERSE = Map.of(
            AttributeModifier.Operation.ADDITION, "add",
            AttributeModifier.Operation.MULTIPLY_BASE, "multiply_base",
            AttributeModifier.Operation.MULTIPLY_TOTAL, "multiply_total"
    );

    public static final Codec<AttributeModifier> ATTRIBUTE_MODIFIER_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("uuid").xmap(UUID::fromString, UUID::toString).forGetter(AttributeModifier::getId),
                    Codec.STRING.fieldOf("name").forGetter(AttributeModifier::getName),
                    Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifier::getAmount),
                    Codec.STRING.fieldOf("operation").xmap(OPERATION_MAP::get, OPERATION_MAP_REVERSE::get).forGetter(AttributeModifier::getOperation)
            ).apply(instance, AttributeModifier::new)
    );

    public static final Codec<Attribute> ATTRIBUTE_CODEC = Codec.STRING.xmap(
            name -> {
                Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.parse(name));
                if (attr == null) throw new IllegalArgumentException("Unknown attribute: " + name);
                return attr;
            },
            attr -> ForgeRegistries.ATTRIBUTES.getKey(attr).toString()
    );

    public static final Codec<Multimap<Attribute, AttributeModifier>> MULTIMAP_CODEC =
            Codec.unboundedMap(ATTRIBUTE_CODEC, Codec.list(ATTRIBUTE_MODIFIER_CODEC))
                    .xmap(map -> {
                        HashMultimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
                        map.forEach(multimap::putAll);
                        return multimap;
                    }, multimap -> {
                        Map<Attribute, List<AttributeModifier>> map = new HashMap<>();
                        for (Attribute key : multimap.keySet()) {
                            map.put(key, List.copyOf(multimap.get(key)));
                        }
                        return map;
                    });

    public static final Codec<EquipmentSlot> EQUIPMENT_SLOT_CODEC = Codec.STRING.comapFlatMap(
            str -> {
                return switch (str.toLowerCase()) {
                    case "mainhand" -> DataResult.success(EquipmentSlot.MAINHAND);
                    case "offhand" -> DataResult.success(EquipmentSlot.OFFHAND);
                    case "head" -> DataResult.success(EquipmentSlot.HEAD);
                    case "chest" -> DataResult.success(EquipmentSlot.CHEST);
                    case "legs" -> DataResult.success(EquipmentSlot.LEGS);
                    case "feet" -> DataResult.success(EquipmentSlot.FEET);
                    default -> throw new IllegalStateException("Unexpected value: " + str.toLowerCase());
                };
            },
            slot -> switch (slot) {
                case MAINHAND -> "mainhand";
                case OFFHAND -> "offhand";
                case HEAD -> "head";
                case CHEST -> "chest";
                case LEGS -> "legs";
                case FEET -> "feet";
            }
    );

    public static final Codec<Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>>> SLOT_MAP_CODEC =
            Codec.unboundedMap(EQUIPMENT_SLOT_CODEC, MULTIMAP_CODEC)
                    .xmap(map -> {
                        EnumMap<EquipmentSlot, Multimap<Attribute, AttributeModifier>> enumMap = new EnumMap<>(EquipmentSlot.class);
                        enumMap.putAll(map);

                        // ensure all slots exist, even empty
                        for (EquipmentSlot slot : EquipmentSlot.values()) {
                            enumMap.putIfAbsent(slot, HashMultimap.create());
                        }

                        return enumMap;
                    }, EnumMap::new);

    public static final Codec<ItemAttributesEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    SLOT_MAP_CODEC.fieldOf(ATTRIBUTE_FIELD).forGetter(ItemAttributesEntry::attributes)
            ).apply(instance, ItemAttributesEntry::new)
    );

}