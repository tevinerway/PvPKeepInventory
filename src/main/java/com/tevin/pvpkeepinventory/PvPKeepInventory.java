package com.tevin.pvpkeepinventory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class PvPKeepInventory implements ModInitializer {

    @Override
    public void onInitialize() {

        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, damageAmount) -> {

            if (entity instanceof ServerPlayerEntity player) {

                if (isKilledByPlayer(source)) {
                    keepInventory(player);
                }
            }

            return true;
        });
    }

    private boolean isKilledByPlayer(DamageSource source) {
        return source.getAttacker() instanceof PlayerEntity;
    }

    private void keepInventory(ServerPlayerEntity player) {
        player.getInventory().clone();
        player.getAbilities().flying = player.getAbilities().flying;
    }
}
