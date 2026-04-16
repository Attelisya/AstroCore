package com.astro.core.events;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.astro.core.AstroCore;

@Mod.EventBusSubscriber(modid = AstroCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KeepArmorOnDeath {

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!stack.isEmpty()) {
                player.getPersistentData().put("saved_armor_" + slot.getName(), stack.save(new net.minecraft.nbt.CompoundTag()));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        for (EquipmentSlot slot : ARMOR_SLOTS) {
            String key = "saved_armor_" + slot.getName();
            if (player.getPersistentData().contains(key)) {
                ItemStack stack = ItemStack.of(player.getPersistentData().getCompound(key));
                player.setItemSlot(slot, stack);
                player.getPersistentData().remove(key);
            }
        }
    }
}