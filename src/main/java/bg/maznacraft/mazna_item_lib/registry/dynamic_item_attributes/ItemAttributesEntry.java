package bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

public record ItemAttributesEntry(ResourceLocation item,
        Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> attributes) {
    // private final DynamicItemAttributes attributes_old = new DynamicItemAttributes();

    //public DynamicItemAttributes getAttributes_old() {
    //    return attributes_old;
    // }

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
                        map.forEach((key, list) -> multimap.putAll(key, list));
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
                    }, enumMap -> new EnumMap<>(enumMap));

    /*public static final Codec<Attribute> ATTRIBUTE_CODEC = Codec.STRING.xmap(
            name -> {
                Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.parse(name));
                if (attr == null) throw new IllegalArgumentException("Unknown attribute: " + name);
                return attr;
            },
            attr -> ForgeRegistries.ATTRIBUTES.getKey(attr).toString()
    );

    public static final Codec<AttributeModifier.Operation> OPERATION_CODEC = Codec.STRING.xmap(
            s -> switch (s.toLowerCase(Locale.ROOT)) {
                case "add" -> AttributeModifier.Operation.ADDITION;
                case "multiply_base" -> AttributeModifier.Operation.MULTIPLY_BASE;
                case "multiply_total" -> AttributeModifier.Operation.MULTIPLY_TOTAL;
                default -> throw new IllegalArgumentException("Unknown operation: " + s);
            },
            op -> switch (op) {
                case ADDITION -> "add";
                case MULTIPLY_BASE -> "multiply_base";
                case MULTIPLY_TOTAL -> "multiply_total";
            }
    );

    public static final Codec<AttributeModifier> ATTRIBUTE_MODIFIER_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("name").forGetter(AttributeModifier::getName),
                    Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifier::getAmount),
                    //Codec.STRING.fieldOf("operation").forGetter(AttributeModifier::getOperation)
                    Codec.INT.fieldOf("operation").forGetter(mod -> mod.getOperation().ordinal()),
                    Codec.STRING.fieldOf("uuid").forGetter(mod -> mod.getId().toString())
            ).apply(instance, (name, amount, operation, uuid) ->
                    new AttributeModifier(UUID.fromString(uuid), name, amount, AttributeModifier.Operation.values()[operation])
            )
    );


    public static final Codec<Multimap<Attribute, AttributeModifier>> MULTIMAP_CODEC =
            Codec.unboundedMap(ATTRIBUTE_CODEC, Codec.list(ATTRIBUTE_MODIFIER_CODEC))
                    .xmap(map -> {
                        // Convert Map<Attribute, List<AttributeModifier>> → Multimap
                        HashMultimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
                        map.forEach((key, list) -> multimap.putAll(key, list));
                        return multimap;
                    }, multimap -> {
                        // Convert Multimap → Map<Attribute, List<AttributeModifier>>
                        Map<Attribute, java.util.List<AttributeModifier>> map = new java.util.HashMap<>();
                        for (Attribute key : multimap.keySet()) {
                            map.put(key, new java.util.ArrayList<>(multimap.get(key)));
                        }
                        return map;
                    });

    public static final Codec<EquipmentSlot> SLOT_CODEC = Codec.STRING.xmap(
            s -> EquipmentSlot.byName(s.toLowerCase(Locale.ROOT)),
            EquipmentSlot::getName
    );

    public static final Codec<Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>>> SLOT_MAP_CODEC =
            Codec.unboundedMap(SLOT_CODEC, MULTIMAP_CODEC)
                    .xmap(map -> {
                        EnumMap<EquipmentSlot, Multimap<Attribute, AttributeModifier>> enumMap = new EnumMap<>(EquipmentSlot.class);
                        enumMap.putAll(map);
                        return enumMap;
                    }, enumMap -> new HashMap<>(enumMap));
    */

    public static final Codec<ItemAttributesEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("item").forGetter(ItemAttributesEntry::item),
                    SLOT_MAP_CODEC.fieldOf("attributes").forGetter(ItemAttributesEntry::attributes)
            ).apply(instance, ItemAttributesEntry::new)
    );

    /*
    public static final Codec<AttributeModifier.Operation> OPERATION_CODEC = Codec.STRING.xmap(
            name -> AttributeModifier.Operation.valueOf(name.toUpperCase()),
            operation -> operation.name().toLowerCase()
    );

    public static final Codec<AttributeModifier> ATTRIBUTE_MODIFIER_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("uuid").xmap(UUID::fromString, UUID::toString).forGetter(AttributeModifier::getId),
            Codec.STRING.fieldOf("name").forGetter(AttributeModifier::getName),
            Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifier::getAmount),
            OPERATION_CODEC.fieldOf("operation").forGetter(AttributeModifier::getOperation)
    ).apply(instance, AttributeModifier::new));

    private static final Codec<Map<Attribute, List<AttributeModifier>>> ATTRIBUTE_MAP_CODEC = Codec.unboundedMap(
            ForgeRegistries.ATTRIBUTES.getCodec(),
            Codec.list(ATTRIBUTE_MODIFIER_CODEC)
    );

    private static final Codec<Multimap<Attribute, AttributeModifier>> ATTRIBUTES_MULTIMAP_CODEC = ATTRIBUTE_MAP_CODEC.xmap(
            map -> {
                Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
                map.forEach((attribute, modifiers) -> multimap.putAll(attribute, modifiers));
                return multimap;
            },
            multimap -> multimap.asMap().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> (List<AttributeModifier>) entry.getValue()))
    );

    public static final Codec<EquipmentSlot> EQUIPMENT_SLOT_CODEC = Codec.STRING.xmap(
            name -> EquipmentSlot.byName(name.toLowerCase()),
            EquipmentSlot::getName
    );

    public static final Codec<Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>>> EQUIPMENT_ATTRIBUTES_MAP_CODEC = Codec.unboundedMap(
            EQUIPMENT_SLOT_CODEC,
            ATTRIBUTES_MULTIMAP_CODEC
    );

    public static final Codec<ItemAttributesEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("item").forGetter(ItemAttributesEntry::item),
            EQUIPMENT_ATTRIBUTES_MAP_CODEC.fieldOf("attributes").forGetter(ItemAttributesEntry::attributes)
    ).apply(instance, ItemAttributesEntry::new));
    */

   /*public static final Codec<ItemAttributesEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("item").forGetter(ItemAttributesEntry::item)
            ).apply(instance, ItemAttributesEntry::new)
    );*/
}