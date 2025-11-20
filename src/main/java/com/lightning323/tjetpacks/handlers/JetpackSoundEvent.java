package com.lightning323.tjetpacks.handlers;

import com.lightning323.tjetpacks.RegistryHandler;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class JetpackSoundEvent extends AbstractTickableSoundInstance {

    //A list of all jetpack sounds for all players
    private static final Map<Integer, JetpackSoundEvent> PLAYING_FOR = Collections.synchronizedMap(new HashMap<>());
    private static final float MAX_VOLUME = 1F;
    private static final float DURATION = 5;

    private final Player player;
    private int fadeOut = -1;


    public JetpackSoundEvent(Player player) {
        super(RegistryHandler.SOUND_JETPACK_OTHER.get(),
                SoundSource.PLAYERS, RandomSource.create());
        this.player = player;
        this.looping = true;
        PLAYING_FOR.put(player.getId(), this);
    }

    public static boolean playing(int entityId) {
        return PLAYING_FOR.containsKey(entityId)
                && PLAYING_FOR.get(entityId) != null
                && !PLAYING_FOR.get(entityId).isStopped();
    }

    @Override
    public void tick() {
        if (this.player.isSpectator() || this.player.getAbilities().flying) {
            this.stop();
        }
        Vec3 pos = this.player.position();
        this.x = (float) pos.x();
        this.y = (float) pos.y();// - 10;
        this.z = (float) pos.z();
        if (this.fadeOut < 0) {
            this.fadeOut = 0;
            synchronized (PLAYING_FOR) {
                PLAYING_FOR.remove(this.player.getId());
            }
        } else if (this.fadeOut >= DURATION) {
            this.stop();
        } else {
            this.volume = MAX_VOLUME - this.fadeOut / DURATION;
            this.fadeOut++;
        }
    }
}
