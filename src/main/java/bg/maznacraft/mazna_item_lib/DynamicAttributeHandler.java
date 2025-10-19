package bg.maznacraft.mazna_item_lib;

import bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes.DynamicItemAttributes;
import bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes.ItemAttributesDatapackRegistry;
import bg.maznacraft.mazna_item_lib.registry.dynamic_item_attributes.ItemAttributesEntry;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryManager;
import org.apache.commons.compress.harmony.pack200.AttributeDefinitionBands;
import org.apache.http.config.Registry;

import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = MaznaItemLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DynamicAttributeHandler {

    @SubscribeEvent
    public static void onItemAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());

        //if(itemId.toString().equalsIgnoreCase("minecraft:diamond_sword")) MaznaItemLib.LOGGER.error("startup: " + itemId.toString());

        // Look up the cached attributes for this item
        Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> entry = ItemAttributesDatapackRegistry.GetDynamicAttributes(itemId);
        if (entry == null) return;


        //MaznaItemLib.LOGGER.error("Post attributes: " + itemId.toString());

        // Apply attributes to the corresponding equipment slot
        entry.get(event.getSlotType())
                .forEach(event::addModifier);
    }


    /*@SubscribeEvent
    public static void addAttributes(ItemAttributeModifierEvent event) {

        //DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MaznaItemLib.MOD_ID);
        //ITEMS.getEntries()

        ItemStack stack = event.getItemStack();

        if(!stack.getItem().getDescriptionId().equals("item.minecraft.shears")
        && !stack.getItem().getDescriptionId().equals("item.minecraft.iron_boots")) return;

        //MaznaItemLib.LOGGER.error("4090");
        //MaznaItemLib.LOGGER.error(stack.getItem().getName(stack).getString());
        //MaznaItemLib.LOGGER.error(stack.getDescriptionId());
        EquipmentSlot slot = event.getSlotType();
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(event.getItemStack().getItem());
        //boolean isClient = event.getEntity().level().isClientSide;
        //event.getItemStack().att
        //System.out.println("[ItemAttributeModifierEvent] "
               // + itemId + " | Slot: " + slot );

        //MaznaItemLib.LOGGER.error(DynamicAttributeRegistry.ITEM_ATTRIBUTE_REGISTRY.getEntries().toString());
        //DynamicAttributeRegistry.ITEM_ATTRIBUTE_REGISTRY.;
        //if (DynamicAttributeRegistry.ITEM_ATTRIBUTE_REGISTRY.getEntries()) return;

        /*for (AttributeDefinitionBands.AttributeDefinition def : DynamicAttributeRegistry.ATTRIBUTES.get(id)) {
            if (def.slot() == event.getSlot()) {
                event.addModifier(def.attribute(),
                        new AttributeModifier(UUID.randomUUID(), def.name(), def.amount(), def.operation()));
            }
        }
        if(event.getSlotType() == EquipmentSlot.FEET)
        {
            event.addModifier(Attributes.ARMOR,
                    new AttributeModifier(UUID.randomUUID(), "fortnite", 10, AttributeModifier.Operation.ADDITION));
        }
        if(event.getSlotType() == EquipmentSlot.MAINHAND)
        {
            event.addModifier(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(UUID.randomUUID(), "fortnite", 7, AttributeModifier.Operation.ADDITION));
        }
        if(event.getSlotType() == EquipmentSlot.OFFHAND)
        {
            event.addModifier(Attributes.ATTACK_SPEED,
                    new AttributeModifier(UUID.randomUUID(), "fortnite", 2, AttributeModifier.Operation.ADDITION));
        }
        ItemStack.of(new CompoundTag());
    }*/
}