package net.josh.magicstaffs.client.mixin;

import net.josh.magicstaffs.ModUtils;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class PlayerMixin {
    @Inject(method="tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ModUtils.SHIFT = MinecraftClient.getInstance().options.sneakKey.isPressed();
    }
}
