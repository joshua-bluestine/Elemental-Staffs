package net.josh.magicstaffs.blocks;

import net.josh.magicstaffs.Magicstaffs;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModBlocks {


    public static Block REDSTONE_STAFF_BLOCK = register("redstone_staff_block",
            RedstoneStaffBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.CLEAR)
                    .noCollision()
                    .sounds(BlockSoundGroup.SOUL_SOIL)
                    .dropsNothing()
                    .noBlockBreakParticles());


    public static Block register(RegistryKey<Block> key, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        Block block = (Block)factory.apply(settings.registryKey(key));
        return (Block)Registry.register(Registries.BLOCK, key, block);
    }

    private static RegistryKey<Block> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.ofVanilla(id));
    }

    private static Block register(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        return register(keyOf(id), factory, settings);
    }

    public static void registerBlocks() {
        Magicstaffs.LOGGER.info("Registering Mod Blocks for " + Magicstaffs.MOD_ID);

    }
}
