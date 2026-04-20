package com.astro.core.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.astro.core.AstroCore;

import java.util.List;

@SuppressWarnings("all")
@Mod.EventBusSubscriber(modid = AstroCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HoeHarvestHandler {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        ItemStack heldItem = player.getItemInHand(event.getHand());

        if (!heldItem.is(ItemTags.HOES)) return;
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (!(block instanceof CropBlock crop)) return;
        if (!crop.isMaxAge(state)) return;

        if (level instanceof ServerLevel serverLevel) {
            LootParams.Builder paramsBuilder = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                    .withParameter(LootContextParams.TOOL, heldItem)
                    .withOptionalParameter(LootContextParams.THIS_ENTITY, player);

            List<ItemStack> drops = state.getDrops(paramsBuilder);

            // Remove one seed from drops so replanting works naturally
            for (ItemStack drop : drops) {
                if (crop.getStateForAge(0).getBlock().asItem() == drop.getItem()) {
                    drop.shrink(1);
                    break;
                }
            }

            // Reset crop to age 0
            level.setBlock(pos, crop.getStateForAge(0), Block.UPDATE_ALL);

            // Spawn drops
            for (ItemStack drop : drops) {
                if (!drop.isEmpty()) {
                    ItemEntity itemEntity = new ItemEntity(level,
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
                    level.addFreshEntity(itemEntity);
                }
            }

            // Play sound and damage hoe
            level.playSound(null, pos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0f, 1.0f);
            heldItem.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(event.getHand()));
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
    }
}