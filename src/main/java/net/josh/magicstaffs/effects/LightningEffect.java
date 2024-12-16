package net.josh.magicstaffs.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;

public class LightningEffect extends StatusEffect {

    // Constructor to set the effect type (beneficial) and color (blue)
    public LightningEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x0000FF); // 0x0000FF is the color blue
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        return true;        // Additional effects can be applied here if necessary, like health regeneration
    }

    @Override
    public boolean isInstant() {
        return false;  // This effect is not instant (it takes time to be applied)
    }
}