package net.josh.magicstaffs;

import net.josh.magicstaffs.staffs.StaffItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item FIRE_STAFF = registerStaffItem("fire_staff");
    public static final Item ICE_STAFF = registerStaffItem("ice_staff");
    public static final Item GRAVITY_STAFF = registerStaffItem("gravity_staff");
    public static final Item FOREST_STAFF = registerStaffItem("forest_staff");
    public static final Item WATER_STAFF = registerStaffItem("water_staff");
    public static final Item LIGHTNING_STAFF = registerStaffItem("lightning_staff");
    public static final Item STORM_STAFF = registerStaffItem("storm_staff");
    public static final Item WIND_STAFF = registerStaffItem("wind_staff");
    public static final Item ENDER_STAFF = registerStaffItem("ender_staff");
    public static final Item WITHER_STAFF = registerStaffItem("wither_staff");
    public static final Item LAVA_STAFF = registerStaffItem("lava_staff");
    public static final Item REDSTONE_STAFF = registerStaffItem("redstone_staff");
    public static final Item WARDEN_STAFF = registerStaffItem("warden_staff");
    public static final Item HEAL_STAFF = registerStaffItem("heal_staff");
    public static final Item CLOAKING_STAFF = registerStaffItem("cloaking_staff");

    private static Item registerStaffItem(String name){
         Identifier id = Identifier.of(Magicstaffs.MOD_ID, name);
         RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
         Item.Settings settings = new Item.Settings().registryKey(key).maxDamage(1500);
         if (name.equals("lava_staff") || name.equals("fire_staff")){
             settings = new Item.Settings().registryKey(key).maxDamage(1500).fireproof();
         }
         return Registry.register(Registries.ITEM, Identifier.of(Magicstaffs.MOD_ID, name), new StaffItem(settings, name));
    }
    public static void registerItems(){
        Magicstaffs.LOGGER.info("Registering Mod Items for " + Magicstaffs.MOD_ID);
    }
}
