package bg.maznacraft.mazna_item_lib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryManager;
import org.apache.commons.compress.harmony.pack200.AttributeDefinitionBands;
import org.apache.http.config.Registry;

@Mod.EventBusSubscriber(modid = MaznaItemLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DynamicAttributeHandler {

    @SubscribeEvent
    public static void addAttributes(ItemAttributeModifierEvent event) {

        //DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MaznaItemLib.MOD_ID);
        //ITEMS.getEntries()

        ItemStack stack = event.getItemStack();

        if(stack.getItem().getDescriptionId().equals("item.minecraft.shears")) return;

        MaznaItemLib.LOGGER.error("4090");
        MaznaItemLib.LOGGER.error(stack.getItem().getName(stack).getString());
        MaznaItemLib.LOGGER.error(stack.getDescriptionId());
        MaznaItemLib.LOGGER.error(stack.getItem().getDescriptionId());

        //MaznaItemLib.LOGGER.error(DynamicAttributeRegistry.ITEM_ATTRIBUTE_REGISTRY.getEntries().toString());
        //DynamicAttributeRegistry.ITEM_ATTRIBUTE_REGISTRY.;
        //if (DynamicAttributeRegistry.ITEM_ATTRIBUTE_REGISTRY.getEntries()) return;

        /*for (AttributeDefinitionBands.AttributeDefinition def : DynamicAttributeRegistry.ATTRIBUTES.get(id)) {
            if (def.slot() == event.getSlot()) {
                event.addModifier(def.attribute(),
                        new AttributeModifier(UUID.randomUUID(), def.name(), def.amount(), def.operation()));
            }
        }*/
    }
}