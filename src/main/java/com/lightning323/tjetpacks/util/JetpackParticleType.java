package com.lightning323.tjetpacks.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

public enum JetpackParticleType {

    NONE(null),
    FLAME(ParticleTypes.FLAME),
    CLOUD(ParticleTypes.CAMPFIRE_COSY_SMOKE),
//    SMOKE(ParticleTypes.SMOKE),
    ;

    public final ParticleOptions particleData;

    JetpackParticleType(ParticleOptions particleData) {
        this.particleData = particleData;
    }

    public ParticleOptions getParticleData() {
        return particleData;
    }
}