package com.astro.core.common.entity;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import javax.annotation.Nullable;
import org.joml.Vector3f;

@SuppressWarnings("all")
public class KuiperSlimeEntity extends Slime {

    public KuiperSlimeEntity(EntityType<? extends Slime> type, Level level) {
        super(type, level);
    }

    @Override
    @MethodsReturnNonnullByDefault
    public ParticleOptions getParticleType() {
        return new DustParticleOptions(new Vector3f(0.4f, 0.2f, 0.8f), 1.0f);
    }

    public static boolean checkSlimeSpawnRules(EntityType<KuiperSlimeEntity> type,
        ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).isSolidRender(level, pos.below());
    }
}
