package bg.maznacraft.mazna_item_lib.items;

import bg.maznacraft.mazna_item_lib.MaznaItemLib;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.apache.http.config.Registry;

import java.util.function.Consumer;

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


}
