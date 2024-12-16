package net.josh.magicstaffs;

import net.minecraft.entity.Entity;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

public class CustomExplosionBehavior extends ExplosionBehavior {
    public CustomExplosionBehavior(){

    }
    @Override
    public boolean shouldDamage(Explosion explosion, Entity entity) {
        return false;
    }

}
