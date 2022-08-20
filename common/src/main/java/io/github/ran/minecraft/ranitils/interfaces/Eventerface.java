package io.github.ran.minecraft.ranitils.interfaces;

import net.minecraft.client.Minecraft;

public interface Eventerface {
    void tick(Minecraft mc);
    void disconnectWorld();
}
