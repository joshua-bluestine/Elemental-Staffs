package net.josh.magicstaffs.staffs;

import net.josh.magicstaffs.ModUtils;
import net.josh.magicstaffs.blocks.ModBlocks;
import net.josh.magicstaffs.effects.ModEffects;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


import static net.minecraft.block.FluidBlock.LEVEL;

public class StaffItem extends Item {
    private String name = "";

    public StaffItem(Settings settings, String name) {
        super(settings);
        this.name = name;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            ServerWorld serverWorld = (ServerWorld) world;
            return switch (this.name) {
                case "gravity_staff" -> {
                    if (ModUtils.SHIFT){
                        user.addStatusEffect(new StatusEffectInstance(ModEffects.GRAVITY_EFFECT, 200, 2, true, true, true), null);
                        coolDownDamage(user, hand, 500, 100);
                    } else {
                        for (LivingEntity jared : fireLaser(serverWorld, user, ParticleTypes.ENCHANTED_HIT, 20)) {
                            jared.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 80, 3, false, false), user);
                            coolDownDamage(user, hand, 40, 10);
                        }
                    }
                    yield ActionResult.SUCCESS;
                }
                case "lightning_staff" -> {
                    if (ModUtils.SHIFT){
                        for (LivingEntity jared : fireLaser(serverWorld, user, ParticleTypes.ELECTRIC_SPARK, 20)) {
                            jared.addStatusEffect(new StatusEffectInstance(ModEffects.LIGHTNING_EFFECT_SPREAD, 160, 3, false, false), user);
                            coolDownDamage(user, hand, 300, 100);
                        }
                    } else {
                        for (LivingEntity jared : fireLaser(serverWorld, user, ParticleTypes.ELECTRIC_SPARK, 20)) {
                            jared.addStatusEffect(new StatusEffectInstance(ModEffects.LIGHTNING_EFFECT, 80, 3, false, false), user);
                            coolDownDamage(user, hand, 150, 40);
                        }
                    }
                    yield ActionResult.SUCCESS;
                }
                case "heal_staff" -> {
                    if (ModUtils.SHIFT){
                        for (int i = 1; i < 20; ++i) {
                            serverWorld.spawnParticles(ParticleTypes.HEART,
                                    user.getParticleX(1.0D),
                                    user.getY() + 1,
                                    user.getParticleZ(1.0D),
                                    1,
                                    world.random.nextGaussian() * 0.2D,
                                    world.random.nextGaussian() * 0.2D,
                                    world.random.nextGaussian() * 0.2D,
                                    0.0);
                        }
                        user.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 1, true, true, true), null);
                        coolDownDamage(user, hand, 500, 100);
                    } else {
                        for (LivingEntity jared : fireLaser(serverWorld, user, ParticleTypes.HEART, 20)) {
                            jared.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 1, true, true), user);
                            coolDownDamage(user, hand, 40, 20);
                        }
                    }
                    yield ActionResult.SUCCESS;
                }
                case "warden_staff" -> {
                    if (ModUtils.SHIFT){
                        fireLaser(serverWorld, user, ParticleTypes.SCULK_SOUL, 20);
                        if (user.raycast(20, 0, false) instanceof BlockHitResult blockHitResult) {
                            if (!world.getBlockState(blockHitResult.getBlockPos()).isAir()) {
                                if (world.getBlockState(blockHitResult.getBlockPos().offset(blockHitResult.getSide())).isAir()) {
                                    world.setBlockState(blockHitResult.getBlockPos().offset(blockHitResult.getSide()),Blocks.SCULK_CATALYST.getDefaultState());
                                    coolDownDamage(user, hand, 500, 100);
                                }
                            }
                        }
                    } else {
                        HitResult hitResult = user.raycast(16, 0.0F, false);
                        Vec3d vec3d = user.getPos().add(0.0, 1.6f, 0.0);
                        Vec3d vec3d2 = hitResult.getPos().subtract(vec3d);
                        Vec3d vec3d3 = vec3d2.normalize();

                        for (int i = 1; i < MathHelper.floor(vec3d2.length()) + 7; ++i) {
                            Vec3d vec3d4 = vec3d.add(vec3d3.multiply(i));
                            serverWorld.spawnParticles(ParticleTypes.SONIC_BOOM, vec3d4.x, vec3d4.y, vec3d4.z, 1, 0.0, 0.0, 0.0, 0.0);
                        }

                        Vec3d endPos = vec3d.add(vec3d3.multiply(vec3d2.length() + 1.0)); // Extend length a bit for the box
                        Box box = new Box(vec3d, endPos).expand(0.5); // Expand the box a little to cover a wider area
                        List<Entity> entities = serverWorld.getEntitiesByClass(Entity.class, box, entity -> entity instanceof LivingEntity);
                        for (Entity entity : entities) {
                            if (entity instanceof LivingEntity) {
                                if (!(entity instanceof PlayerEntity)) {
                                    LivingEntity target2 = (LivingEntity) entity;


                                    for (int i = 1; i < MathHelper.floor(vec3d2.length()) + 7; ++i) {
                                        Vec3d vec3d4 = vec3d.add(vec3d3.multiply(i));
                                        serverWorld.spawnParticles(ParticleTypes.SONIC_BOOM, vec3d4.x, vec3d4.y, vec3d4.z, 1, 0.0, 0.0, 0.0, 0.0);
                                    }

                                    user.playSound(SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 3.0f, 1.0f);
                                    entity.damage(serverWorld, serverWorld.getDamageSources().sonicBoom(user), 10.0f);

                                    double d = 0.5 * (1.0 - target2.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
                                    double e = 2.5 * (1.0 - target2.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
                                    entity.addVelocity(vec3d3.getX() * e, vec3d3.getY() * d, vec3d3.getZ() * e);
                                }
                            }
                        }
                        coolDownDamage(user, hand, 150, 50);
                    }
                    yield ActionResult.SUCCESS;
                }

                case "cloaking_staff" -> {
                    if (ModUtils.SHIFT){
                        user.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 400, 2, true, true, true), null);
                        user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 2, true, true, true), null);
                        coolDownDamage(user, hand, 1000, 100);
                    } else {
                        user.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 200, 2, true, true, true), null);
                        coolDownDamage(user, hand, 500, 50);
                    }
                    yield ActionResult.SUCCESS;
                }
                case "fire_staff" -> {
                    if (ModUtils.SHIFT) {
                        FireballEntity skull = new FireballEntity(EntityType.FIREBALL, world);
                        skull.setVelocity(user, user.getPitch(), user.getYaw(), 1.0f, 3f, 1.0f);
                        skull.setPos(user.getX(), user.getY() + 1, user.getZ());
                        world.spawnEntity(skull);
                        coolDownDamage(user, hand, 80, 25);
                    } else {
                        for (LivingEntity jared : fireLaser(serverWorld, user, ParticleTypes.FLAME, 20)) {
                            jared.damage(serverWorld, jared.getDamageSources().magic(), 1);
                            jared.setOnFireFor(5);
                            coolDownDamage(user, hand, 0, 5);
                        }
                    }
                    yield ActionResult.SUCCESS;
                }
                case "ender_staff" -> {
                    if (ModUtils.SHIFT) {
                        fireLaser(serverWorld, user, ParticleTypes.REVERSE_PORTAL, 40);
                        if (user.raycast(40, 0, false) instanceof BlockHitResult blockHitResult) {
                            if (!world.getBlockState(blockHitResult.getBlockPos()).isAir()) {
                                if (world.getBlockState(blockHitResult.getBlockPos().offset(blockHitResult.getSide())).isAir()) {
                                    BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
                                    user.teleport(pos.getX(), pos.getY(), pos.getZ(), false);
                                    for (int i = 1; i < 20; ++i) {
                                        serverWorld.spawnParticles(ParticleTypes.REVERSE_PORTAL,
                                                user.getParticleX(1.0D),
                                                user.getY() + 1,
                                                user.getParticleZ(1.0D),
                                                1,
                                                world.random.nextGaussian() * 0.2D,
                                                world.random.nextGaussian() * 0.2D,
                                                world.random.nextGaussian() * 0.2D,
                                                0.0);
                                    }
                                    coolDownDamage(user, hand, 500, 100);
                                }
                            }
                        }
                    } else {
                        for (LivingEntity jared : fireLaser(serverWorld, user, ParticleTypes.REVERSE_PORTAL, 20)) {
                            Vec3d teleportPos = user.getPos().add(user.getRotationVector().multiply(3, 0, 3));  // Teleport 2 blocks in front
                            jared.teleport(teleportPos.x, teleportPos.y, teleportPos.z, false);
                            for (int i = 1; i < 20; ++i) {
                                serverWorld.spawnParticles(ParticleTypes.REVERSE_PORTAL,
                                        jared.getParticleX(1.0D),
                                        jared.getY() + 1,
                                        jared.getParticleZ(1.0D),
                                        1,
                                        world.random.nextGaussian() * 0.2D,
                                        world.random.nextGaussian() * 0.2D,
                                        world.random.nextGaussian() * 0.2D,
                                        0.0);
                            }
                            jared.setVelocity(0,0,0);
                            coolDownDamage(user, hand, 40, 10);
                        }
                    }
                    yield ActionResult.SUCCESS;
                }
                case "forest_staff" -> {
                    if (ModUtils.SHIFT){
                        if (user.getHungerManager().isNotFull()) {
                            user.getHungerManager().add(2, 1);
                            for (int i = 1; i < 20; ++i) {
                                serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                                        user.getParticleX(1.0D),
                                        user.getY() + 1,
                                        user.getParticleZ(1.0D),
                                        1,
                                        world.random.nextGaussian() * 0.2D,
                                        world.random.nextGaussian() * 0.2D,
                                        world.random.nextGaussian() * 0.2D,
                                        0.0);
                            }
                            coolDownDamage(user, hand, 200, 50);
                        }
                    } else {
                        fireLaser(serverWorld, user, ParticleTypes.HAPPY_VILLAGER, 20);
                        if (user.raycast(20, 0, false) instanceof BlockHitResult blockHitResult) {
                            BlockPos blockPos = blockHitResult.getBlockPos();
                            BlockPos blockPos2 = blockPos.offset(blockHitResult.getSide());
                            if (useOnFertilizable(user.getStackInHand(hand), world, blockPos)) {
                                if (!world.isClient) {
                                    user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
                                    world.syncWorldEvent(1505, blockPos, 15);
                                }
                                coolDownDamage(user, hand, 20, 20);
                                yield ActionResult.SUCCESS;
                            } else {
                                BlockState blockState = world.getBlockState(blockPos);
                                boolean bl = blockState.isSideSolidFullSquare(world, blockPos, blockHitResult.getSide());
                                if (bl && useOnGround(user.getStackInHand(hand), world, blockPos2, blockHitResult.getSide())) {
                                    if (!world.isClient) {
                                        user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
                                        world.syncWorldEvent(1505, blockPos2, 15);
                                    }
                                    coolDownDamage(user, hand, 20, 20);
                                    yield ActionResult.SUCCESS;
                                } else {
                                    yield ActionResult.SUCCESS;
                                }
                            }
                        }
                    }
                    yield ActionResult.SUCCESS;
                }

                case "ice_staff" -> {
                    if (ModUtils.SHIFT){
                        BlockPos playerPos = user.getBlockPos();
                        int radius = 4;
                        for (int x = -radius; x <= radius; x++) {
                            for (int y = -1; y <= 1; y++) {
                                for (int z = -radius; z <= radius; z++) {
                                    BlockPos pos = playerPos.add(x, y, z);
                                    // Spawn snow particles
                                    serverWorld.spawnParticles(ParticleTypes.SNOWFLAKE,
                                            pos.getX() + 0.5,
                                            pos.getY() + 0.5,
                                            pos.getZ() + 0.5, 1,0, 0.0, 0.0,0);

                                    // Damage all entities inside the box
                                    for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, new Box(pos).expand(0.5), e -> true)) {
                                        if (entity != user) { // Ensure the player is not damaged
                                            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 50, 10), user);
                                            entity.damage(serverWorld, user.getDamageSources().magic(), 7); // Example damage value
                                        }
                                    }
                                }
                            }
                        }
                        coolDownDamage(user, hand, 160, 100);
                    } else {

                        for (LivingEntity jared : fireLaser(serverWorld, user, ParticleTypes.SNOWFLAKE, 20)) {
                            jared.damage(serverWorld, user.getDamageSources().magic(), 7);
                            jared.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 50, 10), user);
                            coolDownDamage(user, hand, 40, 10);
                        }
                    }
                    yield ActionResult.SUCCESS;
                }
                case "lava_staff" -> {
                    boolean entityHit = false;
                    for (LivingEntity jared : fireLaser(serverWorld, user, ParticleTypes.LAVA, 20)){
                        if (ModUtils.lavaBlockPos != null) {
                            world.setBlockState(ModUtils.lavaBlockPos, Blocks.AIR.getDefaultState());
                        }
                        if (ModUtils.SHIFT) {
                            for (int x = -1; x <= 1; x++) {
                                for (int z = -1; z <= 1; z++) {
                                    BlockPos pos = jared.getBlockPos().add(x, 0, z);
                                    world.setBlockState(pos, Blocks.LAVA.getDefaultState().with(LEVEL, 1)); // Replace with desired block action
                                }
                            }
                            coolDownDamage(user, hand, 160, 50);
                        } else {
                            world.setBlockState(jared.getBlockPos(), Blocks.LAVA.getDefaultState().with(LEVEL, 0));
                            ModUtils.lavaBlockPos = jared.getBlockPos();
                            coolDownDamage(user, hand, 80, 20);
                        }
                        entityHit = true;
                    }
                    if (!entityHit) {
                        if (user.raycast(20, 0, false) instanceof BlockHitResult blockHitResult) {
                            if (!world.getBlockState(blockHitResult.getBlockPos()).isAir()) {
                                if (world.getBlockState(blockHitResult.getBlockPos().offset(blockHitResult.getSide())).isAir()) {

                                    if (ModUtils.SHIFT) {
                                        for (int x = -1; x <= 1; x++) {
                                            for (int z = -1; z <= 1; z++) {
                                                BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide()).add(x, 0, z);
                                                world.setBlockState(pos, Blocks.LAVA.getDefaultState().with(LEVEL, 1)); // Replace with desired block action
                                            }
                                        }
                                        coolDownDamage(user, hand, 160, 50);
                                    } else {

                                        if (ModUtils.lavaBlockPos != null) {
                                            world.setBlockState(ModUtils.lavaBlockPos, Blocks.AIR.getDefaultState());
                                        }
                                        world.setBlockState(blockHitResult.getBlockPos().offset(blockHitResult.getSide()), Blocks.LAVA.getDefaultState().with(LEVEL, 0));
                                        ModUtils.lavaBlockPos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
                                        coolDownDamage(user, hand, 80, 20);
                                    }
                                }
                            }
                        }
                    }
                    yield ActionResult.SUCCESS;
                }
                case "wither_staff" -> {
                    if (!ModUtils.SHIFT) {
                        for (LivingEntity jared : fireLaser(serverWorld, user, ParticleTypes.DUST_PLUME, 20)) {
                            jared.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 80, 1), user);
                            coolDownDamage(user, hand, 20, 20);
                        }
                    } else {
                        WitherSkullEntity skull = new WitherSkullEntity(EntityType.WITHER_SKULL, world);
                        skull.setVelocity(user, user.getPitch(), user.getYaw(), 1.0f, 3f, 1.0f);
                        skull.setPos(user.getX(), user.getY() + 1, user.getZ());
                        world.spawnEntity(skull);
                        coolDownDamage(user, hand, 40, 40);
                    }
                    yield ActionResult.SUCCESS;
                }
                case "storm_staff" -> {
                    if (ModUtils.SHIFT) {
                        int radius = 2;
                        for (int x = -radius; x <= radius; x++) {
                            for (int z = -radius; z <= radius; z++) {
                                if (x != 0 && z != 0) {
                                    LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                                    lightningEntity.setPos(user.getX() + x, user.getY(), user.getZ() + z);
                                    serverWorld.spawnEntity(lightningEntity);
                                }
                            }
                        }
                        coolDownDamage(user, hand, 200, 80);
                    } else {
                        for (LivingEntity jared : fireLaser(serverWorld, user, ParticleTypes.CLOUD, 20)) {
                            LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                            lightningEntity.setPos(jared.getX(), jared.getY(), jared.getZ());
                            serverWorld.spawnEntity(lightningEntity);
                            coolDownDamage(user, hand, 40, 40);
                        }
                    }
                    yield ActionResult.SUCCESS;
                }
                case "water_staff" -> {
                    if (ModUtils.SHIFT){
                        user.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 400, 2, true, true, true), null);
                        user.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 400, 2, true, true, true), null);
                        coolDownDamage(user, hand, 400, 100);
                    } else {
                        fireLaser(serverWorld, user, ParticleTypes.DRIPPING_WATER, 20);
                        if (user.raycast(20, 0, false) instanceof BlockHitResult blockHitResult) {
                            if (!world.getBlockState(blockHitResult.getBlockPos()).isAir()) {
                                if (world.getBlockState(blockHitResult.getBlockPos().offset(blockHitResult.getSide())).isAir()) {
                                    world.setBlockState(blockHitResult.getBlockPos().offset(blockHitResult.getSide()), Blocks.WATER.getDefaultState());
                                    coolDownDamage(user, hand, 40, 40);
                                }
                            }
                        }
                    }
                    yield ActionResult.SUCCESS;
                }
                /*
                case "wind_staff" -> {
                    if (ModUtils.SHIFT){
                        Position pos = user.getPos();

                        CustomExplosionBehavior explosionBehavior = new CustomExplosionBehavior();
                        int radius = 1;
                        for (int x = -radius; x <= radius; x++) {
                            for (int z = -radius; z <= radius; z++) {
                                world.createExplosion(user,
                                        null,
                                        explosionBehavior,
                                        pos.getX() + x,
                                        pos.getY(),
                                        pos.getZ() + z,
                                        2,
                                        false,
                                        World.ExplosionSourceType.NONE,
                                        ParticleTypes.EXPLOSION,
                                        ParticleTypes.EXPLOSION_EMITTER,
                                        SoundEvents.ENTITY_GENERIC_EXPLODE);
                            }
                        }
                        coolDownDamage(user, hand, 160, 80);

                    } else {
                        for (LivingEntity jared : fireLaser(serverWorld, user, ParticleTypes.CLOUD, 20)) {
                            CustomExplosionBehavior explosionBehavior = new CustomExplosionBehavior();

                            Vec3d directionToPlayer = user.getPos().subtract(jared.getPos()).normalize();
                            Vec3d pos = jared.getPos().add(directionToPlayer.multiply(1));

                            world.createExplosion(user,
                                    null,
                                    explosionBehavior,
                                    pos.getX(),
                                    pos.getY(),
                                    pos.getZ(),
                                    4,
                                    false,
                                    World.ExplosionSourceType.NONE,
                                    ParticleTypes.EXPLOSION,
                                    ParticleTypes.EXPLOSION_EMITTER,
                                    SoundEvents.ENTITY_GENERIC_EXPLODE);

                            coolDownDamage(user, hand, 40, 20);
                        }
                    }
                    yield ActionResult.SUCCESS;
                }


                 */
                case "redstone_staff" -> {
                    fireLaser(serverWorld, user, new DustParticleEffect(1, 1.0F), 40);
                    if (user.raycast(40, 0, false) instanceof BlockHitResult blockHitResult) {
                        if (world.getBlockState(blockHitResult.getBlockPos().offset(blockHitResult.getSide())).isAir()) {
                            if (ModUtils.redstoneStaffBlockPos != null){
                                world.setBlockState(ModUtils.redstoneStaffBlockPos, Blocks.AIR.getDefaultState());
                            }
                            ModUtils.redstoneStaffBlockPos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
                            world.setBlockState(blockHitResult.getBlockPos().offset(blockHitResult.getSide()), ModBlocks.REDSTONE_STAFF_BLOCK.getDefaultState());
                            coolDownDamage(user, hand, 40, 25);
                        }
                    }
                    yield ActionResult.SUCCESS;
                }
                default -> super.use(world, user, hand);
            };

        }
        return ActionResult.SUCCESS;
    }

    public static List<LivingEntity> fireLaser(World world, LivingEntity user, ParticleEffect particleType, double beamDistance) {
        List<LivingEntity> finalList = new ArrayList<>(List.of());

        if (!world.isClient()) {
            Vec3d playerPos = user.getPos();
            Vec3d lookDirection = user.getRotationVector();
            ServerWorld serverWorld = (ServerWorld) world;

            for (int i = 0; i <= 20; i++) {
                double progress = (i / 20.0) * beamDistance;
                Vec3d particlePos = playerPos.add(lookDirection.multiply(progress, progress, progress));
                serverWorld.spawnParticles(particleType, particlePos.x, particlePos.y + 1, particlePos.z, 1, 0.0, 0.0, 0.0, 0.0);
            }
            Box beamBox = new Box(playerPos.add(lookDirection.multiply(-0.5, -0.5, -0.5)),
                    playerPos.add(lookDirection.multiply(beamDistance + 0.5, beamDistance + 0.5, beamDistance + 0.5)));

            List<LivingEntity> entitiesInPath = world.getEntitiesByClass(LivingEntity.class, beamBox, e -> e != user);
            for (LivingEntity jared : entitiesInPath) {
                if (jared.isEntityLookingAtMe(user, 0.025, true, false, jared.getEyeY())) {

                    for (int i = 1; i < 20; ++i) {
                        serverWorld.spawnParticles(particleType,
                                jared.getParticleX(1.0D),
                                jared.getY() + 1,
                                jared.getParticleZ(1.0D),
                                1,
                                world.random.nextGaussian() * 0.2D,
                                world.random.nextGaussian() * 0.2D,
                                world.random.nextGaussian() * 0.2D,
                                0.0);
                    }
                    finalList.add(jared);
                }
            }
        }
        return finalList;
    }
    public static void fireLaser(World world, LivingEntity user, ParticleEffect particleType, LivingEntity entity) {
        double beamDistance = user.distanceTo(entity);
        if (!world.isClient()) {
            Vec3d playerPos = user.getPos();
            Vec3d lookDirection = new Vec3d(entity.getX() - user.getX(), entity.getY() - user.getY(), entity.getZ() - user.getZ()).normalize();
            ServerWorld serverWorld = (ServerWorld) world;

            for (int i = 0; i <= beamDistance; i++) {
                double progress = (i / beamDistance) * beamDistance;
                Vec3d particlePos = playerPos.add(lookDirection.multiply(progress, progress, progress));
                serverWorld.spawnParticles(particleType, particlePos.x, particlePos.y + 1, particlePos.z, 1, 0.0, 0.0, 0.0, 0.0);
            }
            for (int i = 1; i < 20; ++i) {
                serverWorld.spawnParticles(particleType,
                        entity.getParticleX(1.0D),
                        entity.getY() + 1,
                        entity.getParticleZ(1.0D),
                        1,
                        world.random.nextGaussian() * 0.2D,
                        world.random.nextGaussian() * 0.2D,
                        world.random.nextGaussian() * 0.2D,
                        0.0);
            }
        }
    }
    public void coolDownDamage(PlayerEntity user, Hand hand, int cooldown, int damage){
        user.getItemCooldownManager().set(user.getStackInHand(hand), cooldown);
        if (hand == Hand.MAIN_HAND) {
            user.getStackInHand(hand).damage(damage, user, EquipmentSlot.MAINHAND);
        } else {
            user.getStackInHand(hand).damage(damage, user, EquipmentSlot.OFFHAND);
        }
    }

    public static boolean useOnFertilizable(ItemStack stack, World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        Block var5 = blockState.getBlock();
        if (var5 instanceof Fertilizable fertilizable) {
            if (fertilizable.isFertilizable(world, pos, blockState)) {
                if (world instanceof ServerWorld) {
                    if (fertilizable.canGrow(world, world.random, pos, blockState)) {
                        fertilizable.grow((ServerWorld)world, world.random, pos, blockState);
                    }
                }

                return true;
            }
        }

        return false;
    }

    public static boolean useOnGround(ItemStack stack, World world, BlockPos blockPos, @Nullable Direction facing) {
        if (world.getBlockState(blockPos).isOf(Blocks.WATER) && world.getFluidState(blockPos).getLevel() == 8) {
            if (!(world instanceof ServerWorld)) {
                return true;
            } else {
                Random random = world.getRandom();

                label80:
                for(int i = 0; i < 128; ++i) {
                    BlockPos blockPos2 = blockPos;
                    BlockState blockState = Blocks.SEAGRASS.getDefaultState();

                    for(int j = 0; j < i / 16; ++j) {
                        blockPos2 = blockPos2.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                        if (world.getBlockState(blockPos2).isFullCube(world, blockPos2)) {
                            continue label80;
                        }
                    }

                    RegistryEntry<Biome> registryEntry = world.getBiome(blockPos2);
                    if (registryEntry.isIn(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)) {
                        if (i == 0 && facing != null && facing.getAxis().isHorizontal()) {
                            blockState = (BlockState)Registries.BLOCK.getRandomEntry(BlockTags.WALL_CORALS, world.random).map((blockEntry) -> {
                                return ((Block)blockEntry.value()).getDefaultState();
                            }).orElse(blockState);
                            if (blockState.contains(DeadCoralWallFanBlock.FACING)) {
                                blockState = (BlockState)blockState.with(DeadCoralWallFanBlock.FACING, facing);
                            }
                        } else if (random.nextInt(4) == 0) {
                            blockState = (BlockState)Registries.BLOCK.getRandomEntry(BlockTags.UNDERWATER_BONEMEALS, world.random).map((blockEntry) -> {
                                return ((Block)blockEntry.value()).getDefaultState();
                            }).orElse(blockState);
                        }
                    }

                    if (blockState.isIn(BlockTags.WALL_CORALS, (state) -> {
                        return state.contains(DeadCoralWallFanBlock.FACING);
                    })) {
                        for(int k = 0; !blockState.canPlaceAt(world, blockPos2) && k < 4; ++k) {
                            blockState = (BlockState)blockState.with(DeadCoralWallFanBlock.FACING, Direction.Type.HORIZONTAL.random(random));
                        }
                    }

                    if (blockState.canPlaceAt(world, blockPos2)) {
                        BlockState blockState2 = world.getBlockState(blockPos2);
                        if (blockState2.isOf(Blocks.WATER) && world.getFluidState(blockPos2).getLevel() == 8) {
                            world.setBlockState(blockPos2, blockState, 3);
                        } else if (blockState2.isOf(Blocks.SEAGRASS) && ((Fertilizable)Blocks.SEAGRASS).isFertilizable(world, blockPos2, blockState2) && random.nextInt(10) == 0) {
                            ((Fertilizable)Blocks.SEAGRASS).grow((ServerWorld)world, random, blockPos2, blockState2);
                        }
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }
}
