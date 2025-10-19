package bg.maznacraft.mazna_item_lib.mixin;

import bg.maznacraft.mazna_item_lib.DynamicAttributeRegistry;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Unique
    private DynamicAttributeRegistry.DynamicItemAttributes_Old dynamicAttributes;

    @Inject(method = "getAttributeModifiers", at = @At("RETURN"), cancellable = true)
    private void injectAttributes(EquipmentSlot pSlot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {

        //MaznaItemLib.LOGGER.error("050505055005");

        if (dynamicAttributes != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(cir.getReturnValue());
            builder.putAll(dynamicAttributes.getAttributesForSlot(pSlot));
            cir.setReturnValue(builder.build());
        }
    }

    @Unique
    public void setDynamicAttributes(DynamicAttributeRegistry.DynamicItemAttributes_Old attrs) {
        this.dynamicAttributes = attrs;
    }

    @Unique
    public DynamicAttributeRegistry.DynamicItemAttributes_Old getDynamicAttributes() {
        return dynamicAttributes;
    }
}
