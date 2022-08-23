package io.github.ran.minecraft.ranitils.interfaces;

import net.minecraft.client.Minecraft;

// Good name, I agree
public interface Eventerface {
    void tick(Minecraft mc);
    void disconnectWorld();
}
