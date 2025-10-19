package bg.maznacraft.mazna_item_lib.items;

import bg.maznacraft.mazna_item_lib.MaznaItemLib;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.*;
import org.apache.http.config.Registry;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = MaznaItemLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MaznaItemLib.MOD_ID);

    //public static final RegistryObject<Item> CUSTOM_SWORD = ITEMS.register("custom_sword", () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus)
    {
        //ITEMS.register(eventBus);

        Item i = new Item(new Item.Properties());

        //ITEMS.register("custom_item",
                //() -> new Item( new Item.Properties().rarity(Rarity.RARE) ));

    }

    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        // Iterate over all items
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (item instanceof SwordItem) {
                // Example: modify attributes
                //DynamicAttributeHandler.modifyItem(item);
                MaznaItemLib.LOGGER.error("---------------------------------------------------------------" + item.getDescriptionId());
                //ForgeRegistries.ITEMS.
                //ForgeRegistries.ITEMS.getValue();
                //Arrow
            }
        }
    }

}
