package net.josh.magicstaffs.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;

public class GravityEffect extends StatusEffect {

    public GravityEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x0000FF);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        return true;
    }

    @Override
    public boolean isInstant() {
        return false;
    }
}