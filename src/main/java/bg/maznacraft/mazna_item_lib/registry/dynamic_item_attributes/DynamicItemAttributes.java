package bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class DynamicItemAttributes {
    private final Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> attributes;

    public DynamicItemAttributes() {
        attributes = new EnumMap<>(EquipmentSlot.class);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            attributes.put(slot, HashMultimap.create());
        }
    }

    public DynamicItemAttributes(Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> inAttributes) {
        this.attributes = inAttributes;
    }

    public Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> getAttributes() {
        return attributes;
    }

    public Multimap<Attribute, AttributeModifier> getAttributesForSlot(EquipmentSlot slot) {
        return attributes.get(slot);
    }

    // 👇 We'll define a Codec for this below
    public static final Codec<DynamicItemAttributes> CODEC = createCodec();

    private static Codec<DynamicItemAttributes> createCodec() {
        // Slot codec
        Codec<EquipmentSlot> slotCodec = Codec.STRING.xmap(
                s -> EquipmentSlot.byName(s.toLowerCase(Locale.ROOT)),
                EquipmentSlot::getName
        );

        // Operation codec
        Codec<AttributeModifier.Operation> operationCodec = Codec.STRING.xmap(
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

        // Modifier codec
        Codec<AttributeModifier> modifierCodec = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("name").forGetter(AttributeModifier::getName),
                UUIDUtil.STRING_CODEC.fieldOf("uuid").forGetter(AttributeModifier::getId),
                Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifier::getAmount),
                operationCodec.fieldOf("operation").forGetter(AttributeModifier::getOperation)
        ).apply(instance, (name, uuid, amount, op) -> new AttributeModifier(uuid, name, amount, op)));

        // Attribute codec
        Codec<Attribute> attributeCodec = ResourceLocation.CODEC.xmap(
                ForgeRegistries.ATTRIBUTES::getValue,
                ForgeRegistries.ATTRIBUTES::getKey
        );

        // Multimap codec: Map<Attribute, List<AttributeModifier>>
        Codec<Multimap<Attribute, AttributeModifier>> multimapCodec =
                Codec.unboundedMap(attributeCodec, modifierCodec.listOf())
                        .xmap(map -> {
                            Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
                            map.forEach(multimap::putAll);
                            return multimap;
                        }, multimap -> {
                            Map<Attribute, List<AttributeModifier>> back = new HashMap<>();
                            multimap.asMap().forEach((attr, mods) -> back.put(attr, new ArrayList<>(mods)));
                            return back;
                        });

        // Main map codec: Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>>
        Codec<Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>>> attributesCodec =
                Codec.unboundedMap(slotCodec, multimapCodec);

        return attributesCodec.xmap(DynamicItemAttributes::new, DynamicItemAttributes::getAttributes);
    }
}