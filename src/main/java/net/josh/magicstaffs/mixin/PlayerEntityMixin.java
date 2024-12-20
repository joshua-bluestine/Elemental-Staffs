package net.josh.magicstaffs.mixin;

import net.josh.magicstaffs.ModItems;
import net.josh.magicstaffs.effects.ModEffects;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Unique
    PlayerEntity player = (PlayerEntity) (Object) this;
    @Inject(method="tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.getWorld().isClient()) {
            if (entity.hasStatusEffect(ModEffects.GRAVITY_EFFECT)) {
                for (int i = 1; i < 5; ++i) {
                    ((ServerWorld) entity.getWorld()).spawnParticles(ParticleTypes.ENCHANTED_HIT,
                            entity.getParticleX(1.0D),
                            entity.getY() + 1,
                            entity.getParticleZ(1.0D),
                            1,
                            entity.getWorld().random.nextGaussian() * 0.2D,
                            entity.getWorld().random.nextGaussian() * 0.2D,
                            entity.getWorld().random.nextGaussian() * 0.2D,
                            0.0);
                }
            }
        }

    }
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if (player.hasStatusEffect(ModEffects.GRAVITY_EFFECT)) {
            if (source.isOf(DamageTypes.FALL)) {
                cir.setReturnValue(false);
            }
        }
        if (source.isOf(DamageTypes.LAVA)){
            shouldDamage(cir, 5, ModItems.LAVA_STAFF);
        }
        if (source.isOf(DamageTypes.MOB_ATTACK_NO_AGGRO)){
            shouldDamage(cir, 5, ModItems.CLOAKING_STAFF);
        }
        if (source.isOf(DamageTypes.FREEZE)){
            shouldDamage(cir, 10, ModItems.ICE_STAFF);
        }
        if (source.isOf(DamageTypes.DROWN)){
            shouldDamage(cir, 10, ModItems.WATER_STAFF);
        }
        if (source.isOf(DamageTypes.LIGHTNING_BOLT)){
            shouldDamage(cir, 1, ModItems.STORM_STAFF, ModItems.LIGHTNING_STAFF);
        }
        if (source.isOf(DamageTypes.IN_FIRE)){
            shouldDamage(cir, 2, ModItems.LAVA_STAFF, ModItems.FIRE_STAFF, ModItems.STORM_STAFF);
        }
        if (source.isOf(DamageTypes.ON_FIRE)){
            shouldDamage(cir, 2, ModItems.LAVA_STAFF, ModItems.FIRE_STAFF, ModItems.STORM_STAFF);
        }
    }

    @Unique
    private void shouldDamage(CallbackInfoReturnable<Boolean> cir, int amount, Item... item) {
        for (Item i : item) {
            if (player.getMainHandStack().getItem() == i) {
                player.getMainHandStack().damage(amount, player, EquipmentSlot.MAINHAND);
                cir.setReturnValue(false);
            } else if (player.getOffHandStack().getItem() == i) {
                player.getOffHandStack().damage(amount, player, EquipmentSlot.OFFHAND);
                cir.setReturnValue(false);
            }
        }
    }
}
