package com.orca.inventoryweight;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryWeight implements ModInitializer {
    public static final String MOD_ID = "inventory-weight";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Inventory Weight mod initialized!");
    }
}
