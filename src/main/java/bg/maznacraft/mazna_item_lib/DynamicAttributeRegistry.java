package bg.maznacraft.mazna_item_lib;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

import java.util.*;

//@Mod.EventBusSubscriber(modid = MaznaItemLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DynamicAttributeRegistry {

    public static class DynamicItemAttributes
    {
        private Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> attributes;

        public DynamicItemAttributes()
        {
            attributes = new EnumMap<>(EquipmentSlot.class);

            for(EquipmentSlot slot : EquipmentSlot.values())
            {
                attributes.put(slot,  HashMultimap.create());
            }
        }

        public DynamicItemAttributes(Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> InAttributes)
        {
            attributes = InAttributes;
        }

        public Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> getAttributes() {
            return attributes;
        }

        public Multimap<Attribute, AttributeModifier> getAttributesForSlot(EquipmentSlot slot) {
            return attributes.get(slot);
        }
    }

    //public static final DeferredRegister<DynamicItemAttributes> EXAMPLE = DeferredRegister.create(ResourceLocation.tryBuild(MaznaItemLib.MOD_ID, "dynamic_item_attributes"), MaznaItemLib.MOD_ID);

    //public static final Supplier<IForgeRegistry<DynamicItemAttributes>> REGISTRY = EXAMPLE.makeRegistry(RegistryBuilder::new);

   // public static final ResourceKey<Registry<DynamicItemAttributes>> DYNAMIC_ATTRIBUTE_KEY__ =
     //       ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MaznaItemLib.MOD_ID, "dynamic_item_attributes"));

    /*public static final ResourceKey<Registry<DynamicItemAttributes>> DYNAMIC_ATTRIBUTES;
    //public static final IForgeRegistry<DynamicItemAttributes> DYNAMIC_ATTRIBUTE_KEY;
    public static final DeferredRegister<DynamicItemAttributes> ITEM_ATTRIBUTE_REGISTRY;
    public static final RegistryObject<DynamicItemAttributes> item;

    static{
        DYNAMIC_ATTRIBUTES = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MaznaItemLib.MOD_ID, "dynamic_item_attributes"));

        if(DYNAMIC_ATTRIBUTES)

        //DYNAMIC_ATTRIBUTE_KEY = RegistryManager.ACTIVE.getRegistry(DYNAMIC_ATTRIBUTES);

        ITEM_ATTRIBUTE_REGISTRY = DeferredRegister.create(DYNAMIC_ATTRIBUTES,MaznaItemLib.MOD_ID);


    }*/

    /*public static final RegistryObject<DynamicItemAttributes> shears = ITEM_ATTRIBUTE_REGISTRY.register("shears",
            () -> {
                // Create the outer map
                //Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> attributeMap = new EnumMap<>(EquipmentSlot.class);

                // Create a multimap for the FEET slot
                //Multimap<Attribute, AttributeModifier> feetModifiers = HashMultimap.create();

                // Add some modifiers
                feetModifiers.put(
                        //new Attribute(),
                        Attributes.MOVEMENT_SPEED,
                        new AttributeModifier(
                                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                                "shears_speed",
                                0.05,
                                AttributeModifier.Operation.MULTIPLY_TOTAL
                        )
                );

                feetModifiers.put(
                        Attributes.ARMOR,
                        new AttributeModifier(
                                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                                "shears_armor",
                                2.0,
                                AttributeModifier.Operation.ADDITION
                        )
                );

                // Put the multimap in the map
                //attributeMap.put(EquipmentSlot.FEET, feetModifiers);

                // Return the recor
                DynamicItemAttributes DIA = new DynamicItemAttributes();
                if(DIA == null)
                {
                    MaznaItemLib.LOGGER.error("HSTE MU EBA MAIKATA");
                }
                MaznaItemLib.LOGGER.error(DIA.toString());
                return DIA;
            });*/

    /*@SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        event.create(
         new RegistryBuilder<DynamicItemAttributes>()
                .setName(DYNAMIC_ATTRIBUTE_KEY.location())
        );
    }*/

    public static void register(IEventBus eventBus)
    {

        //PER_ITEM_ATTRIBUTES.makeRegistry(() -> )
       //ITEM_ATTRIBUTE_REGISTRY.register(eventBus);

    }
}
