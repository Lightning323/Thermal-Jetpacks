package com.lightning323.tjetpacks.mixin;

import com.lightning323.tjetpacks.I_ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.server.network.ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin implements I_ServerGamePacketListenerImpl {
    @Shadow
    private int aboveGroundTickCount;

    @Override
    public int getAboveGroundTickCount() {
        return aboveGroundTickCount;
    }

    @Override
    public void setAboveGroundTickCount(int val) {
        aboveGroundTickCount = val;
    }
}
