package net.josh.magicstaffs;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final ItemGroup MAGIC_STAFFS = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of(Magicstaffs.MOD_ID, "magic_staffs"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.FIRE_STAFF)).displayName(Text.translatable("itemgroup.magicstaffs.magic_staffs")).
                    entries((displayContext, entries) -> {
                        entries.add(ModItems.FIRE_STAFF);
                        entries.add(ModItems.ICE_STAFF);
                        entries.add(ModItems.GRAVITY_STAFF);
                        entries.add(ModItems.LIGHTNING_STAFF);
                        entries.add(ModItems.STORM_STAFF);
                        entries.add(ModItems.FOREST_STAFF);
                        entries.add(ModItems.WATER_STAFF);
                        entries.add(ModItems.WIND_STAFF);
                        entries.add(ModItems.WITHER_STAFF);
                        entries.add(ModItems.REDSTONE_STAFF);
                        entries.add(ModItems.LAVA_STAFF);
                        entries.add(ModItems.ENDER_STAFF);
                        entries.add(ModItems.WARDEN_STAFF);
                        entries.add(ModItems.HEAL_STAFF);
                        entries.add(ModItems.CLOAKING_STAFF);
                    }).build());

    public static void registerItemGroups(){
        Magicstaffs.LOGGER.info("Registering Item Groups for " + Magicstaffs.MOD_ID);
    }
}
