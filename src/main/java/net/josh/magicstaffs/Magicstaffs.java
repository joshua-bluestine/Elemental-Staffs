package net.josh.magicstaffs;

import net.fabricmc.api.ModInitializer;
import net.josh.magicstaffs.blocks.ModBlocks;
import net.josh.magicstaffs.effects.ModEffects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Magicstaffs implements ModInitializer {
    public static String MOD_ID = "magicstaffs";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    @Override
    public void onInitialize() {
        ModItems.registerItems();
        ModItemGroups.registerItemGroups();
        ModBlocks.registerBlocks();
        ModEffects.registerModEffects();
    }
}
