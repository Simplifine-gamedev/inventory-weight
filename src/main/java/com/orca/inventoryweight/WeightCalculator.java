package com.orca.inventoryweight;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class WeightCalculator {

    // Weight values per item
    public static final float WEIGHT_VERY_HEAVY = 5.0f;  // Shulker boxes, anvils
    public static final float WEIGHT_HEAVY = 2.0f;       // Stone, ores, metals
    public static final float WEIGHT_MEDIUM = 1.0f;      // Wood, default items
    public static final float WEIGHT_LIGHT = 0.3f;       // Food, feathers, plants
    public static final float WEIGHT_VERY_LIGHT = 0.1f;  // Paper, string

    // Maximum weight for full slowdown (carrying 36 slots of very heavy items)
    public static final float MAX_WEIGHT = 36 * WEIGHT_VERY_HEAVY * 64;

    // Minimum speed multiplier (60% at max weight)
    public static final float MIN_SPEED_MULTIPLIER = 0.6f;

    // Cached values for HUD
    private static float lastCalculatedWeight = 0;
    private static float lastSpeedMultiplier = 1.0f;

    public static float calculateTotalWeight(PlayerInventory inventory) {
        float totalWeight = 0;

        // Main inventory (0-35) and offhand (40)
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                totalWeight += getItemWeight(stack.getItem()) * stack.getCount();
            }
        }

        lastCalculatedWeight = totalWeight;
        return totalWeight;
    }

    public static float getItemWeight(Item item) {
        String itemId = Registries.ITEM.getId(item).getPath();

        // Very heavy items - shulker boxes, anvils, enchanting tables
        if (itemId.contains("shulker_box") ||
            item == Items.ANVIL || item == Items.CHIPPED_ANVIL || item == Items.DAMAGED_ANVIL ||
            item == Items.ENCHANTING_TABLE || item == Items.BEACON) {
            return WEIGHT_VERY_HEAVY;
        }

        // Heavy items - stone, ores, metals, bricks
        if (itemId.contains("ore") ||
            itemId.contains("_block") && (itemId.contains("iron") || itemId.contains("gold") ||
                itemId.contains("diamond") || itemId.contains("emerald") || itemId.contains("copper") ||
                itemId.contains("netherite") || itemId.contains("lapis")) ||
            item == Items.STONE || item == Items.COBBLESTONE || item == Items.DEEPSLATE ||
            item == Items.COBBLED_DEEPSLATE || item == Items.ANDESITE || item == Items.DIORITE ||
            item == Items.GRANITE || item == Items.OBSIDIAN || item == Items.CRYING_OBSIDIAN ||
            itemId.contains("brick") || itemId.contains("iron_") || itemId.contains("gold_") ||
            itemId.contains("diamond_") || itemId.contains("netherite_") ||
            item == Items.IRON_INGOT || item == Items.GOLD_INGOT || item == Items.COPPER_INGOT ||
            item == Items.NETHERITE_INGOT || item == Items.RAW_IRON || item == Items.RAW_GOLD ||
            item == Items.RAW_COPPER) {
            return WEIGHT_HEAVY;
        }

        // Light items - food, feathers, plants, flowers
        if (item.getDefaultStack().contains(DataComponentTypes.FOOD) ||
            item == Items.FEATHER || item == Items.STRING || item == Items.STICK ||
            itemId.contains("seed") || itemId.contains("flower") || itemId.contains("sapling") ||
            item == Items.WHEAT || item == Items.SUGAR_CANE || item == Items.BAMBOO ||
            item == Items.KELP || item == Items.VINE || item == Items.LILY_PAD ||
            itemId.contains("leaves") || item == Items.MOSS_BLOCK || item == Items.MOSS_CARPET) {
            return WEIGHT_LIGHT;
        }

        // Very light items - paper, maps
        if (item == Items.PAPER || item == Items.MAP || item == Items.FILLED_MAP ||
            item == Items.BOOK || item == Items.WRITABLE_BOOK || item == Items.WRITTEN_BOOK ||
            item == Items.KNOWLEDGE_BOOK) {
            return WEIGHT_VERY_LIGHT;
        }

        // Medium weight for wood and everything else
        if (itemId.contains("log") || itemId.contains("planks") || itemId.contains("wood")) {
            return WEIGHT_MEDIUM;
        }

        // Default medium weight
        return WEIGHT_MEDIUM;
    }

    public static float calculateSpeedMultiplier(PlayerInventory inventory) {
        float weight = calculateTotalWeight(inventory);

        // Smooth scaling from 1.0 (empty) to MIN_SPEED_MULTIPLIER (at max weight)
        // Using a softer curve so light loads don't feel punishing
        float weightRatio = Math.min(weight / MAX_WEIGHT, 1.0f);

        // Use square root for smoother curve - light loads barely affect speed
        float curve = (float) Math.sqrt(weightRatio);
        float multiplier = 1.0f - (1.0f - MIN_SPEED_MULTIPLIER) * curve;

        lastSpeedMultiplier = multiplier;
        return multiplier;
    }

    public static float getLastCalculatedWeight() {
        return lastCalculatedWeight;
    }

    public static float getLastSpeedMultiplier() {
        return lastSpeedMultiplier;
    }

    // Convert weight to a display-friendly percentage
    public static int getWeightPercentage() {
        float ratio = lastCalculatedWeight / MAX_WEIGHT;
        return Math.min((int)(ratio * 100), 100);
    }

    // Get speed as percentage for display
    public static int getSpeedPercentage() {
        return (int)(lastSpeedMultiplier * 100);
    }
}
