package com.tevin.pvpkeepinventory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class PvPKeepInventory implements ModInitializer {

    private static final Map<ServerPlayerEntity, ItemStack[]> savedInventories = new HashMap<>();

    @Override
    public void onInitialize() {

        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) -> {

            if (entity instanceof ServerPlayerEntity player) {

                if (source.getAttacker() instanceof PlayerEntity) {

                    ItemStack[] items = new ItemStack[player.getInventory().size()];

                    for (int i = 0; i < player.getInventory().size(); i++) {
                        items[i] = player.getInventory().getStack(i).copy();
                    }

                    savedInventories.put(player, items);

                    // Prevent item drops
                    player.getInventory().clear();
                }
            }

            return true;
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {

            if (entity instanceof ServerPlayerEntity player) {

                ItemStack[] saved = savedInventories.remove(player);

                if (saved != null) {

                    for (int i = 0; i < saved.length; i++) {
                        player.getInventory().setStack(i, saved[i]);
                    }
                }
            }
        });
    }
}
