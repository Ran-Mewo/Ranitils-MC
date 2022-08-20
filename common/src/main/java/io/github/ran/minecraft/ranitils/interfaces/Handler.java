package io.github.ran.minecraft.ranitils.interfaces;

import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.List;

public interface Handler {
    List<KeyMapping> keybinds = new ArrayList<>();
    List<Eventerface> events = new ArrayList<>();

    void init();
}
