package com.orca.inventoryweight;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class InventoryWeightClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(this::renderWeightHud);
        InventoryWeight.LOGGER.info("Inventory Weight client initialized!");
    }

    private void renderWeightHud(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.options.hudHidden) {
            return;
        }

        // Recalculate weight for accurate HUD display
        WeightCalculator.calculateSpeedMultiplier(client.player.getInventory());

        int speedPercent = WeightCalculator.getSpeedPercentage();
        int weightPercent = WeightCalculator.getWeightPercentage();

        // Format display text
        String speedText = speedPercent + "%";
        String weightText = "W:" + weightPercent + "%";

        TextRenderer textRenderer = client.textRenderer;

        // Position in top-left corner with padding
        int x = 5;
        int y = 5;

        // Choose color based on speed (green when fast, yellow/red when slow)
        int color;
        if (speedPercent >= 90) {
            color = 0x55FF55; // Green
        } else if (speedPercent >= 75) {
            color = 0xFFFF55; // Yellow
        } else if (speedPercent >= 65) {
            color = 0xFFAA00; // Orange
        } else {
            color = 0xFF5555; // Red
        }

        // Draw background for better visibility
        int textWidth = Math.max(textRenderer.getWidth(speedText), textRenderer.getWidth(weightText));
        context.fill(x - 2, y - 2, x + textWidth + 4, y + 22, 0x80000000);

        // Draw speed percentage (main display)
        context.drawText(textRenderer, speedText, x, y, color, true);

        // Draw weight percentage below in gray
        context.drawText(textRenderer, weightText, x, y + 12, 0xAAAAAA, true);
    }
}
