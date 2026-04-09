package com.orca.inventoryweight.mixin;

import com.orca.inventoryweight.WeightCalculator;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Unique
    private static final Identifier WEIGHT_MODIFIER_ID = Identifier.of("inventory-weight", "weight_slowdown");

    @Unique
    private int tickCounter = 0;

    @Shadow
    public abstract PlayerInventory getInventory();

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        // Only update every 10 ticks to reduce overhead
        tickCounter++;
        if (tickCounter < 10) {
            return;
        }
        tickCounter = 0;

        PlayerEntity player = (PlayerEntity)(Object)this;

        // Calculate speed multiplier based on inventory weight
        float speedMultiplier = WeightCalculator.calculateSpeedMultiplier(getInventory());

        EntityAttributeInstance speedAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (speedAttribute == null) {
            return;
        }

        // Always remove existing modifier first
        speedAttribute.removeModifier(WEIGHT_MODIFIER_ID);

        // Only add modifier if we need to slow down
        if (speedMultiplier < 0.99f) {
            // Clamp to minimum 60% speed
            speedMultiplier = Math.max(speedMultiplier, 0.6f);

            // The modifier value should be (multiplier - 1) for ADD_MULTIPLIED_TOTAL
            // e.g., for 80% speed: modifier = -0.2
            double modifierValue = speedMultiplier - 1.0;

            EntityAttributeModifier modifier = new EntityAttributeModifier(
                WEIGHT_MODIFIER_ID,
                modifierValue,
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            );
            speedAttribute.addTemporaryModifier(modifier);
        }
    }
}
