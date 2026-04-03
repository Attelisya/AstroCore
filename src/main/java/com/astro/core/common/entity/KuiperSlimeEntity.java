package com.astro.core.common.entity;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
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

}