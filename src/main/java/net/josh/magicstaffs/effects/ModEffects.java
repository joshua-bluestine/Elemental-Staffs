package net.josh.magicstaffs.effects;

import net.josh.magicstaffs.Magicstaffs;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEffects {



    private static RegistryEntry<StatusEffect> registerEffects(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Magicstaffs.MOD_ID,id), statusEffect);
    }
    public static void registerModEffects() {

        GRAVITY_EFFECT = registerEffects(
                "gravity_effect",
                new GravityEffect());
        FROSTBITE_EFFECT = registerEffects(
                "frostbite_effect",
                new FrostbiteEffect());
        LIGHTNING_EFFECT = registerEffects(
                "lightning_effect",
                new LightningEffect());
        LIGHTNING_EFFECT_SPREAD = registerEffects(
                "lightning_effect_spread",
                new LightningEffect());
        System.out.println("Registering Effects for " + Magicstaffs.MOD_ID);
    }
    public static RegistryEntry<StatusEffect> FROSTBITE_EFFECT;
    public static RegistryEntry<StatusEffect> LIGHTNING_EFFECT;
    public static RegistryEntry<StatusEffect> LIGHTNING_EFFECT_SPREAD;
    public static RegistryEntry<StatusEffect> GRAVITY_EFFECT;
}