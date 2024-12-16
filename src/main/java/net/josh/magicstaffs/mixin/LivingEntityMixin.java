package net.josh.magicstaffs.mixin;

import net.josh.magicstaffs.effects.ModEffects;
import net.josh.magicstaffs.staffs.StaffItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    int counter = 0;
    @Inject(method="tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.getWorld().isClient()) {
            if (entity.hasStatusEffect(ModEffects.LIGHTNING_EFFECT)) {
                for (int i = 1; i < 5; ++i) {
                    ((ServerWorld) entity.getWorld()).spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                            entity.getParticleX(1.0D),
                            entity.getY() + 1,
                            entity.getParticleZ(1.0D),
                            1,
                            entity.getWorld().random.nextGaussian() * 0.2D,
                            entity.getWorld().random.nextGaussian() * 0.2D,
                            entity.getWorld().random.nextGaussian() * 0.2D,
                            0.0);
                }
                if (counter < 20) {
                    counter++;
                } else {
                    counter = 0;
                    Vec3d pos = entity.getPos();
                    Box box = new Box(pos.add(-5, -1, -5), pos.add(5, 1, 5));
                    for (LivingEntity jared : entity.getWorld().getEntitiesByClass(LivingEntity.class, box, (livingEntity) -> true)) {
                        if (jared != entity && !(jared instanceof PlayerEntity)) {
                            jared.damage((ServerWorld) jared.getWorld(), jared.getDamageSources().magic(), 4);
                            StaffItem.fireLaser(entity.getWorld(), entity, ParticleTypes.ELECTRIC_SPARK, jared);
                        }
                    }
                }
            } else if (entity.hasStatusEffect(ModEffects.LIGHTNING_EFFECT_SPREAD)) {
                for (int i = 1; i < 10; ++i) {
                    ((ServerWorld) entity.getWorld()).spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                            entity.getParticleX(1.0D),
                            entity.getY() + 1,
                            entity.getParticleZ(1.0D),
                            1,
                            entity.getWorld().random.nextGaussian() * 0.2D,
                            entity.getWorld().random.nextGaussian() * 0.2D,
                            entity.getWorld().random.nextGaussian() * 0.2D,
                            0.0);
                }
                if (counter < 20) {
                    counter++;
                } else {
                    counter = 0;
                    Vec3d pos = entity.getPos();
                    Box box = new Box(pos.add(-5, -1, -5), pos.add(5, 1, 5));
                    for (LivingEntity jared : entity.getWorld().getEntitiesByClass(LivingEntity.class, box, (livingEntity) -> true)) {
                        if (jared != entity && !(jared instanceof PlayerEntity)) {
                            jared.addStatusEffect(new StatusEffectInstance(ModEffects.LIGHTNING_EFFECT, 40, 1, true, true), jared);
                            jared.damage((ServerWorld) jared.getWorld(), jared.getDamageSources().magic(), 4);
                            StaffItem.fireLaser(entity.getWorld(), entity, ParticleTypes.ELECTRIC_SPARK, jared);
                        }
                    }
                }
            }
        }
    }
}
